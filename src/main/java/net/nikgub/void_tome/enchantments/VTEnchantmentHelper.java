package net.nikgub.void_tome.enchantments;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.nikgub.void_tome.animation.VTAnimationUtils;
import net.nikgub.void_tome.base.VTAttributes;
import net.nikgub.void_tome.base.VTDamageSource;
import net.nikgub.void_tome.base.VTUtils;
import net.nikgub.void_tome.entities.VTEntities;
import net.nikgub.void_tome.entities.VoidBeamEntity;
import net.nikgub.void_tome.entities.VoidTomeDefaultProjectile;
import net.nikgub.void_tome.mob_effects.VTMobEffects;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class VTEnchantmentHelper {
    public static void formOfRain(LivingEntity source, ItemStack itemStack){
        if(source == null) return;
        Vec3 attackCenter = new Vec3(source.getX(), source.getY(), source.getZ());
        for(int i = 0; i < 3; i ++){
            VoidBeamEntity voidBeam = new VoidBeamEntity(VTEntities.VOID_BEAM.get(), source.level, itemStack);
            if(source instanceof Player player) voidBeam.setOwner(player);
            voidBeam.setPos(attackCenter.x + ThreadLocalRandom.current().nextDouble(-8, 8),
                    VTUtils.findGround(attackCenter, source.level).y, attackCenter.z + ThreadLocalRandom.current().nextDouble(-8, 8));
            source.level.addFreshEntity(voidBeam);
        }
    }
    public static void formOfGlass(LivingEntity source, ItemStack itemStack){

    }
    public static void formOfNothing(LivingEntity source, ItemStack itemStack){
        double lx = source.getLookAngle().x;
        double ly = source.getLookAngle().y;
        double lz = source.getLookAngle().z;
        double k = 1.2f;
        source.setDeltaMovement(
                lx * k,
                ly * 0.1 * k,
                lz * k
        );
        for(LivingEntity entity : VTUtils.entityCollector(new Vec3(source.getX(), source.getY() + 1.5, source.getZ()), 1.5, source.level)){
            if(!entity.is(source)){
                entity.hurt(VTDamageSource.VOID_NOTHING(source), (float) source.getAttributeValue(VTAttributes.VOID_TOME_DAMAGE.get()) / 2);
            }
        }
    }
    public enum Form{
        RAIN((VoidTomeEnchantment) VTEnchantments.FORM_OF_RAIN.get(), VTEnchantmentHelper::formOfRain, VTAnimationUtils.VOID_RAIN),
        GLASS((VoidTomeEnchantment) VTEnchantments.FORM_OF_GLASS.get(), VTEnchantmentHelper::formOfGlass, VTAnimationUtils.VOID_GLASS),
        WAY((VoidTomeEnchantment) VTEnchantments.FORM_OF_WAY.get(), ((livingEntity, itemStack) -> {}), VTAnimationUtils.VOID_TOME),
        NOTHING((VoidTomeEnchantment) VTEnchantments.FORM_OF_NOTHING.get(), VTEnchantmentHelper::formOfNothing, VTAnimationUtils.VOID_NOTHING),
        EMPTY(null, (livingEntity, itemStack) -> {
            VoidTomeDefaultProjectile projectile = new VoidTomeDefaultProjectile(VTEntities.VT_DEFAULT.get(), livingEntity.level, itemStack);
            VTUtils.shootProjectile(projectile, livingEntity, 0.4f, 0.2f);
        }, HumanoidModel.ArmPose.EMPTY);
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
            boolean flag;
            for(MobEffectInstance effect : source.getActiveEffects()){
                if(!effect.getEffect().isBeneficial()){
                    source.getActiveEffects().clear();
                    source.addEffect(new MobEffectInstance(VTMobEffects.CLEANSED.get(), 120, 0));
                }
            }
        })),
        DARKENING((VoidTomeEnchantment) VTEnchantments.DARKENING.get(), 1, ((source, target, itemStack) -> {})),
        DEVOURING((VoidTomeEnchantment) VTEnchantments.DEVOURING.get(), 1, ((source, target, itemStack) -> {})),
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
