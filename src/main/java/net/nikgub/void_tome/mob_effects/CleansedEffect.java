package net.nikgub.void_tome.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.nikgub.void_tome.base.VTUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class CleansedEffect extends MobEffect {
    private static final UUID ATTACK_MOD = UUID.fromString("c251703c-3a88-4f25-bdb5-e8304e3931d6"); // c251703c-3a88-4f25-bdb5-e8304e3931d6
    private static final UUID HEALTH_MOD = UUID.fromString("bbdbe935-fb83-40ab-864d-2249e5bfc911");
    private static final UUID SPEED_MOD = UUID.fromString("3ff91856-777d-11ee-b962-0242ac120002");
    private static final UUID ARMOR_MOD = UUID.fromString("43cbe0da-777d-11ee-b962-0242ac120002");
    private static final Attribute[] ATTRIBUTES = {
            Attributes.ATTACK_DAMAGE,
            Attributes.MAX_HEALTH,
            Attributes.MOVEMENT_SPEED,
            Attributes.ARMOR
    };
    private static final UUID[] UUIDS = {
            ATTACK_MOD,
            HEALTH_MOD,
            SPEED_MOD,
            ARMOR_MOD
    };
    protected CleansedEffect() {
        super(MobEffectCategory.HARMFUL, VTUtils.rgbToColorInteger(20, 10, 180));
        for(Attribute attribute : ATTRIBUTES) {
            this.addAttributeModifier(attribute, "Cleansed modifier", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
    }
    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull Attribute attribute, @NotNull String desc, double amount, AttributeModifier.@NotNull Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUIDS[Arrays.stream(ATTRIBUTES).toList().indexOf(attribute)], desc, amount, operation);
        this.attributeModifiers.put(attribute, attributemodifier);
        return this;
    }
}
