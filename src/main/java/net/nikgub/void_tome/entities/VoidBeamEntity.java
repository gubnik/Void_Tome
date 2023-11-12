package net.nikgub.void_tome.entities;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.nikgub.void_tome.damage.VTDamageSource;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.enchantments.VTEnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoidBeamEntity extends AttackEffectEntity{
    @Nullable
    private final ItemStack voidTome;
    public VoidBeamEntity(EntityType<? extends AttackEffectEntity> entityType, Level level, @Nullable ItemStack voidTome) {
        super(entityType, level);
        this.voidTome = voidTome;
        this.lifetime = 10;
    }
    public VoidBeamEntity(EntityType<? extends AttackEffectEntity> entityType, Level level) {
        super(entityType, level);
        this.voidTome = null;
        this.lifetime = 10;
    }
    public void tick() {
        super.tick();
        List<VTEnchantmentHelper.Meaning> meanings;
        DamageSource damageSource = VTDamageSource.VOID_RAIN(this.getOwner());
        if(voidTome != null) {
            meanings = VTEnchantmentHelper.Meaning.getMeaningEnchantments(voidTome);
        } else meanings = new ArrayList<>();
        List<LivingEntity> collectedEntities = new ArrayList<>();
        for(int i = 0; i < 32; i++) {
            collectedEntities.addAll(VTUtils.entityCollector(new Vec3(this.getX(), this.getY() + i, this.getZ()), 0.2, this.level()));
        }
        if(collectedEntities.isEmpty()) return;
        for(LivingEntity livingEntity : collectedEntities){
            if(this.getOwner() != null && livingEntity != this.getOwner()){
                livingEntity.hurt(damageSource, 4);
                for(VTEnchantmentHelper.Meaning meaning : meanings){
                    meaning.getAttack().accept(this.getOwner(), livingEntity, voidTome);
                }
            }
        }
    }
}
