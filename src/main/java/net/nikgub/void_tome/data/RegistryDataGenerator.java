package net.nikgub.void_tome.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.damage.VTDamageTypes;
import net.nikgub.void_tome.damage.VTDamageTypesTags;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, VTDamageTypes::generate);

    // Use addProviders() instead
    private RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", VoidTomeMod.MOD_ID));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistryDataGenerator(output, provider));
        // This is needed here because Minecraft Forge doesn't properly support tagging custom registries, without problems.
        // If you think this looks fixable, please ensure the fixes are tested in runData & runClient as these current issues exist entirely within Forge's internals.
        generator.addProvider(isServer, new VTDamageTypesTags(output, provider.thenApply(r -> append(r, BUILDER)), helper));
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}
