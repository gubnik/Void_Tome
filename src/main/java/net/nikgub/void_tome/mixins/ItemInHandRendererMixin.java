package net.nikgub.void_tome.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nikgub.void_tome.enchantments.VTEnchantmentHelper;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@SuppressWarnings("unused")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    public abstract void renderItem(LivingEntity p_109323_, ItemStack p_109324_, ItemTransforms.TransformType p_109325_, boolean p_109326_, PoseStack p_109327_, MultiBufferSource p_109328_, int p_109329_);
    @Shadow
    public abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float f);
    @Inject(method = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At("TAIL"), cancellable = true)
    public void renderItemMixin(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
                           CallbackInfo callbackInfo) {
        if (itemStack.getItem() instanceof VoidTomeItem) {
            if(VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(itemStack)) == VTEnchantmentHelper.Form.GLASS){
                poseStack.scale(1.2f, 1.2f, 1.2f);
                if(transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) poseStack.translate(-0.02,0, 0);
                ItemStack toRender = new ItemStack(Items.GOLDEN_SWORD);
                toRender.getOrCreateTag().putBoolean("VTRender", true);
                this.itemRenderer.renderStatic(livingEntity, toRender, transformType, b, poseStack, multiBufferSource, livingEntity.level, i, OverlayTexture.NO_OVERLAY, livingEntity.getId() + transformType.ordinal());
                callbackInfo.cancel();
            }
        }
    }
    @Inject(method = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At("HEAD"), cancellable = true)
    private void renderArmWithItemMixin(AbstractClientPlayer player, float v, float v1, InteractionHand hand, float v2, ItemStack itemStack, float v3, PoseStack poseStack, MultiBufferSource multiBufferSource, int i1,
                                   CallbackInfo callbackInfo) {
        if(player.getUsedItemHand().equals(hand) && player.getUseItemRemainingTicks() > 0){
            if(itemStack.getItem() instanceof VoidTomeItem) {
                this.applyItemArmTransform(poseStack, hand.equals(InteractionHand.MAIN_HAND) ? HumanoidArm.RIGHT : HumanoidArm.LEFT, v3);
                this.renderItem(player, itemStack, hand.equals(InteractionHand.MAIN_HAND) ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                        false,
                        poseStack,
                        multiBufferSource,
                        i1);
                callbackInfo.cancel();
            }
        }
        //poseStack.popPose();
    }
}