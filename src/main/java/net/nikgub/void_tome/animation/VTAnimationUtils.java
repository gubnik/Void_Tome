package net.nikgub.void_tome.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class VTAnimationUtils {
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
}