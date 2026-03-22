package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.options_structure.HomePlane.no_quick_settings
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.impl.settings.HomePlaneSettings
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.ThemeColorSaver
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.number.Precision
import net.minecraft.text.Text

object HomePlane : Renderable(), OptionScreen.CutoutProvider {
    val quickSettings = Translation("menu.home.quickSettings", "Quick Settings")
    val editQuickSettings = Translation("menu.home.edit_quick_settings", "Edit Quick Settings")
    val widgetPresets = Translation("menu.home.widget_presets", "Widget Presets")
    val moreWidgetOptions = Translation("menu.home.more_widget_options", "More customization options can be found in the widgets tab")
    val currentSettings = Translation("menu.home.current_settings", "Current Settings")
    val defaultSettings = Translation("menu.home.default_settings", "Default Settings")
    val border = Translation("menu.home.border", "Default with Border")
    val themed = Translation("menu.home.themed", "Theme color")
    val themed_border = Translation("menu.home.themed_border", "Theme with Border")
    val selectPreset = Translation("menu.home.select_preset", "Apply preset [%s] to your widgets")
    val no_quick_settings = Translation("menu.home.no_quick_settings", "Here you can add settings that you need frequently, so you don't have to search for them in the different categories and have quicker access to them.")

    var borderRadius = DefaultWidgetSettings.borderRadius.get().toFloat()

    val quickSettingsOptions = mutableMapOf<String, MutableMap<String, Renderable>>()

    val checkTexture = createIdentifier("bewisclient", "textures/gui/sprites/check.png")

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun getCutout(): Renderable = WidgetPresetList

    override fun init() {
        val button = Button(editQuickSettings()) {
            OptionScreen.currentInstance?.openPage(
                Plane { x, y, width, _ -> listOf(TextElement(editQuickSettings(), OptionsMenuSettings.getTextThemeColor(), centered = true)(x, y, width, 13)) }.setHeight(14),
                VerticalAlignScrollPlane ({ width ->
                    quickSettingsOptions.map {
                        listOf(
                            EmptyRenderable().setHeight(5),
                            InfoTextRenderable(Text.translatable("bewisclient." + it.key), centered = true, color = OptionsMenuSettings.getTextThemeColor(), padding = 0),
                            EmptyRenderable().setHeight(3),
                        ) + it.value.map { a -> ConfigureRenderableVisibilityPlane(it.key, a.key, a.value).setWidth(width) }
                    }.flatten()
                }, 1)
            )
        }

        if (HomePlaneSettings.quickSettings.isEmpty()) {
            addRenderable(Plane { x, y, width, height ->
                listOf(
                    InfoTextRenderable(no_quick_settings(), OptionsMenuSettings.getTextThemeColor() alpha 0.66f, centered = true)(x + width / 2 - 100, y + height / 4, 200, 0),
                    button(x + width / 2 - 50, y + height / 2, 100, 14)
                )
            }(x, y, width, height))
            return
        }

        addRenderable(
            VerticalAlignScrollPlane(
                mutableListOf(
                    VerticalAlignPlane(HomePlaneSettings.quickSettings.toSortedSet().filter { it.split("/").size >= 2 }.groupBy { it.split("/")[0] }.mapNotNull {
                        listOf(
                            EmptyRenderable().setHeight(5),
                            InfoTextRenderable(Text.translatable("bewisclient." + it.key), centered = true, color = OptionsMenuSettings.getTextThemeColor(), padding = 0).setHeight(14),
                            EmptyRenderable().setHeight(3),
                        ) + it.value.mapNotNull { a ->
                            quickSettingsOptions[it.key]?.get(a.split("/")[1])
                        }
                    }.flatten(), gap = 1),
                    Plane { x, y, width, height ->
                        listOf(
                            button(x + width / 2 - 50, y, 100, height)
                        )
                    }.setHeight(14)
                ), 5
            )(x, y, width, height)
        )
    }

    class ConfigureRenderableVisibilityPlane(val category: String, val id: String, val renderable: Renderable) : Renderable() {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            internalHeight = renderable.height
            screenDrawing.fillWithBorderRounded(x, y + height / 2 - 8, 16, 16, 5, OptionsMenuSettings.getThemeColor(alpha = 0.15f), OptionsMenuSettings.getThemeColor(alpha = 0.15f))
            renderRenderables(screenDrawing, mouseX, mouseY)

            if (HomePlaneSettings.quickSettings.contains("$category/$id")) {
                screenDrawing.drawTexture(checkTexture, x + 1, y + height / 2 - 7, 14, 14, OptionsMenuSettings.getThemeColor())
            }
        }

        override fun init() {
            renderable.setPosition(x + 20, y)
            renderable.setWidth(width - 20)
            addRenderable(renderable)
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + height / 2 - 8 && mouseY <= y + height / 2 + 8) {
                val key = "$category/$id"
                if (!HomePlaneSettings.quickSettings.remove(key)) {
                    HomePlaneSettings.quickSettings.add(key)
                }
                return true
            }
            return super.onMouseClick(mouseX, mouseY, button)
        }
    }

    val widgetPresetElement = VerticalAlignPlane(
        listOf(
            InfoTextRenderable(widgetPresets(), centered = true, color = OptionsMenuSettings.getTextThemeColor(), padding = 0),
            WidgetPresetList,
            BorderRadiusFader.setHeight(14),
            InfoTextRenderable(moreWidgetOptions(), centered = true, padding = 0)
        )
    ).setWidth(width)

    object BorderRadiusFader : Renderable() {
        val fader = Fader({ borderRadius }, Precision(0f, 10f, 1f, 0), { borderRadius = it })

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            val textWidth = screenDrawing.getTextWidth(Text.translatable("bewisclient.menu.widget.border_radius"))
            screenDrawing.drawText(Text.translatable("bewisclient.menu.widget.border_radius"), x, y + 3, OptionsMenuSettings.getTextThemeColor())
            fader(x + textWidth + 5, y, width - textWidth - 5, 14)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(fader(x, y, width, 14))
        }
    }

    object WidgetPresetList : Renderable() {
        init {
            internalHeight = 200
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            var offsetY = 5
            renderables.forEach {
                it.setPosition(x + 5, y + offsetY)
                it.render(screenDrawing, mouseX, mouseY)
                offsetY += it.height + 5
            }
            internalHeight = offsetY
            screenDrawing.drawBorder(x, y, width, height, OptionsMenuSettings.getThemeColor(black = 0.5f, alpha = 0.5f))
        }

        override fun init() {
            addRenderable(
                WidgetPreviewElement(
                    text = currentSettings(),
                    backgroundColor = { DefaultWidgetSettings.backgroundColor().getColor() alpha DefaultWidgetSettings.backgroundOpacity() },
                    borderColor = { DefaultWidgetSettings.borderColor().getColor() alpha DefaultWidgetSettings.borderOpacity() },
                    paddingSize = { DefaultWidgetSettings.paddingSize() },
                    borderRadius = { DefaultWidgetSettings.borderRadius() },
                    shadow = { DefaultWidgetSettings.shadow() },
                    textColor = { DefaultWidgetSettings.textColor().getColor() },
                    hideTooltip = true
                ).setWidth(width - 10)
            )
            addRenderable(
                WidgetPreviewElement(
                    text = defaultSettings(),
                    backgroundColor = { Color.BLACK alpha 0.5f },
                    borderColor = { Color.BLACK alpha 0f },
                    paddingSize = { 4 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { Color.WHITE }
                ).setWidth(width - 10))
            addRenderable(
                WidgetPreviewElement(
                    text = border(),
                    backgroundColor = { Color.BLACK alpha 0.5f },
                    borderColor = { Color.BLACK },
                    paddingSize = { 5 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { Color.WHITE }
                ).setWidth(width - 10))
            addRenderable(
                WidgetPreviewElement(
                    text = themed(),
                    backgroundColor = { ThemeColorSaver(0.19f).getColor() alpha 0.66f },
                    borderColor = { Color.BLACK alpha 0f },
                    paddingSize = { 4 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { ThemeColorSaver(1f).getColor() }
                ).setWidth(width - 10))
            addRenderable(
                WidgetPreviewElement(
                    text = themed_border(),
                    backgroundColor = { ThemeColorSaver(0.19f).getColor() alpha 0.66f },
                    borderColor = { ThemeColorSaver(0.67f).getColor() alpha 1.0f },
                    paddingSize = { 5 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { ThemeColorSaver(1f).getColor() }
                ).setWidth(width - 10))
        }

        class WidgetPreviewElement(
            val text: Text,
            val backgroundColor: () -> Color,
            val borderColor: () -> Color,
            val paddingSize: () -> Int,
            val borderRadius: () -> Int,
            val shadow: () -> Boolean,
            val textColor: () -> Color,
            hideTooltip: Boolean = false
        ) : TooltipHoverable(if (hideTooltip) null else selectPreset(text)) {
            override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                super.render(screenDrawing, mouseX, mouseY)
                screenDrawing.setDefaultFont()
                val line = text
                internalHeight = screenDrawing.getTextHeight() + paddingSize() * 2 - 2

                screenDrawing.fillWithBorderRounded(x, y, width, height, borderRadius(), backgroundColor(), borderColor())

                val y = y + paddingSize()
                screenDrawing.drawCenteredText(line, x + width / 2, y, textColor(), shadow())
                screenDrawing.setBewisclientFont()
            }
        }
    }
}

fun <T : Renderable> T.addToQuickSettings(category: String, id: String): T {
    HomePlane.quickSettingsOptions.getOrPut(category, ::mutableMapOf)[id] = this
    return this
}