package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.util.*
import net.bewis09.bewisclient.util.number.Precision

class ChangingColorSaver : ColorSaver {
    val changingSpeed: Int
    val startHue: Float
    val startTime: Long

    companion object {
        val infoTranslation = Translation("color.changing.info", "Changing Color (Speed: %s ms)")
    }

    constructor(changingSpeed: Int, startTime: Long = 0, startHue: Float = 0f) {
        this.changingSpeed = changingSpeed
        this.startHue = startHue
        this.startTime = startTime
    }

    fun getHue(): Float {
        return (((System.currentTimeMillis() - startTime) % changingSpeed) / changingSpeed.toFloat() + startHue) % 1f
    }

    override fun getColor(): Color {
        return Color(getHue(), 1f, 1f)
    }

    override fun getType(): String = "changing"

    override fun saveToJson(): JsonElement {
        return JsonPrimitive(changingSpeed)
    }

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

    class SettingRenderable(val get: () -> ChangingColorSaver, val set: (ColorSaver) -> Unit) : Renderable() {
        val fader = Fader({ get().changingSpeed.toFloat() }, Precision(1000f, 20000f, 100f, -2)) { speed ->
            set(
                ChangingColorSaver(speed.toInt(), System.currentTimeMillis(), get().getHue())
            )
        }
        val text = TextElement({ Translations.CHANGE_DURATION(get().changingSpeed / 1000f) }, centered = true)
        val spectrumButton = ImageButton(texture) {}.setImagePadding(0)
        val actionButton = Rectangle { get().getColor() }

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

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
            screenDrawing.translate(get().getHue() * (width - 1), 0f) {
                screenDrawing.drawVerticalLine(x, y + 36, 8, Color.BLACK)
            }
        }

        override fun init() {
            addRenderable(
                text(
                    x,
                    y + 2,
                    width,
                    9,
                )
            )
            addRenderable(
                fader(
                    x, y + 11, width, 14
                )
            )
            addRenderable(
                Rectangle(0xAAAAAA.color alpha 0.5f)(
                    x, y + 29, width, 1
                )
            )
            addRenderable(
                spectrumButton(
                    x, y + 36, width, 8
                )
            )
            addRenderable(
                Rectangle(0xAAAAAA.color alpha 0.5f)(
                    x, y + 49, width, 1
                )
            )
            addRenderable(
                actionButton(
                    x, y + 55, width, 8
                )
            )
        }
    }
}