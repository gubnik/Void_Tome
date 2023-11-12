package net.nikgub.void_tome.base;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class VTDamageSource {
    public static DamageSource VOID_TOME(Entity entity){
        return new DamageSource(Holder.direct(VTDamageTypes.VOID_TOME), entity);
    }
    public static DamageSource VOID_RAIN(Entity entity){
        return new DamageSource(Holder.direct(VTDamageTypes.VOID_RAIN), entity);
    }
    public static DamageSource VOID_GLASS(Entity entity){
        return new DamageSource(Holder.direct(VTDamageTypes.VOID_GLASS), entity);
    }
    public static DamageSource VOID_NOTHING(Entity entity){
        return new DamageSource(Holder.direct(VTDamageTypes.VOID_NOTHING), entity);
    }
}
