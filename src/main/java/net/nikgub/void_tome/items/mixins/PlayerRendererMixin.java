package net.nikgub.void_tome.items.mixins;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.nikgub.void_tome.base.AnimationUtils;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
    at = @At("HEAD"), cancellable = true)
    private static void getArmPoseMixin(AbstractClientPlayer clientPlayer, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack itemStack = clientPlayer.getItemInHand(hand);
        if(itemStack.getItem() instanceof VoidTomeItem && clientPlayer.getUsedItemHand() == hand
        && clientPlayer.getUseItemRemainingTicks() > 0){
            cir.setReturnValue(AnimationUtils.VOID_TOME);
        }
    }
}
