package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.functionalities.Fullbright.getNightVisionInstance
import net.bewis09.bewisclient.impl.functionalities.Fullbright.hasNightVision
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(LivingEntity::class)
class NightVisionInjectorMixin {
    @Inject(method = ["getEffect"], at = [At("RETURN")], cancellable = true)
    private fun injectNightVision(effect: Holder<MobEffect>, cir: CallbackInfoReturnable<MobEffectInstance?>) {
        if (effect.value() === MobEffects.NIGHT_VISION.value() && cir.getReturnValue() == null) cir.setReturnValue(getNightVisionInstance())
    }

    @Inject(method = ["hasEffect"], at = [At("RETURN")], cancellable = true)
    private fun injectHasNightVision(effect: Holder<MobEffect>, cir: CallbackInfoReturnable<Boolean?>) {
        if (effect.value() === MobEffects.NIGHT_VISION.value() && cir.getReturnValue() == false) {
            cir.setReturnValue(hasNightVision())
        }
    }
}