package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.colors
import net.bewis09.bewisclient.logic.number.Precision
import java.awt.Color

open class StaticColorSaver : ColorSaver {
    private val color: Color

    companion object {
        val infoTranslation = Translation("color.static.info", "Static Color (Color: %s)")

        fun fromColorString(colorString: String): StaticColorSaver? {
            if (colorString.startsWith("#")) {
                return StaticColorSaver(colorString.substring(1).toIntOrNull(16) ?: 0xFFFFFF)
            }
            return null
        }
    }

    constructor(color: Int) {
        this.color = createColor(color)
    }

    constructor(color: Color) {
        this.color = color
    }

    constructor(r: Float, g: Float, b: Float) {
        this.color = createColor(r, g, b)
    }

    constructor(r: Int, g: Int, b: Int) {
        this.color = createColor(r, g, b)
    }

    override fun getColor(): Color {
        return color
    }

    override fun getType(): String = "static"

    override fun saveToJson(): JsonElement {
        return JsonPrimitive(getColorString())
    }

    fun getColorString(): String {
        return String.format("#%06X", color.rgb and 0xFFFFFF)
    }

    object Factory : ColorSaverFactory<StaticColorSaver> {
        private val translation = Translation("color.static", "Static")
        private val description = Translation("color.static.description", "A static color that does not change.")

        override fun createFromJson(jsonElement: JsonElement): StaticColorSaver? {
            return if (jsonElement.isJsonPrimitive) {
                fromColorString(jsonElement.asString)
            } else {
                null
            }
        }

        override fun getType(): String = "static"

        override fun getTranslation(): Translation = translation

        override fun getDefault(): StaticColorSaver = StaticColorSaver(0xFFFFFF)

        override fun getDescription(): Translation = description

        override fun getSettingsRenderable(get: () -> StaticColorSaver, set: (ColorSaver) -> Unit) = SettingRenderable(get, set)
    }

    override fun toInfoString(): String {
        return infoTranslation(getColorString()).string
    }

    class SettingRenderable(val get: () -> StaticColorSaver, val set: (ColorSaver) -> Unit) : Renderable() {
        val colorPicker = ColorPicker({ get().getColor() }) { hue, sat -> set(StaticColorSaver(Color.HSBtoRGB(hue, sat, get().getColor().brightness ))) }
        val fader = Fader({ get().getColor().brightness }, Precision(0f, 1f, 0.01f, 2)) { bri ->
            set(StaticColorSaver(get().getColor().withBrightness(bri)))
        }
        val text = Text(Translations.CHANGE_BRIGHTNESS.getTranslatedString(), centered = true)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(
                colorPicker(
                    getX(), getY(), getHeight(), getHeight()
                )
            )
            addRenderable(
                text(
                    getX() + getHeight() + 6,
                    getY() + 2,
                    getWidth() - getHeight() - 5,
                    9,
                )
            )
            addRenderable(
                fader(
                    getX() + getHeight() + 6, getY() + 11, getWidth() - getHeight() - 6, 14
                )
            )
            addRenderable(Rectangle(0xAAAAAA.color alpha 0.5f)(getX() + getHeight() + 5, getY() + 30, getWidth() - getHeight() - 5, 1))
            addRenderable(ColorButton(getX() + getHeight() + 5, getY() + 36, 27, 27, { get().getColor() }, String.format("#%06X", get().getColor().rgb)))
            addRenderable(Rectangle(0xAAAAAA.color alpha 0.5f)(getX() + getHeight() + 37, getY() + 36, 1, 27))

            addRenderable(
                HorizontalScrollGrid({
                    return@HorizontalScrollGrid colors.map { color ->
                        ColorButton(0, 0, 12, 12, { color.color }, color.translation.getTranslatedString(), { newColor ->
                            set(StaticColorSaver(newColor))
                        })
                    }
                }, 3, 12)(
                    getX() + getHeight() + 43, getY() + 36, getWidth() - getHeight() - 43, 27
                )
            )
        }

        class ColorButton(x: Int, y: Int, width: Int, height: Int, val color: () -> Color, tooltip: String? = null, val onClick: ((Color) -> Unit)? = null) : TooltipHoverable(tooltip?.let { Translation.literal(it) }) {
            init {
                this.x = x.toUInt()
                this.y = y.toUInt()
                this.width = width.toUInt()
                this.height = height.toUInt()
            }

            override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                super.render(screenDrawing, mouseX, mouseY)
                screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 3, color(), 0xAAAAAA.color alpha  0.5f)
            }

            override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
                onClick?.let {
                    it(color())
                    return true
                }

                return false
            }
        }
    }
}