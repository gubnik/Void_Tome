package net.nikgub.void_tome.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    public void renderModelLists(BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_){}
    @Shadow
    private ItemColors itemColors;
    @Inject(method = "render",
            at = @At("HEAD"), cancellable = true)
    private void renderCustomizer(ItemStack itemStack, ItemDisplayContext transformType, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo callbackInfo){
        if(itemStack.getItem() == Items.DIAMOND_SWORD && itemStack.getOrCreateTag().getBoolean("VTRender")){
            bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, bakedModel, transformType, b);
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            for(RenderType renderType : bakedModel.getRenderTypes(itemStack, true)) {
                VertexConsumer vertex = multiBufferSource.getBuffer(renderType);
                this.renderModelLists(bakedModel, itemStack, i, j, poseStack, vertex);
            }
            callbackInfo.cancel();
        }
    }
    @Inject(method = "renderQuadList",
            at = @At("HEAD"), cancellable = true)
    public void renderQuadList(PoseStack poseStack, VertexConsumer vertexConsumer, List<BakedQuad> bakedQuads, ItemStack itemStack, int p_115167_, int p_115168_, CallbackInfo callbackInfo){
        if(itemStack.getItem() == Items.GOLDEN_SWORD && itemStack.getOrCreateTag().getBoolean("VTRender")) {
            boolean flag = !itemStack.isEmpty();
            PoseStack.Pose posestack$pose = poseStack.last();

            for (BakedQuad bakedquad : bakedQuads) {
                float f = 0.25f;
                float f1 = 0.0f;
                float f2 = 1.0f;
                vertexConsumer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 0.5F, p_115167_, p_115168_, false);
            }
            callbackInfo.cancel();
        }
    }
}
