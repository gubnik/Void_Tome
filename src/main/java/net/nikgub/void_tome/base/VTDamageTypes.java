package net.nikgub.void_tome.base;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.nikgub.void_tome.VoidTomeMod;

public class VTDamageTypes {
    public static ResourceKey<DamageType> VOID_TOME_RK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, "void_tome"));
    public static ResourceKey<DamageType> VOID_RAIN_RK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, "void_rain"));
    public static ResourceKey<DamageType> VOID_GLASS_RK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, "void_glass"));
    public static ResourceKey<DamageType> VOID_NOTHING_RK = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, "void_nothing"));
    public static DamageType VOID_TOME = new DamageType("void_tome", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_RAIN = new DamageType("void_rain", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_GLASS = new DamageType("void_glass", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_NOTHING = new DamageType("void_nothing", DamageScaling.NEVER, 0.1f);
    public static void generate(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(VOID_TOME_RK, VOID_TOME);
        bootstrap.register(VOID_RAIN_RK, VOID_RAIN);
        bootstrap.register(VOID_GLASS_RK, VOID_GLASS);
        bootstrap.register(VOID_NOTHING_RK, VOID_NOTHING);
    }
}
