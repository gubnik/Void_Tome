package net.nikgub.void_tome.base;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.nikgub.void_tome.enchantments.VTEnchantments;
import net.nikgub.void_tome.items.VTItems;
import org.jetbrains.annotations.NotNull;

public class VTCreativeTabs {
    public static final CreativeModeTab VOID_TOME_TAB = (new CreativeModeTab("void_tome")
    {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(VTItems.VOID_TOME.get());
        }
    }
    ).setEnchantmentCategories(VTEnchantments.VOID_TOME);
}
