package net.nikgub.void_tome.items.void_tome;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.nikgub.void_tome.VoidTomeConfig;
import net.nikgub.void_tome.base.AnimationUtils;
import org.jetbrains.annotations.NotNull;

public class VoidTomeItem extends Item implements IClientItemExtensions {
    public VoidTomeItem(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand){
        player.startUsingItem(hand);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    public int getUseDuration(@NotNull ItemStack itemStack){
        return VoidTomeConfig.COMMON.maxUse.get();
    }
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack){
        return UseAnim.CUSTOM;
    }
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack)
    {
        return AnimationUtils.VOID_TOME;
    }

}
