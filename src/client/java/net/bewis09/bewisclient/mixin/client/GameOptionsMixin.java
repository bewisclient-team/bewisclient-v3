package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings;
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "getMenuBackgroundBlurrinessValue", at = @At("RETURN"), cancellable = true)
    private void bewisclient$getMenuBackgroundBlurrinessValue(CallbackInfoReturnable<Integer> cir) {
        if (MinecraftClient.getInstance().currentScreen instanceof RenderableScreen renderableScreen) {
            if (!OptionsMenuSettings.INSTANCE.getBlurBackground().get()) cir.setReturnValue(0);
            if (renderableScreen.getRenderable() instanceof BackgroundEffectProvider p) {
                cir.setReturnValue((int) (p.getBackgroundEffectFactor() * cir.getReturnValue()));
            }
        }
    }
}
