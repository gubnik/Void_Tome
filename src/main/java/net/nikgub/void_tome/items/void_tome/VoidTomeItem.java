package net.nikgub.void_tome.items.void_tome;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

import java.util.function.Consumer;

public class VoidTomeItem extends Item implements IClientItemExtensions, IForgeItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public static int DAMAGE = 6;
    public VoidTomeItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(VTAttributes.VOID_TOME_DAMAGE.get(), new AttributeModifier(VTAttributes.BASE_VOID_TOME_DAMAGE_UUID, "Weapon modifier", DAMAGE, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    public boolean isEnchantable(@NotNull ItemStack p_41456_) {
        return true;
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand){
        //return InteractionResultHolder.success(player.getItemInHand(hand));
        if(hand == InteractionHand.OFF_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));
        else {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
    }
    public int getUseDuration(@NotNull ItemStack itemStack){
        return switch (VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack))){
            case WAY -> 40;
            case GLASS -> 80;
            case NOTHING -> 60;
            default -> 72000;
        };
    }
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack){
        return UseAnim.CUSTOM;
    }
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    // ANIMATIONS
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, int tick) {
        VTEnchantmentHelper.Form form;
        form = VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack));
        //int icd = 1;
        int icd = switch (form){
            case GLASS, WAY -> 10;
            default -> 1;
        };
        if(tick % icd == 0) form.getAttack().accept(livingEntity, itemStack);
    }
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new VTClientExtension());
    }
    private static class VTClientExtension implements IClientItemExtensions{
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
