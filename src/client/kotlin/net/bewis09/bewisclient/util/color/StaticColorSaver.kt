package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.renderables.components.setting.ColorPicker
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.bewisclient.util.string
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.Direction
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.LineType
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.logic.color

open class StaticColorSaver(private val color: Color) : ColorSaver {
    companion object {
        val infoTranslation = Translation("color.static.info", "Static Color (Color: %s)")
        val changeBrightnessText = Translation("menu.color.change_brightness", "Change Brightness")

        fun fromColorString(colorString: String): StaticColorSaver? {
            if (colorString.startsWith("#")) {
                return StaticColorSaver(color(colorString.substring(1).toIntOrNull(16)) ?: Color.WHITE)
            }
            return null
        }
    }

    constructor(r: Float, g: Float, b: Float) : this(Color((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt()))

    override fun getColor(): Color = color

    override fun getType(): String = "static"

    override fun saveToJson(): JsonElement = JsonPrimitive(getColorString())

    fun getColorString(): String {
        return String.format("#%06X", color.argb and 0xFFFFFF)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StaticColorSaver) return false
        return color == other.color
    }

    override fun hashCode(): Int {
        return color.hashCode()
    }

    object Factory : ColorSaverFactory<StaticColorSaver> {
        private val translation = Translation("color.static", "Static")
        private val description = Translation("color.static.description", "A static color that does not change.")

        override fun createFromJson(jsonElement: JsonElement): StaticColorSaver? {
            return jsonElement.string()?.let { fromColorString(it) }
        }

        override fun getType(): String = "static"

        override fun getTranslation(): Translation = translation

        override fun getDefault(): StaticColorSaver = StaticColorSaver(Color.WHITE)

        override fun getDescription(): Translation = description

        override fun getSettingsRenderable(get: () -> StaticColorSaver, set: (ColorSaver) -> Unit) = SettingRenderable(get, set)
    }

    override fun toInfoString(): String {
        return infoTranslation(getColorString()).string
    }

    class SettingRenderable(val get: () -> StaticColorSaver, val set: (ColorSaver) -> Unit) : SimpleRenderable() {
        val colorPicker = ColorPicker {
            get = { this@SettingRenderable.get().getColor() }
            set = { hue, sat -> this@SettingRenderable.set(StaticColorSaver(Color(hue, sat, this@SettingRenderable.get().getColor().brightness))) }
        }

        override fun Init.init() {
            addRenderable(colorPicker(x, y, height, height))
            Text {
                text = changeBrightnessText();
                textAlign = TextAlign.CENTER
            }(x + height + 6, y + 2, width - height - 5, 9)
            Fader {
                value = { get().getColor().brightness }
                precision = Precision(0f, 1f, 0.01f, 2)
                onChange = { bri ->
                    set(StaticColorSaver(get().getColor().withBrightness(bri)))
                }
            }(x + height + 6, y + 11, width - height - 6, 14)
            Rectangle {
                colorProvider = { if (General.isMinecrafty) Color.WHITE alpha 0.3f else General.getThemeColor(alpha = 0.3f) }
            }(x + height + 5, y + 30, width - height - 5, 1)
            ColorButton {
                color = { get().getColor() }
                tooltip = String.format("#%06X", get().getColor().argb).toText()
            }(x + height + 5, y + 36, 27, 27).add()
            Rectangle {
                colorProvider = { if (General.isMinecrafty) Color.WHITE alpha 0.3f else General.getThemeColor(alpha = 0.3f) }
            }(x + height + 37, y + 36, 1, 27)
            Div {
                onInit = {
                    colors.forEach { color ->
                        ColorButton {
                            x = 0
                            y = 0
                            width = 12
                            height = 12
                            this.color = { color.color }
                            tooltip = color.translation()
                            this.onClick = { newColor ->
                                set(StaticColorSaver(newColor))
                            }
                        }.add()
                    }
                }
                gap = 3
                minElementSize = 12
                lines = 2
                direction = Direction.HORIZONTAL
                lineType = LineType.DEFINITE
                fitType = FitType.SCROLL
            }(x + height + 43, y + 36, width - height - 43, 27)
        }

        class ColorButton(p: Props<ColorButton>) : TooltipHoverable<ColorButton>(p) {
            lateinit var color: () -> Color
            var onClick: ((Color) -> Unit)? = null

            init {
                props()
            }

            override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                screenDrawing.fillWithBorderRounded(x, y, width, height, if (General.isMinecrafty) 0 else 3, color(), if (General.isMinecrafty) Color.WHITE alpha 0.3f else General.getThemeColor(alpha = 0.3f))
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