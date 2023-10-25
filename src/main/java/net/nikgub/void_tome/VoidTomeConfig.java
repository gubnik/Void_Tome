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
    public final ForgeConfigSpec.ConfigValue<Integer> maxUse;
    VoidTomeConfig(ForgeConfigSpec.Builder builder){
            builder.push("Void Tome Configuration");
        dragonLocked = builder
                .comment("Should Void Tome be locked behind killing the Dragon?")
                .define("Locked behind Free the End?", true);
        maxUse = builder
                .comment("For how many ticks can a Void Tome be continuously used?")
                .defineInRange("Max duration of a continuous use", 20, 1, 1200);
            // builder.pop();
        }
        @Override
        public ModConfig getConfig() {
            return null;
        }
    }
