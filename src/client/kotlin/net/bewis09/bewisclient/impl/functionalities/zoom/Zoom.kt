package net.bewis09.bewisclient.impl.functionalities.zoom

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.ZoomSettings
import org.lwjgl.glfw.GLFW

object Zoom : ImageSettingCategory(
    "zoom", Translation("menu.category.zoom", "Zoom"), arrayOf(
        ZoomSettings.smooth.createRenderable("zoom.smooth", "Smooth Zoom", "Enable or disable smooth zoom (Works as if smooth camera is enabled)"), ZoomSettings.instant.createRenderable("zoom.instant", "Instant Zoom", "Disables the transition animation when zooming in or out")
    ), ZoomSettings.enabled
) {
    var smoothCameraEnabledBefore: Boolean? = null

    val ZoomKeybind = Keybind(GLFW.GLFW_KEY_C, "zoom.use", "Zoom", null) {
        setUsed(it)
    }

    var factorAnimation = Animator({ if (ZoomSettings.instant.get()) 1 else 100 }, Animator.EASE_OUT, "factor" to 1f)

    fun getFactor(): Float {
        return if (ZoomSettings.enabled.get()) factorAnimation["factor"] else 1f
    }

    fun isUsed(): Boolean {
        return factorAnimation.getWithoutInterpolation("factor") != 1f
    }

    fun setUsed(used: Boolean) {
        if (!isUsed() && used) {
            smoothCameraEnabledBefore = client.options.smoothCameraEnabled
            if (ZoomSettings.smooth.get()) {
                client.options.smoothCameraEnabled = true
            }
            factorAnimation["factor"] = 0.23f
        } else if (!used && isUsed()) {
            if (ZoomSettings.smooth.get()) {
                client.options.smoothCameraEnabled = smoothCameraEnabledBefore ?: false
            }
            factorAnimation["factor"] = 1f
        }
    }
}