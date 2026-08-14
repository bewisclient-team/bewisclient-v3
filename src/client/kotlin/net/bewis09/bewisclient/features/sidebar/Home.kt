package net.bewis09.bewisclient.features.sidebar

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.element.TextElement
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.drawable.renderables.components.structure.EmptyRenderable
import net.bewis09.bewisclient.drawable.renderables.components.structure.Plane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignPlane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.util.color.ThemeColorSaver
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.bewisclient.util.string
import net.minecraft.network.chat.Component

object Home : SidebarFeature(createIdentifier("bewisclient", "home"), "Bewisclient") {
    val quickSettings = create("quick_settings", ListSetting(mutableListOf(), { it.string() }, ::JsonPrimitive))

    override fun getRenderable(): Renderable = HomePlane

    object HomePlane : Renderable() {
        val editQuickSettings = createTranslation("edit_quick_settings", "Edit Quick Settings")
        val widgetPresets = createTranslation("widget_presets", "Widget Presets")
        val moreWidgetOptions = createTranslation("more_widget_options", "More customization options can be found in the widgets tab")
        val currentSettings = createTranslation("current_settings", "Current Settings")
        val defaultSettings = createTranslation("default_settings", "Default Settings")
        val border = createTranslation("border", "Default with Border")
        val themed = createTranslation("themed", "Theme color")
        val themed_border = createTranslation("themed_border", "Theme with Border")
        val selectPreset = createTranslation("select_preset", "Apply preset [%s] to your widgets")
        val no_quick_settings = createTranslation("no_quick_settings", "Here you can add settings that you need frequently, so you don't have to search for them in the different categories and have quicker access to them.")

        var borderRadius = Widgets.Default.borderRadius.get().toFloat()

        val quickSettingsOptions = mutableMapOf<String, MutableMap<String, Renderable>>()

        val checkTexture = createIdentifier("bewisclient", "textures/gui/sprites/check.png")

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            val button = Button(editQuickSettings()) {
                OptionScreen.currentInstance?.openPage(
                    Plane { x, y, width, _ -> listOf(TextElement(editQuickSettings(), General.getTextThemeColor(), centered = true)(x, y, width, 13)) }.setHeight(14),
                    VerticalAlignScrollPlane({ width ->
                        quickSettingsOptions.map {
                            listOf(
                                EmptyRenderable().setHeight(5),
                                InfoTextRenderable(Component.translatable(it.key), centered = true, color = General.getTextThemeColor(), padding = 0),
                                EmptyRenderable().setHeight(3),
                            ) + it.value.map { a -> ConfigureRenderableVisibilityPlane(it.key, a.key, a.value).setWidth(width) }
                        }.flatten()
                    }, 1)
                )
            }

            val innerList = quickSettings.asSequence().filter { it.split("/").size >= 2 }.groupBy { it.split("/")[0] }.mapNotNull {
                (listOf(
                    EmptyRenderable().setHeight(5),
                    InfoTextRenderable(Component.translatable(it.key), centered = true, color = General.getTextThemeColor(), padding = 0).setHeight(14),
                    EmptyRenderable().setHeight(3),
                ) + it.value.mapNotNull { a ->
                    quickSettingsOptions[it.key]?.get(a.split("/")[1])
                }).run { if (size == 3) null else this }
            }.flatten().toList()

            if (innerList.isEmpty()) {
                addRenderable(Plane { x, y, width, height ->
                    listOf(
                        InfoTextRenderable(no_quick_settings(), General.getTextThemeColor() alpha 0.66f, centered = true)(x + width / 2 - 100, y + height / 4, 200, 0),
                        button(x + width / 2 - 50, y + height / 2, 100, SelectiveScreenDrawer.getSideButtonHeight())
                    )
                }(x, y, width, height))
                return
            }

            addRenderable(
                VerticalAlignScrollPlane(
                    mutableListOf(
                        VerticalAlignPlane(innerList, gap = 1),
                        Plane { x, y, width, height ->
                            listOf(
                                button(x + width / 2 - 50, y, 100, height)
                            )
                        }.setHeight(SelectiveScreenDrawer.getSideButtonHeight())
                    ), 5
                )(x, y, width, height)
            )
        }

        class ConfigureRenderableVisibilityPlane(val category: String, val id: String, val renderable: Renderable) : Renderable() {
            override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                internalHeight = renderable.height
                if (isMinecrafty) {
                    SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x, y + height / 2 - 9, 18, 18, 0f, mouseX, mouseY)
                } else {
                    screenDrawing.fillWithBorderRounded(x, y + height / 2 - 8, 16, 16, 5, General.getThemeColor(alpha = 0.15f), General.getThemeColor(alpha = 0.15f))
                }

                renderRenderables(screenDrawing, mouseX, mouseY)

                if (quickSettings.contains("$category/$id")) {
                    screenDrawing.drawTexture(checkTexture, x + if (isMinecrafty) 2 else 1, y + height / 2 - 7, 14, 14, if (isMinecrafty) Color.WHITE else General.getThemeColor())
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
                    if (!quickSettings.remove(key)) {
                        quickSettings.add(key)
                    }
                    return true
                }
                return super.onMouseClick(mouseX, mouseY, button)
            }
        }

        val widgetPresetElement = VerticalAlignPlane(
            listOf(
                InfoTextRenderable(widgetPresets(), centered = true, color = General.getTextThemeColor(), padding = 0),
                WidgetPresetList,
                BorderRadiusFader.setHeight(14),
                InfoTextRenderable(moreWidgetOptions(), centered = true, padding = 0)
            )
        ).setWidth(width)

        object BorderRadiusFader : Renderable() {
            val fader = Fader({ borderRadius }, Precision(0f, 10f, 1f, 0), { borderRadius = it })

            override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                val textWidth = screenDrawing.getTextWidth(Component.translatable("bewisclient.menu.widget.border_radius"))
                screenDrawing.drawText(Component.translatable("bewisclient.menu.widget.border_radius"), x, y + 3, General.getTextThemeColor())
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
                screenDrawing.drawBorder(x, y, width, height, General.getThemeColor(black = 0.5f, alpha = 0.5f))
            }

            override fun init() {
                addRenderable(
                    WidgetPreviewElement(
                        text = currentSettings(),
                        backgroundColor = { Widgets.Default.backgroundColor().getColor() alpha Widgets.Default.backgroundOpacity() },
                        borderColor = { Widgets.Default.borderColor().getColor() alpha Widgets.Default.borderOpacity() },
                        paddingSize = { Widgets.Default.paddingSize() },
                        borderRadius = { Widgets.Default.borderRadius() },
                        shadow = { Widgets.Default.shadow() },
                        textColor = { Widgets.Default.textColor().getColor() },
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
                val text: Component,
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
}