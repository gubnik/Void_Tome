package net.nikgub.void_tome.mixins;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.nikgub.void_tome.VoidTomeConfig;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.items.VTItems;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    private UUID thrower;
    @Shadow
    public UUID getThrower() {
        return this.thrower;
    }
    @Shadow
    public Entity getThrowingEntity() {
        return Util.mapNullable(this.getThrower(), this.level::getPlayerByUUID);
    }
    @Shadow
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
    @Shadow
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }
    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    @Inject(method = "Lnet/minecraft/world/entity/item/ItemEntity;tick()V", at = @At("HEAD"), cancellable = true)
    private void tickMixin(CallbackInfo callbackInfo){
        if(this.getItem().getItem() instanceof VoidTomeItem){
            this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.01D, 0.0D);
        }
        if(this.level.dimension().equals(Level.END) && this.getY() < 0
        && this.getItem().getItem().equals(Items.BOOK) &&
                (this.getThrowingEntity() instanceof ServerPlayer serverPlayer &&
                        ((VTUtils.hasCompletedTheAdvancement(serverPlayer, new ResourceLocation("minecraft:end/kill_dragon")) || !VoidTomeConfig.COMMON.dragonLocked.get())
                        && VoidTomeConfig.COMMON.defaultObtaining.get())))
        {
            this.discard();
            serverPlayer.addItem(new ItemStack(VTItems.VOID_TOME.get()));
        }
    }
}
