package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignPlane
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.ThemeColorSaver
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.number.Precision
import net.minecraft.text.Text

object HomePlane : Renderable() {
    val quickSettings = Translation("menu.home.quickSettings", "Quick Settings")
    val widgetPresets = Translation("menu.home.widget_presets", "Widget Presets")
    val moreWidgetOptions = Translation("menu.home.more_widget_options", "More customization options can be found in the widgets tab")
    val currentSettings = Translation("menu.home.current_settings", "Current Settings")
    val defaultSettings = Translation("menu.home.default_settings", "Default Settings")
    val border = Translation("menu.home.border", "Default with Border")
    val themed = Translation("menu.home.themed", "Theme color")
    val themed_border = Translation("menu.home.themed_border", "Theme with Border")
    val selectPreset = Translation("menu.home.select_preset", "Apply preset [%s] to your widgets")

    var borderRadius = DefaultWidgetSettings.borderRadius.get().toFloat()

    val quickSettingsOptions = mutableMapOf<String, MutableList<Renderable>>()

    val widgetBackgroundTexture = createIdentifier("bewisclient", "textures/gui/widget_presets_background.png")

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(VerticalScrollGrid({ width ->
            listOf(
                VerticalAlignPlane(listOf(
                    InfoTextRenderable(widgetPresets(), centered = true, color = OptionsMenuSettings.getTextThemeColor(), padding = 0),
                    WidgetPresetList,
                    BorderRadiusFader.setHeight(14),
                    InfoTextRenderable(moreWidgetOptions(), centered = true, padding = 0)
                )).setWidth(width),
                VerticalAlignPlane(listOf(
                    InfoTextRenderable(quickSettings(), centered = true, color = OptionsMenuSettings.getTextThemeColor()),
                    QuickSettingsPane,

                )).setWidth(width)
            )
        }, 5, 120)(x, y, width, height))
    }

    object QuickSettingsPane: Renderable() {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {

        }
    }

    object BorderRadiusFader: Renderable() {
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
            screenDrawing.drawTextureRegion(widgetBackgroundTexture, x, y, 480 / 2f - width / 2f, 270 / 2f - height / 2f, width, height, width, height, 480, 270)
            renderables.forEach {
                it.setPosition(x + 5, y + offsetY)
                it.render(screenDrawing, mouseX, mouseY)
                offsetY += it.height + 5
            }
            internalHeight = offsetY
            screenDrawing.drawBorder(x, y, width, height, OptionsMenuSettings.getThemeColor(black = 0.1f, alpha = 0.5f))
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
                ).setWidth(width - 10))
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
                    backgroundColor = { ThemeColorSaver(0.27f).getColor() alpha 0.59f },
                    borderColor = { Color.BLACK alpha 0f },
                    paddingSize = { 4 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { ThemeColorSaver(0.94f).getColor() }
                ).setWidth(width - 10))
            addRenderable(
                WidgetPreviewElement(
                    text = themed_border(),
                    backgroundColor = { ThemeColorSaver(0.27f).getColor() alpha 0.59f },
                    borderColor = { ThemeColorSaver(0.74f).getColor() alpha 0.6f },
                    paddingSize = { 5 },
                    borderRadius = { borderRadius.toInt() },
                    shadow = { true },
                    textColor = { ThemeColorSaver(0.94f).getColor() }
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
        ) : TooltipHoverable(if(hideTooltip) null else selectPreset(text)) {
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

fun <T: Renderable> T.addToQuickSettings(category: String): T {
    HomePlane.quickSettingsOptions.getOrPut(category, ::mutableListOf).add(this)
    return this
}