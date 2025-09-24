package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.PerspectiveSettings
import org.lwjgl.glfw.GLFW

object Perspective : ImageSettingCategory(
    "perspective", Translation("menu.category.perspective", "Perspective"), arrayOf(), PerspectiveSettings.enabled
) {
    @JvmField
    var cameraAddPitch: Float = 0f
    @JvmField
    var cameraAddYaw: Float = 0f

    object EnablePerspective : Keybind(GLFW.GLFW_KEY_DOWN, "perspective.enable_perspective", "Perspective", {})
}