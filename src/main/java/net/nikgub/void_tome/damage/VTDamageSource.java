package net.nikgub.void_tome.damage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class VTDamageSource {
    public static DamageSource VOID_TOME(Entity entity){
        return new DamageSource(entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(VTDamageTypes.VOID_TOME_RK), entity);
    }
    public static DamageSource VOID_RAIN(Entity entity){
        return new DamageSource(entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(VTDamageTypes.VOID_RAIN_RK), entity);
    }
    public static DamageSource VOID_GLASS(Entity entity){
        return new DamageSource(entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(VTDamageTypes.VOID_GLASS_RK), entity);
    }
    public static DamageSource VOID_NOTHING(Entity entity){
        return new DamageSource(entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(VTDamageTypes.VOID_NOTHING_RK), entity);
    }
}
