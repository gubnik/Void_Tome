package net.nikgub.void_tome.base;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;

public class VTDamageTypes {
    public static ResourceKey<DamageType> DAMAGE_TYPES = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(VoidTomeMod.MOD_ID, "dmg"));
    public static DamageType VOID_TOME = new DamageType("void_tome", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_RAIN = new DamageType("void_rain", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_GLASS = new DamageType("void_glass", DamageScaling.NEVER, 0.1f);
    public static DamageType VOID_NOTHING = new DamageType("void_nothing", DamageScaling.NEVER, 0.1f);
    public static void generate(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(DAMAGE_TYPES, VOID_TOME);
        bootstrap.register(DAMAGE_TYPES, VOID_RAIN);
        bootstrap.register(DAMAGE_TYPES, VOID_GLASS);
        bootstrap.register(DAMAGE_TYPES, VOID_NOTHING);
    }
}
