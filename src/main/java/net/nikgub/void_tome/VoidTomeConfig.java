package net.nikgub.void_tome;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class VoidTomeConfig implements IConfigEvent {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final VoidTomeConfig COMMON;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final VoidTomeConfig CLIENT;
    static {
        final Pair<VoidTomeConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(VoidTomeConfig::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
        final Pair<VoidTomeConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(VoidTomeConfig::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
    public final ForgeConfigSpec.ConfigValue<Boolean> dragonLocked;
    public final ForgeConfigSpec.ConfigValue<Boolean> defaultObtaining;
    public final ForgeConfigSpec.ConfigValue<Double> screenShakeAmount;
    VoidTomeConfig(ForgeConfigSpec.Builder builder){
            builder.push("Void Tome Configuration");
        dragonLocked = builder
                .comment("Should Void Tome be locked behind killing the Dragon?")
                .define("Locked behind Free the End", true);
        defaultObtaining = builder
                .comment("Can Void Tome be unlocked by a regular method?")
                .define("Default obtaining method", true);
        screenShakeAmount = builder
                .comment("Defines how severe is screenshake produced by Void Tome effects")
                .defineInRange("Screenshake amount", 0.01d, 0, 1f);
            // builder.pop();
        }
        @Override
        public ModConfig getConfig() {
            return null;
        }
    }
