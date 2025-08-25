package net.bewis09.bewisclient.impl.functionalities.fullbright

import net.bewis09.bewisclient.drawable.interpolateColor
import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings
import net.bewis09.bewisclient.logic.TextColors
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Style
import org.lwjgl.glfw.GLFW

object Fullbright : ImageSettingCategory(
    "fullbright", Translation("menu.category.fullbright", "Fullbright"), arrayOf(
        FullbrightSettings.brightness.createRenderable(
            "fullbright.brightness", "Brightness", "Adjust the brightness level. 0.0 to 1.0 are the normal levels, while 1.0 to 15.0 is lighting up the world according to the brightness level"
        ),
        FullbrightSettings.nightVision.createRenderable("fullbright.night_vision", "Night Vision", "Allows you to have the visual effect of night vision without actually having it"),
    ), FullbrightSettings.enabled
) {
    val nightVisionEnabledTranslation = Translation("fullbright.night_vision.enabled", TextColors.YELLOW + "Night Vision Enabled")
    val nightVisionDisabledTranslation = Translation("fullbright.night_vision.disabled", TextColors.RED + "Night Vision Disabled")

    val brightnessTranslation = Translation("fullbright.brightness", "Brightness: %s")

    object ToggleNightVision : Keybind(GLFW.GLFW_KEY_H, "fullbright.toggle_night_vision", "Toggle Night Vision", {
        FullbrightSettings.nightVision.toggle()
        if (hasNightVision()) {
            showTitle(nightVisionEnabledTranslation())
        } else {
            showTitle(nightVisionDisabledTranslation())
        }
    })

    object ToggleFullbright : Keybind(GLFW.GLFW_KEY_G, "fullbright.toggle_fullbright", "Toggle Fullbright", {
        val value = FullbrightSettings.brightness.get()

        if (value > 1f) {
            FullbrightSettings.brightness.set(1f)
        } else {
            FullbrightSettings.brightness.set(15f)
        }

        FullbrightSettings.enabled.set(true)

        showFullbrightMessage()
    })

    object IncreaseBrightness : Keybind(GLFW.GLFW_KEY_UP, "fullbright.increase_brightness", "Increase Brightness", {
        val current = FullbrightSettings.brightness.get()
        FullbrightSettings.brightness.set(15f.coerceAtMost(current + 0.25f))
        showFullbrightMessage()
    })

    object DecreaseBrightness : Keybind(GLFW.GLFW_KEY_DOWN, "fullbright.decrease_brightness", "Decrease Brightness", {
        val current = FullbrightSettings.brightness.get()
        FullbrightSettings.brightness.set(0f.coerceAtLeast(current - 0.25f))
        showFullbrightMessage()
    })

    fun showFullbrightMessage() {
        val value = FullbrightSettings.brightness.get()
        showTitle(brightnessTranslation((value * 100).toString() + "%").setStyle(Style.EMPTY.withColor(interpolateColor(0xFF0000, 0xFFFF00, value / 15))))
    }

    private val nightVisionInstance = StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000000, 0, false, false, false)

    fun getNightVisionInstance(): StatusEffectInstance? {
        return if (hasNightVision()) nightVisionInstance else null
    }

    fun hasNightVision(): Boolean {
        return FullbrightSettings.nightVision.get()
    }
}