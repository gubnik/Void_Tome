package net.nikgub.void_tome.enchantments;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.nikgub.void_tome.base.Styles;
import org.jetbrains.annotations.NotNull;

public class VoidTomeEnchantment extends Enchantment {
    private final Type type;
    protected VoidTomeEnchantment(Rarity rarity, EquipmentSlot[] slots, Type type) {
        super(rarity, VTEnchantments.VOID_TOME, slots);
        this.type = type;
    }
    @Override
    public @NotNull Component getFullname(int p_44701_) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());
        if(!this.isCurse()){
            if(type == Type.MEANING) {
                mutablecomponent.withStyle(Styles.VOID_MEANING);
            } else mutablecomponent.withStyle(Styles.VOID_FORM);
        } else mutablecomponent.withStyle(Styles.VOID_CURSE);

        if (p_44701_ != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(" ").append(Component.translatable("enchantment.level." + p_44701_));
        }

        return mutablecomponent;
    }
    protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return !(enchantment instanceof VoidTomeEnchantment voidTomeEnchantment && voidTomeEnchantment.type == Type.FORM) || enchantment.isCurse() || this.isCurse() || this.type == Type.MEANING;
    }
    @Override
    public int getMaxLevel(){ return 1;}

    public Type getType() {
        return type;
    }

    public enum Type{
        FORM,
        MEANING
    }
}
