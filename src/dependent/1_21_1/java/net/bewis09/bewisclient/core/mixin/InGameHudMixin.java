package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    public void inject(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (PumpkinOverlaySettings.INSTANCE.getEnabled().get() && texture.toString().equals("minecraft:textures/misc/pumpkinblur.png")) {
            ci.cancel();
        }
    }
}
