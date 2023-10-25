package net.nikgub.void_tome.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ModUtils {
    public static boolean hasCompletedTheAdvancement(ServerPlayer serverPlayer, ResourceLocation advancement){
        return serverPlayer.getAdvancements()
                .getOrStartProgress(Objects.requireNonNull(
                        serverPlayer.server.getAdvancements().getAdvancement(advancement)
                ))
                .isDone();
    }
}
