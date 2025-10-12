package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.ScreenshotSettings;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    @Inject(method = "method_1664", at = @At("HEAD"), cancellable = true)
    private static void injectScreenshotText(File file, Style style, CallbackInfoReturnable<Style> cir) {
        if (ScreenshotSettings.INSTANCE.getRedirect().get())
            cir.setReturnValue(style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bewisclient screenshot "+file.getAbsolutePath())));
    }
}
