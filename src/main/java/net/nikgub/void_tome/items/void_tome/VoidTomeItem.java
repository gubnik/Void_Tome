package net.nikgub.void_tome.items.void_tome;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.extensions.IForgeItem;
import net.nikgub.void_tome.base.VTAttributes;
import net.nikgub.void_tome.enchantments.VTEnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class VoidTomeItem extends Item implements IClientItemExtensions, IForgeItem {
    public static UUID BASE_SPEED = BASE_ATTACK_SPEED_UUID;
    public static int DAMAGE = 6;
    public VoidTomeItem(Properties properties) {
        super(properties);

    }
    public boolean isEnchantable(@NotNull ItemStack p_41456_) {
        return true;
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand){
        if(hand == InteractionHand.OFF_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));
        if(VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(player.getItemInHand(hand))) == VTEnchantmentHelper.Form.GLASS)
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        player.startUsingItem(hand);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    public int getUseDuration(@NotNull ItemStack itemStack){
        return switch (VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack))){
            case GLASS -> 80;
            case NOTHING -> 20;
            default -> 72000;
        };
    }
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        this.releaseUsing(itemStack, level, livingEntity, 0);
        return itemStack;
    }
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int tick) {
        if(livingEntity instanceof Player player) player.getCooldowns().addCooldown(itemStack.getItem(), (this.getUseDuration(itemStack) >= 72000) ? 200 : this.getUseDuration(itemStack));
    }
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack){
        return UseAnim.CUSTOM;
    }
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, ItemStack itemStack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(VTAttributes.VOID_TOME_DAMAGE.get(), new AttributeModifier(VTAttributes.BASE_VOID_TOME_DAMAGE_UUID, "Weapon modifier", DAMAGE, AttributeModifier.Operation.ADDITION));
        if(VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack)) == VTEnchantmentHelper.Form.GLASS){
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(VoidTomeItem.BASE_SPEED, "Weapon modifier", -2.4d, AttributeModifier.Operation.ADDITION));
        }
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getDefaultAttributeModifiers(slot);
    }

    // ANIMATIONS
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, int tick) {
        VTEnchantmentHelper.Form form;
        form = VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack));
        form.getAttack().accept(livingEntity, itemStack);
    }
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new VTClientExtension());
    }
    public static class VTClientExtension implements IClientItemExtensions{
        public static HumanoidModel.ArmPose VOID_TOME = HumanoidModel.ArmPose.create("void_tome", false, (model, entity, arm) -> {
            int tick;
            if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
            else return;
            if(tick <= 0) return;
            ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
            armPart.xRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 2 + model.head.xRot - (float) Math.PI / 2;
            armPart.zRot = Mth.cos( (float) Math.PI * tick * 9 / 180);
        });
        public static HumanoidModel.ArmPose VOID_RAIN = HumanoidModel.ArmPose.create("void_rain", false, (model, entity, arm) -> {
            int tick;
            if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
            else return;
            if(tick <= 0) return;
            ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
            armPart.xRot = (float) Math.PI - Mth.sin( (float) Math.PI * tick * 9 / 180) * -1 * Mth.cos( (float) Math.PI * tick * 9 / 180);
            armPart.zRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 5;
        });
        public static HumanoidModel.ArmPose VOID_GLASS = HumanoidModel.ArmPose.create("void_glass", false, (model, entity, arm) -> {
            int tick;
            if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
            else return;
            if(tick <= 0) return;
            ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
            armPart.xRot = Mth.cos( (float) Math.PI * tick * 18 / 180)  + model.head.xRot - (float) Math.PI / 2;
            armPart.zRot = Mth.cos( (float) Math.PI * tick * 18 / 180);
        });
        public static HumanoidModel.ArmPose VOID_NOTHING = HumanoidModel.ArmPose.create("void_nothing", false, (model, entity, arm) -> {
        });
        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            return VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack)).getAnimation();
        }
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            return (player.getUseItemRemainingTicks() > 0);
        }
    }
    // END OF ANIMATIONS
}
