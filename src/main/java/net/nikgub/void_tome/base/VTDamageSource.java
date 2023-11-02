package net.nikgub.void_tome.base;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class VTDamageSource {
    public static DamageSource VOID_TOME(Entity entity){
        return new EntityDamageSource("void_tome", entity).setMagic().setProjectile();
    }
    public static DamageSource VOID_RAIN(Entity entity){
        return new EntityDamageSource("void_rain", entity).setMagic().setProjectile();
    }
    public static DamageSource VOID_GLASS(Entity entity){
        return new EntityDamageSource("void_glass", entity).setMagic();
    }
    public static DamageSource VOID_STAR(Entity entity){
        return new EntityDamageSource("void_star", entity).setMagic().setProjectile();
    }
    public static DamageSource VOID_NOTHING(Entity entity){
        return new EntityDamageSource("void_nothing", entity).setMagic().setExplosion();
    }
}
