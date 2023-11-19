package net.nikgub.void_tome.damage;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikgub.void_tome.VoidTomeMod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class VTDamageTypesTags extends TagsProvider<DamageType> {
    public VTDamageTypesTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, VoidTomeMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(DamageTypeTags.IS_PROJECTILE).addOptional(VTDamageTypes.VOID_TOME_RK.location());
    }
}