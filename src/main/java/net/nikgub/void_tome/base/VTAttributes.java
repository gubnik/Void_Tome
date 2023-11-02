package net.nikgub.void_tome.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;

import java.util.UUID;
@Mod.EventBusSubscriber(modid = VoidTomeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VTAttributes {
    public static UUID BASE_VOID_TOME_DAMAGE_UUID = UUID.fromString("f810b33e-76a3-11ee-b962-0242ac120002");
    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, VoidTomeMod.MOD_ID);
    public static final RegistryObject<Attribute> VOID_TOME_DAMAGE = ATTRIBUTES.register("generic.void_tome_damage",
            () -> new RangedAttribute("attribute.generic.void_tome_damage", 0f, 0f, 512f));
}
