package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.settings.structure.ImageFeature
import org.lwjgl.glfw.GLFW

object Perspective : ImageFeature("perspective", "Perspective") {
    @JvmField
    var cameraAddPitch: Float = 0f

    @JvmField
    var cameraAddYaw: Float = 0f

    object EnablePerspective : Keybind(GLFW.GLFW_KEY_LEFT_ALT, "perspective.enable_perspective", "Perspective", {})
}