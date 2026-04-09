// @VersionReplacement

package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.ScreenshotSettings;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {
    // @[1.21.4] "method_1664" @[1.21.11] "method_67805" @[] "lambda$grab$2"
    @Inject(method = /*[@]*/"method_1664"/*[!@]*/, at = @At("HEAD"), cancellable = true)
    private static void injectScreenshotText(File file, Style style, CallbackInfoReturnable<Style> cir) {
        if (ScreenshotSettings.INSTANCE.getRedirect().get())
            // @[1.21.4] (ClickEvent.Action.RUN_COMMAND, "/"+ @[] .RunCommand(
            cir.setReturnValue(style.withClickEvent(new ClickEvent/*[@]*/(ClickEvent.Action.RUN_COMMAND, "/"+/*[!@]*/"bewisclient screenshot "+file.getAbsolutePath())));
    }
}