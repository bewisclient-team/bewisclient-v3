package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing;
import net.bewis09.bewisclient.impl.functionalities.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings;
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void inject(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float f, CallbackInfo ci) {
        if (PumpkinOverlaySettings.INSTANCE.getEnabled().get() && resourceLocation.toString().equals("minecraft:textures/misc/pumpkinblur.png")) {
            ci.cancel();
        }
    }
}