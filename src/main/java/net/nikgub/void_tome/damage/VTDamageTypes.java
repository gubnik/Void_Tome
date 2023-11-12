package net.nikgub.void_tome.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.nikgub.void_tome.VoidTomeMod;

public class VTDamageTypes {
    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, name));
    }
    public static ResourceKey<DamageType> VOID_TOME_RK = register("void_tome");
    public static ResourceKey<DamageType> VOID_RAIN_RK = register("void_rain");
    public static ResourceKey<DamageType> VOID_GLASS_RK = register("void_glass");
    public static ResourceKey<DamageType> VOID_NOTHING_RK = register("void_nothing");
    public static void generate(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(VOID_TOME_RK, new DamageType(VOID_TOME_RK.location().getPath(), DamageScaling.NEVER, 0.1f));
        bootstrap.register(VOID_RAIN_RK, new DamageType(VOID_RAIN_RK.location().getPath(), DamageScaling.NEVER, 0.1f));
        bootstrap.register(VOID_GLASS_RK, new DamageType(VOID_GLASS_RK.location().getPath(), DamageScaling.NEVER, 0.1f));
        bootstrap.register(VOID_NOTHING_RK, new DamageType(VOID_NOTHING_RK.location().getPath(), DamageScaling.NEVER, 0.1f));
    }
}
