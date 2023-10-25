package net.nikgub.void_tome.base;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ModRarity{
    public static Rarity VOID_RARITY = Rarity.create("void", new UnaryOperator<Style>() {
        @Override
        public Style apply(Style style) {
            return VOID_STYLE;
        }
    });
    public static Style VOID_STYLE = Style.create(Optional.of(TextColor.fromRgb(-10404731)), Optional.of(Boolean.TRUE), Optional.of(Boolean.FALSE), Optional.of(Boolean.FALSE), Optional.of(Boolean.FALSE), Optional.of(Boolean.FALSE), Optional.of("void"), Optional.of(Style.DEFAULT_FONT));
}
