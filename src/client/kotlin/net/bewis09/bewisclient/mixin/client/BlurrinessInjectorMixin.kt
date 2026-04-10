package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings.blurBackground
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.version.getScreen
import net.minecraft.client.Options
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Options::class)
class BlurrinessInjectorMixin {
    @Inject(method = ["getMenuBackgroundBlurriness"], at = [At("RETURN")], cancellable = true)
    private fun bewisclientGetMenuBackgroundBlurrinessValue(cir: CallbackInfoReturnable<Int>) {
        val renderableScreen = getScreen() as? RenderableScreen ?: return

        if (!blurBackground.get()) cir.returnValue = 0

        val provider = renderableScreen.renderable as? BackgroundEffectProvider ?: return

        cir.returnValue = (provider.getBackgroundEffectFactor() * cir.returnValue).toInt()
    }
}