package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.widget.CPSWidget;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at=@At("HEAD"))
    private void bewisclient$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action != 1) return;

        if (button == 0) CPSWidget.INSTANCE.getLeftMouseList().add(System.currentTimeMillis());
        if (button == 1) CPSWidget.INSTANCE.getRightMouseList().add(System.currentTimeMillis());
    }
}
