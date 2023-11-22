package net.nikgub.void_tome.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import org.jetbrains.annotations.NotNull;

public class VoidTomeLayer <T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public static float INFLATION = 1.15F;
    public static ResourceLocation VOID_LAYER = new ResourceLocation("minecraft:textures/entity/player/wide/steve.png");

    public VoidTomeLayer(RenderLayerParent<T, M> owner) {
        super(owner);
    }
    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int p_117351_, @NotNull T entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if (entity instanceof Player player && player.getUseItem().getItem() instanceof VoidTomeItem) {
            poseStack.scale(INFLATION, INFLATION, INFLATION);
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(
                    RenderType.entityTranslucentEmissive(VOID_LAYER)
            );
            this.getParentModel().renderToBuffer(poseStack, vertexconsumer, VTUtils.rgbToColorInteger(64, 0, 256), OverlayTexture.NO_OVERLAY,
                    0.25F, // color
                    0F, // color
                    1F, // color
                    0.4F);
        }
    }
}
