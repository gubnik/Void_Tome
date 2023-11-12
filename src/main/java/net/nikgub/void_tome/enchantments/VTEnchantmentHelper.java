package net.nikgub.void_tome.enchantments;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.nikgub.void_tome.base.VTAttributes;
import net.nikgub.void_tome.base.VTDamageSource;
import net.nikgub.void_tome.base.VTDamageTypes;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.entities.VTEntities;
import net.nikgub.void_tome.entities.VoidBeamEntity;
import net.nikgub.void_tome.items.void_tome.VoidTomeItem;
import net.nikgub.void_tome.mob_effects.VTMobEffects;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class VTEnchantmentHelper {
    private static void emptyForm(LivingEntity source, ItemStack itemStack) {
        List<Meaning> meanings = Meaning.getMeaningEnchantments(itemStack);
        Vec3 traced = VTUtils.traceUntil(source, ((vec3, level) -> {
            level.addParticle(ParticleTypes.DRAGON_BREATH, vec3.x, vec3.y, vec3.z, 0, 0, 0);
        }), 10);
        DamageSource damageSource = new DamageSource(source.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(VTDamageTypes.VOID_TOME_RK), source);;
        List<? extends LivingEntity> collected = VTUtils.entityCollector(traced, 1.5f, source.level());
        for (LivingEntity target : collected) {
            target.hurt(damageSource, (float) source.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()) / 2);
            for (Meaning meaning : meanings) {
                meaning.getAttack().accept(source, target, itemStack);
            }
        }
    }
    public static void formOfRain(LivingEntity source, ItemStack itemStack){
        if(source == null) return;
        Vec3 attackCenter = new Vec3(source.getX(), source.getY(), source.getZ());
        for(int i = 0; i < 3; i ++){
            VoidBeamEntity voidBeam = new VoidBeamEntity(VTEntities.VOID_BEAM.get(), source.level(), itemStack);
            if(source instanceof Player player) voidBeam.setOwner(player);
            voidBeam.setPos(attackCenter.x + ThreadLocalRandom.current().nextDouble(-8, 8),
                    VTUtils.findGround(attackCenter, source.level()).y, attackCenter.z + ThreadLocalRandom.current().nextDouble(-8, 8));
            source.level().addFreshEntity(voidBeam);
        }
    }
    public static void formOfGlass(LivingEntity source, ItemStack itemStack){

    }
    public static void formOfNothing(LivingEntity source, ItemStack itemStack){
        List<VTEnchantmentHelper.Meaning> meanings = VTEnchantmentHelper.Meaning.getMeaningEnchantments(itemStack);
        double lx = source.getLookAngle().x;
        double ly = source.getLookAngle().y;
        double lz = source.getLookAngle().z;
        double k = 1.2f;
        DamageSource damageSource = VTDamageSource.VOID_NOTHING(source);
        source.setDeltaMovement(
                lx * k,
                ly * 0.1 * k,
                lz * k
        );
        for(LivingEntity entity : VTUtils.entityCollector(new Vec3(source.getX(), source.getY() + 1.5, source.getZ()), 1.5, source.level())){
            if(!entity.is(source)){
                for(VTEnchantmentHelper.Meaning meaning : meanings){
                    meaning.getAttack().accept(source, entity, itemStack);
                }
                entity.hurt(damageSource, (float) source.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()) / 2);
            }
        }
    }
    public enum Form{
        RAIN((VoidTomeEnchantment) VTEnchantments.FORM_OF_RAIN.get(), VTEnchantmentHelper::formOfRain, VoidTomeItem.VTClientExtension.VOID_RAIN),
        GLASS((VoidTomeEnchantment) VTEnchantments.FORM_OF_GLASS.get(), VTEnchantmentHelper::formOfGlass, VoidTomeItem.VTClientExtension.VOID_GLASS),
        NOTHING((VoidTomeEnchantment) VTEnchantments.FORM_OF_NOTHING.get(), VTEnchantmentHelper::formOfNothing, VoidTomeItem.VTClientExtension.VOID_NOTHING),
        EMPTY(null, VTEnchantmentHelper::emptyForm, VoidTomeItem.VTClientExtension.VOID_TOME);
        private final VoidTomeEnchantment vtEnchantment;
        private final HumanoidModel.ArmPose animation;
        private final BiConsumer<LivingEntity, ItemStack> attack;
        Form(VoidTomeEnchantment vtEnchantment
                , BiConsumer<LivingEntity, ItemStack> attack
                , HumanoidModel.ArmPose animation
        ){
            this.vtEnchantment = vtEnchantment;
            this.attack = attack;
            this.animation = animation;
        }

        public VoidTomeEnchantment getVtEnchantment(){
            return this.vtEnchantment;
        }
        public BiConsumer<LivingEntity, ItemStack> getAttack(){
            return this.attack;
        }
        public HumanoidModel.ArmPose getAnimation(){
            return this.animation;
        }
        public static Form getFormFromEnchantment(Enchantment enchantment){
            if(enchantment == null) return Form.EMPTY;
            for(Form form : values()){
                if(form.vtEnchantment.equals(enchantment)) return form;
            } throw new IllegalArgumentException("Invalid enchantment, must only consume Void Tome Form enchantments");
        }
        public static @Nullable VoidTomeEnchantment getFormEnchantment(ItemStack itemStack){
            for(Enchantment enchantment : itemStack.getAllEnchantments().keySet()){
                if(enchantment instanceof VoidTomeEnchantment voidTomeEnchantment && voidTomeEnchantment.getType() == VoidTomeEnchantment.Type.FORM)
                    return voidTomeEnchantment;
            } return null;
        }
    }
    public enum Meaning{
        CLEANSING((VoidTomeEnchantment) VTEnchantments.CLEANSING.get(), 1, ((source, target, itemStack) -> {
            for(MobEffectInstance effect : target.getActiveEffects()){
                if(!effect.getEffect().isBeneficial()){
                    target.getActiveEffects().remove(effect);
                    target.addEffect(new MobEffectInstance(VTMobEffects.CLEANSED.get(), 120, 0));
                }
            }
        })),
        DARKENING((VoidTomeEnchantment) VTEnchantments.DARKENING.get(), 1, ((source, target, itemStack) -> {
            double posX, posY, posZ;
            Vec3 cPos;
            List<? extends LivingEntity> collector;
            posX = source.getX();
            posY = source.getY() + 0.3f;
            posZ = source.getZ();
            DamageSource damageSource = VTDamageSource.VOID_TOME(source);
            for(int i = 0; i < 60; i++){
                for(int j = 0; j < 5; j++) {
                    cPos = new Vec3(
                            posX - Mth.sin((float)(Math.toRadians(source.getYRot()) + Math.toRadians(i * 6))) * j,
                            posY,
                            posZ + Mth.cos((float)(Math.toRadians(source.getYRot()) + Math.toRadians(i * 6))) * j
                    );
                    if(source.level() instanceof ServerLevel level){
                        level.sendParticles(ParticleTypes.SMOKE, cPos.x, cPos.y, cPos.z, 1, 0, 0, 0, 0);
                    }
                    collector = VTUtils.entityCollector(cPos, 0.3, source.level());
                    for(LivingEntity entity : collector){
                        if(entity.equals(source)) continue;
                        entity.hurt(damageSource, (float) source.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()) / 3);
                        entity.setDeltaMovement(new Vec3(
                                -Mth.sin((float)(Math.toRadians(source.getYRot()) + Math.toRadians(i * 4))) * j / 2,
                                0.2f,
                                Mth.cos((float)(Math.toRadians(source.getYRot()) + Math.toRadians(i * 4))) * j/ 2
                        ));
                    }
                }
            }
        })),
        DEVOURING((VoidTomeEnchantment) VTEnchantments.DEVOURING.get(), 1, ((source, target, itemStack) -> {
            if(target.isDeadOrDying()) return;
            double dx, dy, dz;
            double[] VALUES;
            double MAX;
            Vec3 direction;
            double cx = target.getX();
            double cy = target.getY() + 1.5;
            double cz = target.getZ();
            for(LivingEntity entity : VTUtils.entityCollector(new Vec3(cx, cy, cz), 4, source.level())){
                dx = target.getX() - entity.getX();
                dy = target.getY() - entity.getY();
                dz = target.getZ() - entity.getZ();
                VALUES = new double[]{Mth.abs((float)dx), Mth.abs((float)dy), Mth.abs((float)dz)};
                MAX = Arrays.stream(VALUES).max().getAsDouble();
                dx = dx / MAX;
                dy = dy / MAX;
                dz = dz / MAX;
                direction = new Vec3(dx*1.5, dy*1.5, dz*1.5);
                entity.setDeltaMovement(direction);
            }
        })),

        EMPTY(null, 1, ((source, target, itemStack) -> {}));
        private final VoidTomeEnchantment vtEnchantment;
        private final double damageModifier;
        private final TriConsumer<LivingEntity, LivingEntity, ItemStack> attack;
        Meaning(VoidTomeEnchantment vtEnchantment,
                double damageModifier,
                TriConsumer<LivingEntity, LivingEntity, ItemStack> attack // source, target, itemstack
        ){
            this.vtEnchantment = vtEnchantment;
            this.damageModifier = damageModifier;
            this.attack = attack;
        }
        public VoidTomeEnchantment getVtEnchantment(){
            return this.vtEnchantment;
        }
        public double getDamageModifier(){
            return this.damageModifier;
        }
        public TriConsumer<LivingEntity, LivingEntity, ItemStack> getAttack(){
            return this.attack;
        }
        public static Meaning getMeaningFromEnchantment(Enchantment enchantment){
            for(Meaning meaning : values()){
                if(meaning.vtEnchantment.equals(enchantment)) return meaning;
            } throw new IllegalArgumentException("Invalid enchantment, must only consume Void Tome Meaning enchantments");
        }
        public static @NotNull List<Meaning> getMeaningEnchantments(ItemStack itemStack){
            List<Meaning> list = new ArrayList<>();
            for(Enchantment enchantment : itemStack.getAllEnchantments().keySet()){
                if(enchantment instanceof VoidTomeEnchantment voidTomeEnchantment && voidTomeEnchantment.getType() == VoidTomeEnchantment.Type.MEANING)
                    list.add(getMeaningFromEnchantment(voidTomeEnchantment));
            } return list;
        }
    }
}
