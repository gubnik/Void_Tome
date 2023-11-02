package net.nikgub.void_tome.entities;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.nikgub.void_tome.base.VTAttributes;
import net.nikgub.void_tome.base.VTDamageSource;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.enchantments.VTEnchantmentHelper;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VoidTomeDefaultProjectile extends Fireball implements ItemSupplier {
    private final int maxLifetime = 10;
    private final ItemStack voidTome;
    public VoidTomeDefaultProjectile(EntityType<? extends Fireball> p_37006_, Level p_37007_, ItemStack voidTome) {
        super(p_37006_, p_37007_);
        this.voidTome = voidTome;
    }
    public VoidTomeDefaultProjectile(EntityType<? extends Fireball> p_37006_, Level p_37007_) {
        super(p_37006_, p_37007_);
        this.voidTome = null;
    }
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.PORTAL;
    }
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level.isClientSide) {
            float damage = (this.getOwner() instanceof LivingEntity owner) ? (float) owner.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()) : VoidTomeItem.DAMAGE;
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            entity.hurt(VTDamageSource.VOID_STAR(owner), damage);
        }
    }
    public @NotNull ItemStack getItem(){
        return new ItemStack(Items.AIR);
    }
    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
        if(this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 1, 0.01, 0.01, 0.01, 0.05);
            List<? extends LivingEntity> collectedEntities = VTUtils.entityCollector(new Vec3(this.getX(), this.getY(), this.getZ()), 0.4, level);
            List<VTEnchantmentHelper.Meaning> meanings = new ArrayList<>();
            if(!(voidTome == null)){
                meanings = VTEnchantmentHelper.Meaning.getMeaningEnchantments(this.voidTome);
            }
            for(LivingEntity livingEntity : collectedEntities){
                if(this.getOwner() instanceof LivingEntity owner) {
                    livingEntity.hurt(VTDamageSource.VOID_TOME(owner), (float) owner.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()));
                    for (VTEnchantmentHelper.Meaning meaning : meanings) {
                        meaning.getAttack().accept(owner, livingEntity, this.voidTome);
                    }
                }
            }
        }
        if(this.tickCount > this.maxLifetime && !this.level.isClientSide){
            this.discard();
        }
    }
    protected boolean shouldBurn() {
        return false;
    }
}
