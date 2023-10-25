package net.nikgub.void_tome.items.mixins;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.base.AnimationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Shadow
    public HumanoidModel.ArmPose leftArmPose;
    @Shadow
    public HumanoidModel.ArmPose rightArmPose;
    @Shadow
    public ModelPart rightArm;
    @Shadow
    public ModelPart leftArm;
    @Shadow
    public ModelPart head;
    @Inject(method = "Lnet/minecraft/client/model/HumanoidModel;poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true)
    private void poseRightArmMixin(T entity, CallbackInfo ci){
        if(this.rightArmPose.equals(AnimationUtils.VOID_TOME)){
            int tick;
            if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
            else return;
            this.rightArm.xRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 4 + this.head.xRot - (float) Math.PI / 2;
            this.rightArm.zRot = Mth.cos( (float) Math.PI * tick * 9 / 180) / 4 - this.head.yRot;
            ci.cancel();
        }
    }
    @Inject(method = "Lnet/minecraft/client/model/HumanoidModel;poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true)
    private void poseLeftArmMixin(T entity, CallbackInfo ci){
        if(this.leftArmPose.equals(AnimationUtils.VOID_TOME)){
            int tick;
            if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
            else return;
            this.leftArm.xRot = -1 * Mth.cos( (float) Math.PI * tick * 9 / 180) / 4 + this.head.xRot - (float) Math.PI / 2;
            this.leftArm.zRot = Mth.cos( (float) Math.PI * tick * 9 / 180) / 4 - this.head.yRot;
            ci.cancel();
        }
    }
}
