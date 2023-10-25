package net.nikgub.void_tome;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nikgub.void_tome.items.ModItems;
import org.slf4j.Logger;
@Mod(VoidTomeMod.MOD_ID)
public class VoidTomeMod
{
    public static final String MOD_ID = "void_tome";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VoidTomeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VoidTomeConfig.COMMON_SPEC, "void_tome/common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VoidTomeConfig.CLIENT_SPEC, "void_tome/client.toml");

        modEventBus.addListener(this::commonSetup);
        ModItems.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
