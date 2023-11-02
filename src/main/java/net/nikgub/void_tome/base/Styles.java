package net.nikgub.void_tome.base;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Optional;

public class Styles {
    public static Style VOID = Style.create(
            Optional.of(TextColor.fromRgb(4920993)),
            Optional.of(Boolean.TRUE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of("void"),
            Optional.of(Style.DEFAULT_FONT));
    public static Style VOID_FORM = Style.create(
            Optional.of(TextColor.fromRgb(5587875)),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.empty(),
            Optional.of(Style.DEFAULT_FONT));
    public static Style VOID_MEANING = Style.create(
            Optional.of(TextColor.fromRgb(9459136)),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.empty(),
            Optional.of(Style.DEFAULT_FONT));
    public static Style VOID_CURSE = Style.create(
            Optional.of(TextColor.fromRgb(12198484)),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.of(Boolean.FALSE),
            Optional.empty(),
            Optional.of(Style.DEFAULT_FONT));
}
