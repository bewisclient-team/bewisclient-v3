package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.held_item_info.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
abstract
class InGameHudMixin {
    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int heldItemTooltipFade;

    @Shadow private ItemStack currentStack;

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void bewisclient$renderHeldItemTooltip(DrawContext drawContext, CallbackInfo ci) {
        if (HeldItemTooltipSettings.INSTANCE.getEnabled().get()) {
            HeldItemTooltip.INSTANCE.render(drawContext, getTextRenderer(), heldItemTooltipFade, currentStack);
            ci.cancel();
        }
    }
}