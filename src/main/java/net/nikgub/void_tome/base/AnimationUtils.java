package net.nikgub.void_tome.base;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.IArmPoseTransformer;

public class AnimationUtils {
    public static HumanoidModel.ArmPose VOID_TOME = HumanoidModel.ArmPose.create("void_tome", false, (model, entity, arm) -> {
        int tick;
        if(entity instanceof Player player) tick = player.getUseItemRemainingTicks();
        else return;
        ModelPart armPart = (arm.equals(HumanoidArm.RIGHT)) ? model.rightArm : model.leftArm;
        //armPart.xRot = armPart.xRot * 0.5F - (float) Math.PI;
        armPart.xRot = armPart.xRot * Mth.cos( (float) Math.PI * tick / 180);
        armPart.zRot = armPart.zRot * Mth.sin( (float) Math.PI * tick / 180);
    });
}
