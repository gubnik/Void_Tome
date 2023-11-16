package net.nikgub.void_tome.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.base.VTCreativeTabs;
import net.nikgub.void_tome.base.VTRarity;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;

public class VTItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VoidTomeMod.MOD_ID);
    public static RegistryObject<Item> VOID_TOME = ITEMS.register("void_tome",
            () -> new VoidTomeItem(new Item.Properties().stacksTo(1).rarity(VTRarity.VOID_RARITY).tab(VTCreativeTabs.VOID_TOME_TAB)));
}
