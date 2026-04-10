// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.functionalities.Zoom.factorAnimation
import net.bewis09.bewisclient.impl.functionalities.Zoom.isUsed
import net.bewis09.bewisclient.impl.widget.CPSWidget.leftMouseList
import net.bewis09.bewisclient.impl.widget.CPSWidget.rightMouseList
import net.bewis09.bewisclient.util.MathHelper.clamp
import net.minecraft.client.MouseHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MouseHandler::class)
class MouseMixin {
    @Inject(method = ["onButton"], at = [At("HEAD")])
    // @[1.21.8] button: Int, action: Int, modifiers: Int @[] rawButtonInfo: net.minecraft.client.input.MouseButtonInfo, action: Int
    private fun bewisclientOnMouseButton(handle: Long, /*[@]*/rawButtonInfo: net.minecraft.client.input.MouseButtonInfo, action: Int/*[!@]*/, ci: CallbackInfo?) {
        if (action != 1) return

        // @[1.21.8] button @[] rawButtonInfo.button()
        when(/*[@]*/rawButtonInfo.button()/*[!@]*/) {
            0 -> leftMouseList.add(System.currentTimeMillis())
            1 -> rightMouseList.add(System.currentTimeMillis())
        }
    }

    @Inject(method = ["onScroll"], at = [At("HEAD")], cancellable = true)
    private fun bewisclientOnMouseScroll(handle: Long, horizontal: Double, vertical: Double, ci: CallbackInfo) {
        if (isUsed()) {
            factorAnimation.set(clamp(factorAnimation.getWithoutInterpolation() - vertical.toFloat() * 0.02f, .009f, .4f))

            ci.cancel()
        }
    }
}