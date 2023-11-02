package net.nikgub.void_tome.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;

public class VTEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VoidTomeMod.MOD_ID);
    public static RegistryObject<EntityType<VoidBeamEntity>> VOID_BEAM = register("void_beam",
            EntityType.Builder.<VoidBeamEntity>of(VoidBeamEntity::new, MobCategory.MISC)
                    .clientTrackingRange(128).setShouldReceiveVelocityUpdates(false));
    public static final RegistryObject<EntityType<VoidTomeDefaultProjectile>> VT_DEFAULT = register("void_tome_projectile",
            EntityType.Builder.<VoidTomeDefaultProjectile>of(VoidTomeDefaultProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registry_name, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITIES.register(registry_name, () -> entityTypeBuilder.build(registry_name));
    }
}
