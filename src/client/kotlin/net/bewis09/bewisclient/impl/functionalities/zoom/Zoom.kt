package net.bewis09.bewisclient.impl.functionalities.zoom

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.renderables.option_screen.ImageSettingCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.ZoomSettings
import org.lwjgl.glfw.GLFW

object Zoom: ImageSettingCategory(
    "zoom", Translation("menu.category.zoom", "Zoom"), arrayOf(
        ZoomSettings.enabled.createRenderable("zoom.enabled", "Enable Zoom", "Enable or disable zoom functionality"),
        ZoomSettings.smooth.createRenderable("zoom.smooth", "Smooth Zoom", "Enable or disable smooth zoom (Works as if smooth camera is enabled)"),
        ZoomSettings.instant.createRenderable("zoom.instant", "Instant Zoom", "Disables the transition animation when zooming in or out")
    )
) {
    val ZoomKeybind = Keybind(GLFW.GLFW_KEY_C, "zoom.use", "Zoom", null) {
        setUsed(it)
    }

    var factorAnimation = Animator({ if(ZoomSettings.instant.get()) 0 else 200 }, Animator.EASE_OUT, "factor" to 1f)

    fun getFactor(): Float {
        return if(ZoomSettings.enabled.get()) factorAnimation["factor"] else 1f
    }

    fun isUsed(): Boolean {
        return factorAnimation.getWithoutInterpolation("factor") != 1f
    }

    fun setUsed(used: Boolean) {
        if (!isUsed() && used) {
            factorAnimation["factor"] = 0.23f
        } else if (!used && isUsed()) {
            factorAnimation["factor"] = 1f
        }
    }
}