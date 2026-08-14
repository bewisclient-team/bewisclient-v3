package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.color
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.common.within
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import org.lwjgl.glfw.GLFW

object Fullbright : ImageFeature(createIdentifier("bewisclient", "fullbright"), "Fullbright") {
    var nightVision by boolean("night_vision", false, "Night Vision", "Allows you to have the visual effect of night vision without actually having it", quickSetting = true)
    var brightness by float("brightness", 1f, 0f, 15f, 0.01f, 2, "Brightness", "Adjust the brightness level. 0.0 to 1.0 are the normal levels, while 1.0 to 15.0 is lighting up the world according to the brightness level", quickSetting = true)

    val nightVisionEnabledTranslation = createTranslation("night_vision.enabled", "Night Vision Enabled")
    val nightVisionDisabledTranslation = createTranslation("night_vision.disabled", "Night Vision Disabled")

    val brightnessTranslation = createTranslation("brightness", "Brightness: %s")

    val infoText = createTranslation(
        "night_vision.error_text", "When night vision is applied via Bewisclient, the effect will not be the same as if you got it via a potion, because Bewisclient preserves the old way in which night vision works, which illuminates the world completely, whilst with the status effect it is always rendered as if the brightness is set all the way down to moody."
    )

    object ToggleNightVision : Keybind(GLFW.GLFW_KEY_H, "fullbright.toggle_night_vision", "Toggle Night Vision", {
        nightVision = !nightVision
        if (hasNightVision()) {
            showTitle(nightVisionEnabledTranslation().setColor(0xFFFF55))
        } else {
            showTitle(nightVisionDisabledTranslation().setColor(0xFF5555))
        }
    })

    object ToggleFullbright : Keybind(GLFW.GLFW_KEY_G, "fullbright.toggle_fullbright", "Toggle Fullbright", {
        brightness = if (brightness > 1f) 1f else 15f
        enabled = true

        showFullbrightMessage()
    })

    object IncreaseBrightness : Keybind(GLFW.GLFW_KEY_UP, "fullbright.increase_brightness", "Increase Brightness", {
        brightness = 15f.coerceAtMost(brightness + 0.25f)
        showFullbrightMessage()
    })

    object DecreaseBrightness : Keybind(GLFW.GLFW_KEY_DOWN, "fullbright.decrease_brightness", "Decrease Brightness", {
        brightness = 0f.coerceAtLeast(brightness - 0.25f)
        showFullbrightMessage()
    })

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        super.appendSettingsRenderables(list)
        list.add(InfoTextRenderable(infoText(), 0xAAAAAA.color * General.getThemeColor(), true))
    }

    fun showFullbrightMessage() {
        showTitle(brightnessTranslation((brightness * 100).toString() + "%").setColor(((brightness / 15) within (0xFF0000.color to 0xFFFF00.color)).argb))
    }

    private val nightVisionInstance = MobEffectInstance(MobEffects.NIGHT_VISION, -1, 255, false, false, false)

    fun getNightVisionInstance(): MobEffectInstance? {
        return if (hasNightVision()) nightVisionInstance else null
    }

    fun hasNightVision(): Boolean {
        return nightVision && enabled
    }
}