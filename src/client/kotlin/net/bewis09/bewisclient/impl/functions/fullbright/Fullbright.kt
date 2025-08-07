package net.bewis09.bewisclient.impl.functions.fullbright

import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.TextColors
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import org.lwjgl.glfw.GLFW

object Fullbright: BewisclientInterface {
    val nightVisionEnabledTranslation = Translation("fullbright.night_vision.enabled", TextColors.YELLOW+"Night Vision Enabled")
    val nightVisionDisabledTranslation = Translation("fullbright.night_vision.disabled", TextColors.RED+"Night Vision Disabled")

    object ToggleNightVision: Keybind(GLFW.GLFW_KEY_H,"fullbright.toggle_night_vision", "Toggle Night Vision", {
        getSettings().fullbright.nightVision.toggle()
        if (hasNightVision()) {
            showTitle(nightVisionEnabledTranslation())
        } else {
            showTitle(nightVisionDisabledTranslation())
        }
    })

    private val nightVisionInstance = StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000000, 0, false, false, false)

    fun getNightVisionInstance(): StatusEffectInstance? {
        return if (hasNightVision()) nightVisionInstance else null
    }

    fun hasNightVision(): Boolean {
        return getSettings().fullbright.nightVision.get()
    }
}