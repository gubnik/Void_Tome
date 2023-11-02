package net.nikgub.void_tome.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;

public class VTMobEffects {
    public static DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, VoidTomeMod.MOD_ID);
    public static RegistryObject<MobEffect> CLEANSED = EFFECTS.register("cleansed",
            CleansedEffect::new);
}
