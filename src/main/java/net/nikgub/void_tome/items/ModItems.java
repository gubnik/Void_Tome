package net.nikgub.void_tome.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.base.ModRarity;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;

public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VoidTomeMod.MOD_ID);
    public static RegistryObject<Item> VOID_TOME = ITEMS.register("void_tome",
            () -> new VoidTomeItem(new Item.Properties().stacksTo(1).rarity(ModRarity.VOID_RARITY)));
}
