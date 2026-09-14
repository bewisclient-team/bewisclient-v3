// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.features.utilities.Zoom.factorAnimation
import net.bewis09.bewisclient.features.utilities.Zoom.isUsed
import net.bewis09.bewisclient.widget.impl.CPSWidget.leftMouseList
import net.bewis09.bewisclient.widget.impl.CPSWidget.rightMouseList
import net.minecraft.client.MouseHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MouseHandler::class)
class MouseMixin {
    // @[1.21.8] "onPress" @[] "onButton"
    @Inject(method = [/*[@]*/"onButton"/*[!@]*/], at = [At("HEAD")])
    // @[1.21.8] button: Int, action: Int, modifiers: Int @[] rawButtonInfo: net.minecraft.client.input.MouseButtonInfo, action: Int
    private fun bewisclientOnMouseButton(handle: Long, /*[@]*/rawButtonInfo: net.minecraft.client.input.MouseButtonInfo, action: Int/*[!@]*/, ci: CallbackInfo?) {
        if (action != 1) return

        // @[1.21.8] button @[] rawButtonInfo.button()
        when (/*[@]*/rawButtonInfo.button()/*[!@]*/) {
            InputConstants.MOUSE_BUTTON_LEFT -> leftMouseList.add(System.currentTimeMillis())
            InputConstants.MOUSE_BUTTON_RIGHT -> rightMouseList.add(System.currentTimeMillis())
        }
    }

    @Inject(method = ["onScroll"], at = [At("HEAD")], cancellable = true)
    private fun bewisclientOnMouseScroll(handle: Long, horizontal: Double, vertical: Double, ci: CallbackInfo) {
        if (isUsed()) {
            factorAnimation.set((factorAnimation.getWithoutInterpolation() - vertical.toFloat() * 0.02f).coerceIn(.009f, .4f))

            ci.cancel()
        }
    }
}