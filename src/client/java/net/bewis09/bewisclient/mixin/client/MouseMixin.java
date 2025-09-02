package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.zoom.Zoom;
import net.bewis09.bewisclient.impl.widget.CPSWidget;
import net.minecraft.class_11910;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void bewisclient$onMouseButton(long window, class_11910 arg, int action, CallbackInfo ci) {
        if (action != 1) return;

        if (arg.button() == 0) CPSWidget.INSTANCE.getLeftMouseList().add(System.currentTimeMillis());
        if (arg.button() == 1) CPSWidget.INSTANCE.getRightMouseList().add(System.currentTimeMillis());
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void bewisclient$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (Zoom.INSTANCE.isUsed()) {
            Zoom.INSTANCE.getFactorAnimation().set("factor", MathHelper.clamp(Zoom.INSTANCE.getFactorAnimation().getWithoutInterpolation("factor") - (float) vertical * 0.02f, .009f, .4f));

            ci.cancel();
        }
    }
}
