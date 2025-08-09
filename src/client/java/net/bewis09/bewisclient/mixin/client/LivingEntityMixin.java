package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.fullbright.Fullbright;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getStatusEffect", at = @At("RETURN"), cancellable = true)
    private void injectNightVision(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if (effect.value() == StatusEffects.NIGHT_VISION.value() && cir.getReturnValue() == null) cir.setReturnValue(Fullbright.INSTANCE.getNightVisionInstance());
    }

    @Inject(method = "hasStatusEffect", at = @At("RETURN"), cancellable = true)
    private void injectHasNightVision(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect.value() == StatusEffects.NIGHT_VISION.value() && cir.getReturnValue() == false) {
            cir.setReturnValue(Fullbright.INSTANCE.hasNightVision());
        }
    }
}