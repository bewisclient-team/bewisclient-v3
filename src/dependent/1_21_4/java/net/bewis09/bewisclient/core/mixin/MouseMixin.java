package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.functionalities.Zoom;
import net.bewis09.bewisclient.impl.widget.CPSWidget;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void bewisclient$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action != 1) return;

        if (button == 0) CPSWidget.INSTANCE.getLeftMouseList().add(System.currentTimeMillis());
        if (button == 1) CPSWidget.INSTANCE.getRightMouseList().add(System.currentTimeMillis());
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void bewisclient$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (Zoom.INSTANCE.isUsed()) {
            Zoom.INSTANCE.getFactorAnimation().set(MathHelper.clamp(Zoom.INSTANCE.getFactorAnimation().getWithoutInterpolation() - (float) vertical * 0.02f, .009f, .4f));

            ci.cancel();
        }
    }
}
