package net.bewis09.bewisclient.features.utilities

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.settings.structure.ImageFeature

object Perspective : ImageFeature("perspective", "Perspective") {
    @JvmField
    var cameraAddPitch: Float = 0f

    @JvmField
    var cameraAddYaw: Float = 0f

    object EnablePerspective : Keybind(InputConstants.KEY_LALT, "perspective.enable_perspective", "Perspective", {})
}