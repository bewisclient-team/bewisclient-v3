package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.settings.structure.ImageFeature
import org.lwjgl.glfw.GLFW

object Zoom : ImageFeature(createIdentifier("bewisclient", "zoom"), "Zoom") {
    val smooth by boolean("smooth", true, "Smooth Zoom", "Enable or disable smooth zoom (Works as if smooth camera is enabled)", quickSetting = true)
    val instant by boolean("instant", false, "Instant Zoom", "Disables the transition animation when zooming in or out", quickSetting = true)

    var smoothCameraEnabledBefore: Boolean? = null

    val ZoomKeybind = Keybind(GLFW.GLFW_KEY_C, "zoom.use", "Zoom", null, ::setUsed)

    var factorAnimation = Animator({ if (instant) 1 else 100 }, Animator.EASE_OUT, 1f)

    override val enabledByDefault: Boolean
        get() = true

    fun getFactor(): Float = if (enabled) factorAnimation.get() else 1f

    fun isUsed(): Boolean = factorAnimation.getWithoutInterpolation() != 1f

    fun setUsed(used: Boolean) {
        if (used == isUsed()) return

        if (used) {
            smoothCameraEnabledBefore = client.options.smoothCamera
            if (smooth) {
                client.options.smoothCamera = true
            }
            factorAnimation.set(0.23f)
        } else {
            if (smooth) {
                client.options.smoothCamera = smoothCameraEnabledBefore ?: false
            }
            factorAnimation.set(1f)
        }
    }
}