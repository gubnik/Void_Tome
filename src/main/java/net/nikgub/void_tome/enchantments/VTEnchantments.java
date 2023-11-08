package net.nikgub.void_tome.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;

public class VTEnchantments {
    public static EnchantmentCategory VOID_TOME = EnchantmentCategory.create("void_tome", item -> item instanceof VoidTomeItem);
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, VoidTomeMod.MOD_ID);
    public static RegistryObject<Enchantment> FORM_OF_RAIN = ENCHANTMENTS.register("form_of_rain",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.FORM));
    public static RegistryObject<Enchantment> FORM_OF_GLASS = ENCHANTMENTS.register("form_of_glass",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.FORM));
    public static RegistryObject<Enchantment> FORM_OF_NOTHING = ENCHANTMENTS.register("form_of_nothing",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.FORM){
                @Override
                public boolean isCurse() {return true;}
                @Override
                public boolean isTreasureOnly(){return true;}
            });
    public static RegistryObject<Enchantment> CLEANSING = ENCHANTMENTS.register("cleansing",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.MEANING));
    public static RegistryObject<Enchantment> DARKENING = ENCHANTMENTS.register("darkening",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.MEANING));
    public static RegistryObject<Enchantment> DEVOURING = ENCHANTMENTS.register("devouring",
            () -> new VoidTomeEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[]{}, VoidTomeEnchantment.Type.MEANING){
                @Override
                public boolean isCurse(){return true;}
                @Override
                public boolean isTreasureOnly(){return true;}
            });
}
