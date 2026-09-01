package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.util.int
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.renderite.drawer.translate
import net.bewis09.renderite.logic.Color

class ChangingColorSaver : ColorSaver {
    val changingSpeed: Int
    val startHue: Float
    val startTime: Long

    companion object {
        val infoTranslation = Translation("color.changing.info", "Changing Color (Speed: %s ms)")
        val changeDuration = Translation("menu.color.change_duration", "Change Duration (%s)")
    }

    constructor(changingSpeed: Int, startTime: Long = 0, startHue: Float = 0f) {
        this.changingSpeed = changingSpeed
        this.startHue = startHue
        this.startTime = startTime
    }

    fun getHue(): Float {
        return (((System.currentTimeMillis() - startTime) % changingSpeed) / changingSpeed.toFloat() + startHue) % 1f
    }

    override fun getColor(): Color = Color(getHue(), 1f, 1f)

    override fun getType(): String = "changing"

    override fun saveToJson(): JsonElement = JsonPrimitive(changingSpeed)

    object Factory : ColorSaverFactory<ChangingColorSaver> {
        private val translation = Translation("color.changing", "Changing")
        private val description = Translation("color.changing.description", "A color that changes over time, cycling through the spectrum based on the speed set.")

        override fun createFromJson(jsonElement: JsonElement): ChangingColorSaver? {
            return jsonElement.int()?.let { ChangingColorSaver(it) }
        }

        override fun getType(): String = "changing"

        override fun getTranslation(): Translation = translation

        override fun getDefault(): ChangingColorSaver = ChangingColorSaver(5000)

        override fun getDescription(): Translation = description

        override fun getSettingsRenderable(get: () -> ChangingColorSaver, set: (ColorSaver) -> Unit): Renderable = SettingRenderable(get, set)
    }

    override fun toInfoString(): String = infoTranslation(changingSpeed.toString()).string

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChangingColorSaver) return false
        return changingSpeed == other.changingSpeed && startHue == other.startHue && startTime == other.startTime
    }

    override fun hashCode(): Int {
        var result = changingSpeed
        result = 31 * result + startHue.hashCode()
        result = 31 * result + startTime.hashCode()
        return result
    }

    class SettingRenderable(val get: () -> ChangingColorSaver, val set: (ColorSaver) -> Unit) : SimpleRenderable() {
        companion object {
            val texture = Bewisclient.createTexture(createIdentifier("bewisclient", "color_strip_selector_190"), 190, 14) { image ->
                for (x in 0 until 190) {
                    for (y in 0 until 14) {
                        val color = Color(x / 190f, 1f, 1f)
                        image.setRGB(x, y, color.argb)
                    }
                }
            }
        }

        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.translate(get().getHue() * (width - 1), 0f) {
                screenDrawing.drawVerticalLine(x, y + 36, 8, Color.BLACK)
            }
        }

        override fun Init.init() {
            Text {
                textProvider = { changeDuration(get().changingSpeed / 1000f) }
                textAlign = TextAlign.CENTER
            }(x, y + 2, width, 9)
            Fader {
                value = { get().changingSpeed.toFloat() }
                precision = Precision(1000f, 20000f, 100f, -2)
                onChange = { speed ->
                    set(ChangingColorSaver(speed.toInt(), System.currentTimeMillis(), get().getHue()))
                }
            }(x, y + 11, width, 14)
            Rectangle {
                colorProvider = { General.getThemeColor(alpha = 0.3f) }
            }(x, y + 29, width, 1)
            ImageButton {
                image = texture
                imagePadding = 0
            }(x, y + 36, width, 8)
            Rectangle {
                colorProvider = { General.getThemeColor(alpha = 0.3f) }
            }(x, y + 49, width, 1)
            Rectangle {
                colorProvider = { get().getColor() }
            }(x, y + 55, width, 8)
        }
    }
}