package net.nikgub.void_tome.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.nikgub.void_tome.VoidTomeMod;
import net.nikgub.void_tome.entities.VoidBeamEntity;
import net.nikgub.void_tome.entities.models.VoidBeamModel;
import org.jetbrains.annotations.NotNull;

public class VoidBeamRenderer extends EntityRenderer<VoidBeamEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(VoidTomeMod.MOD_ID, "textures/entity/void_beam.png");
    private final VoidBeamModel<VoidBeamEntity> model;
    public VoidBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new VoidBeamModel<>(context.bakeLayer(VoidBeamModel.LAYER_LOCATION));
    }
    public void render(@NotNull VoidBeamEntity entity, float p_114529_, float p_114530_, @NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int p_114533_){
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION));
        int tick = entity.tickCount;
        float scale = entity.getSize();
        float phase = (5 - Mth.abs(tick - 5));
        float mx = 0.2f * phase;
        float mz = 0.2f * phase;
        poseStack.scale(
                mx * scale,
                32,
                mz * scale
        );
        this.model.renderToBuffer(poseStack, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, mx, mx, 1, 0.2F);
        super.render(entity, p_114529_, p_114530_, poseStack, multiBufferSource, p_114533_);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull VoidBeamEntity voidBeamEntity) {
        return TEXTURE_LOCATION;
    }
}
