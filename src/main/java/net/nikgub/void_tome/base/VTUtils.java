package net.nikgub.void_tome.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class VTUtils {
    public static boolean hasCompletedTheAdvancement(ServerPlayer serverPlayer, ResourceLocation advancement){
        return serverPlayer.getAdvancements()
                .getOrStartProgress(Objects.requireNonNull(
                        serverPlayer.server.getAdvancements().getAdvancement(advancement)
                ))
                .isDone();
    }
    public static int rgbToColorInteger(int red, int green, int blue){
        int[] VALUES = {red, green, blue};
        int c = Math.max(Arrays.stream(VALUES).max().getAsInt(), 255);
        for(int value : VALUES){
            value = (value/c) * 255;
        }
        return 65536 * red + 256 * green + blue;
    }
    public static Vec3 findGround(Vec3 pos, Level level){
        while (pos.y > -64 && !level.getBlockState(new BlockPos(new Vec3i((int) pos.x, (int) pos.y, (int) pos.z))).canOcclude()){
            pos = new Vec3(pos.x, pos.y - 1, pos.z);
        }
        return pos;
    }
    public static void shootProjectile(Projectile projectile, LivingEntity shooter, float speed, float inaccuracy){
        assert shooter != null;
        projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.2, shooter.getZ());
        projectile.shoot(shooter.getLookAngle().x, shooter.getLookAngle().y, shooter.getLookAngle().z, speed, inaccuracy);
        shooter.level().addFreshEntity(projectile);
    }
    public static List<? extends LivingEntity> entityCollector(Vec3 center, double radius, Level level){
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(radius), e -> true).stream().sorted(Comparator.comparingDouble(
                entityFound -> entityFound.distanceToSqr(center))).toList();
    }
    public static Vec3 traceUntil(LivingEntity livingEntity){
        Vec3 lookPos;
        Vec3 initLook = livingEntity.getLookAngle();
        double i = 1;
        while(entityCollector(lookPos = new Vec3(initLook.x * i, initLook.y * i, initLook.z * i), 0.1, livingEntity.level()).isEmpty()
                && !livingEntity.level().getBlockState(new BlockPos(new Vec3i((int) lookPos.x, (int) lookPos.y, (int) lookPos.z))).canOcclude()){
            i += 0.2;
            if(i >= 1000) return lookPos;
        }
        return lookPos;
    }
    public static Vec3 traceUntil(LivingEntity livingEntity, double limit){
        Vec3 lookPos;
        Vec3 initLook = livingEntity.getLookAngle();
        double i = 1;
        while(entityCollector(lookPos = new Vec3(initLook.x * i, initLook.y * i, initLook.z * i), 0.1, livingEntity.level()).isEmpty()
                && !livingEntity.level().getBlockState(new BlockPos(new Vec3i((int) lookPos.x, (int) lookPos.y, (int) lookPos.z))).canOcclude()
                && i < limit){
            i += 0.2;
        }
        return lookPos;
    }
    public static Vec3 traceUntil(LivingEntity livingEntity, BiConsumer<Vec3, Level> action, double limit){
        Vec3 lookPos;
        Vec3 initLook = livingEntity.getLookAngle();
        double i = 1.2;
        while(entityCollector(
                lookPos = new Vec3( livingEntity.getX() + initLook.x * i, livingEntity.getY() + 1.5 + initLook.y * i, livingEntity.getZ() + initLook.z * i)
                , 0.1, livingEntity.level()).isEmpty() &&
                !livingEntity.level().getBlockState(new BlockPos(new Vec3i((int) lookPos.x, (int) lookPos.y, (int) lookPos.z))).canOcclude()
                && i < limit){
            action.accept(lookPos, livingEntity.level());
            i += 0.2;
        }
        return lookPos;
    }
}
