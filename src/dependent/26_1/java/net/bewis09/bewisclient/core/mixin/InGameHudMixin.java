package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing;
import net.bewis09.bewisclient.impl.functionalities.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings;
import net.bewis09.bewisclient.util.logic.BewisclientInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract class InGameHudMixin {
    @Shadow
    public abstract Font getFont();

    @Shadow
    private int toolHighlightTimer;

    @Shadow
    private net.minecraft.world.item.ItemStack lastToolHighlight;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "extractSelectedItemName", at = @At("HEAD"), cancellable = true)
    private void bewisclient$renderHeldItemTooltip(GuiGraphicsExtractor drawContext, CallbackInfo ci) {
        if (HeldItemTooltipSettings.INSTANCE.isEnabled()) {
            HeldItemTooltip.INSTANCE.render(new ScreenDrawing(drawContext, getFont()), toolHighlightTimer, lastToolHighlight);
            ci.cancel();
        }
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"))
    private void bewisclient$renderScoreboardSidebar(GuiGraphicsExtractor guiGraphics, Objective objective, CallbackInfo ci) {
        float scale = ScoreboardSettings.INSTANCE.isEnabled() ? ScoreboardSettings.INSTANCE.getScale().get() : 1.0f;

        ScreenDrawing screenDrawing = new ScreenDrawing(guiGraphics, minecraft.font);

        screenDrawing.push();
        screenDrawing.scale(scale, scale);
        screenDrawing.translate((float) (-minecraft.getWindow().getGuiScaledWidth()) * (1.0f - 1 / scale), (float) (-minecraft.getWindow().getGuiScaledHeight()) * (1.0f - 1 / scale) / 2.0f);
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("RETURN"))
    private void bewisclient$renderScoreboardSidebarReturn(GuiGraphicsExtractor guiGraphics, Objective objective, CallbackInfo ci) {
        new ScreenDrawing(guiGraphics, minecraft.font).pop();
    }
}