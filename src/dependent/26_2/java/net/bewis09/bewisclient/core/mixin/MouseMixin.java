package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.functionalities.Zoom;
import net.bewis09.bewisclient.impl.widget.CPSWidget;
import net.bewis09.bewisclient.util.MathHelper;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void bewisclient$onMouseButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        if (action != 1) return;

        if (rawButtonInfo.button() == 0) CPSWidget.INSTANCE.getLeftMouseList().add(System.currentTimeMillis());
        if (rawButtonInfo.button() == 1) CPSWidget.INSTANCE.getRightMouseList().add(System.currentTimeMillis());
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void bewisclient$onMouseScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        if (Zoom.INSTANCE.isUsed()) {
            Zoom.INSTANCE.getFactorAnimation().set(MathHelper.INSTANCE.clamp(Zoom.INSTANCE.getFactorAnimation().getWithoutInterpolation() - (float) yoffset * 0.02f, .009f, .4f));

            ci.cancel();
        }
    }
}
