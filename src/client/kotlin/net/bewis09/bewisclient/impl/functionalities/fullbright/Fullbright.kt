package net.bewis09.bewisclient.impl.functionalities.fullbright

import net.bewis09.bewisclient.drawable.interpolateColor
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.TextColors
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Style
import org.lwjgl.glfw.GLFW

object Fullbright: BewisclientInterface {
    val nightVisionEnabledTranslation = Translation("fullbright.night_vision.enabled", TextColors.YELLOW+"Night Vision Enabled")
    val nightVisionDisabledTranslation = Translation("fullbright.night_vision.disabled", TextColors.RED+"Night Vision Disabled")

    val brightnessTranslation = Translation("fullbright.brightness", "Brightness: %s")

    object ToggleNightVision: Keybind(GLFW.GLFW_KEY_H,"fullbright.toggle_night_vision", "Toggle Night Vision", {
        getSettings().fullbright.nightVision.toggle()
        if (hasNightVision()) {
            showTitle(nightVisionEnabledTranslation())
        } else {
            showTitle(nightVisionDisabledTranslation())
        }
    })

    object ToggleFullbright: Keybind(GLFW.GLFW_KEY_G, "fullbright.toggle_fullbright", "Toggle Fullbright", {
        val value = getSettings().fullbright.brightness.get()

        if (value > 1f) {
            getSettings().fullbright.brightness.set(1f)
        } else {
            getSettings().fullbright.brightness.set(15f)
        }

        getSettings().fullbright.enabled.set(true)

        showFullbrightMessage()
    })

    object IncreaseBrightness: Keybind(GLFW.GLFW_KEY_UP, "fullbright.increase_brightness", "Increase Brightness", {
        val current = getSettings().fullbright.brightness.get()
        getSettings().fullbright.brightness.set(15f.coerceAtMost(current + 0.25f))
        showFullbrightMessage()
    })

    object DecreaseBrightness: Keybind(GLFW.GLFW_KEY_DOWN, "fullbright.decrease_brightness", "Decrease Brightness", {
        val current = getSettings().fullbright.brightness.get()
        getSettings().fullbright.brightness.set(0f.coerceAtLeast(current - 0.25f))
        showFullbrightMessage()
    })

    fun showFullbrightMessage() {
        val value = getSettings().fullbright.brightness.get()
        showTitle(brightnessTranslation((value * 100).toString() + "%").setStyle(Style.EMPTY.withColor(interpolateColor(0xFF0000, 0xFFFF00, value / 15))))
    }

    private val nightVisionInstance = StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000000, 0, false, false, false)

    fun getNightVisionInstance(): StatusEffectInstance? {
        return if (hasNightVision()) nightVisionInstance else null
    }

    fun hasNightVision(): Boolean {
        return getSettings().fullbright.nightVision.get()
    }
}