package net.nikgub.void_tome;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nikgub.void_tome.base.VTAttributes;
import net.nikgub.void_tome.enchantments.VTEnchantmentHelper;
import net.nikgub.void_tome.enchantments.VTEnchantments;
import net.nikgub.void_tome.entities.VTEntities;
import net.nikgub.void_tome.entities.models.VoidBeamModel;
import net.nikgub.void_tome.entities.renderers.VoidBeamRenderer;
import net.nikgub.void_tome.entities.renderers.VoidTomeLayer;
import net.nikgub.void_tome.items.VTItems;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import net.nikgub.void_tome.mob_effects.VTMobEffects;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@SuppressWarnings("unused")
@Mod(VoidTomeMod.MOD_ID)
public class VoidTomeMod {
    public static final String MOD_ID = "void_tome";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VoidTomeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VoidTomeConfig.COMMON_SPEC, "void_tome/common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VoidTomeConfig.CLIENT_SPEC, "void_tome/client.toml");

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerLayerDefinitions);
        VTItems.ITEMS.register(modEventBus);
        VTEnchantments.ENCHANTMENTS.register(modEventBus);
        VTEntities.ENTITIES.register(modEventBus);
        VTAttributes.ATTRIBUTES.register(modEventBus);
        VTMobEffects.EFFECTS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRenderers.register(VTEntities.VOID_BEAM.get(), VoidBeamRenderer::new);
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(VoidBeamModel.LAYER_LOCATION, VoidBeamModel::createBodyLayer);
    }
    @SubscribeEvent
    public void livingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player
                && player.getUseItem().getItem() instanceof VoidTomeItem) {
            event.setAmount(event.getAmount() * 2);
        }
        if (event.getSource().getEntity() instanceof Player player
        && player.getMainHandItem().getItem() instanceof VoidTomeItem
        && VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(player.getMainHandItem())) == VTEnchantmentHelper.Form.GLASS) {
            player.level.playSound(player,  new BlockPos(new Vec3(player.getX(), player.getY(), player.getZ())), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1f, 0.4f);
            event.setAmount((float) player.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()));
            for(VTEnchantmentHelper.Meaning meaning : VTEnchantmentHelper.Meaning.getMeaningEnchantments(player.getMainHandItem())){
                meaning.getAttack().accept(player, event.getEntity(), player.getMainHandItem());
            }
        }

    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void entityAttributeProvider(EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, VTAttributes.VOID_TOME_DAMAGE.get());
        }
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
        public static ModelLayerLocation VOID_TOME_LAYER = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "void_tome");
        @SubscribeEvent
        public static void playerLayerEvent(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(VOID_TOME_LAYER, () -> LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 64, 64));
        }
        @SubscribeEvent
        public static void layerConstructor(EntityRenderersEvent.AddLayers event){

            addLayerToPlayerSkin(event, "default", VoidTomeLayer::new);
            addLayerToPlayerSkin(event, "slim", VoidTomeLayer::new);
        }

        @SuppressWarnings({"rawtypes", "unchecked", "unused"})
        private static <E extends Player, M extends HumanoidModel<E>>
        void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory)
        {
            LivingEntityRenderer renderer = event.getSkin(skinName);
            if (renderer != null) renderer.addLayer(factory.apply(renderer));
        }
        private static <E extends LivingEntity, M extends HumanoidModel<E>>
        void addLayerToHumanoid(EntityRenderersEvent.AddLayers event, EntityType<E> entityType, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory)
        {
            LivingEntityRenderer<E, M> renderer = event.getRenderer(entityType);
            if (renderer != null) renderer.addLayer(factory.apply(renderer));
        }
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void playerRenderEvent(RenderPlayerEvent event){
            Player player = event.getEntity();
            if(player.isUsingItem() && player.getUseItem().getItem() instanceof VoidTomeItem voidTomeItem &&
            VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(player.getUseItem())) == VTEnchantmentHelper.Form.NOTHING) {
                player.level.addParticle(ParticleTypes.SMOKE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
                player.level.addParticle(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
                player.level.addParticle(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
                event.setCanceled(event.isCancelable());
            }
        }
        @SubscribeEvent
        public static void tooltipLineEvent(ItemTooltipEvent event) {
            if(!(event.getItemStack().getItem() instanceof VoidTomeItem voidTomeItem)) return;
            List<Component> forModification = List.copyOf(event.getToolTip());
            List<Component> forDeletion = new ArrayList<>();
            for (Component component : forModification) {
                if (component.getString().contains("Void Tome Damage") && component.getString().contains("+")) {
                    forDeletion.add(component);
                    event.getToolTip().add(Component.literal(" ")
                            .append(Component.translatable("attribute.modifier.equals." + 0,
                                    ATTRIBUTE_MODIFIER_FORMAT.format(VoidTomeItem.DAMAGE),
                                    Component.translatable("attribute.generic.void_tome_damage"))
                            ).withStyle(ChatFormatting.DARK_PURPLE));
                }
            }
            for(Component component : forDeletion) event.getToolTip().remove(component);
        }
        // Credit: BobMowzie
        @SubscribeEvent
        public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;
            float delta = Minecraft.getInstance().getFrameTime();
            float ticksExistedDelta = player.tickCount + delta;
            float intensity = VoidTomeConfig.CLIENT.screenShakeAmount.get().floatValue();
            if (!Minecraft.getInstance().isPaused() && player.level.isClientSide()
            && ((player.getUseItemRemainingTicks() > 0 && player.getMainHandItem().equals(player.getUseItem()) && player.getMainHandItem().getItem() instanceof VoidTomeItem)
            || (player.swinging && player.getMainHandItem().getItem() instanceof VoidTomeItem
            && VTEnchantmentHelper.Form.getFormFromEnchantment(VTEnchantmentHelper.Form.getFormEnchantment(player.getMainHandItem())) == VTEnchantmentHelper.Form.GLASS))) {
                event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }
}
