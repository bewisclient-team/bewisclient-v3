package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.Fullbright;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getEffect", at = @At("RETURN"), cancellable = true)
    private void injectNightVision(Holder<@NotNull MobEffect> effect, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (effect.value() == MobEffects.NIGHT_VISION.value() && cir.getReturnValue() == null) cir.setReturnValue(Fullbright.INSTANCE.getNightVisionInstance());
    }

    @Inject(method = "hasEffect", at = @At("RETURN"), cancellable = true)
    private void injectHasNightVision(Holder<@NotNull MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect.value() == MobEffects.NIGHT_VISION.value() && cir.getReturnValue() == false) {
            cir.setReturnValue(Fullbright.INSTANCE.hasNightVision());
        }
    }
}