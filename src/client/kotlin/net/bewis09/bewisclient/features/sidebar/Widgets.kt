package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.element.TextElement
import net.bewis09.bewisclient.drawable.renderables.components.structure.Plane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.settings.structure.CategorizedFeature
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.util.color.StaticColorSaver
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.bewisclient.widget.WidgetLoader.widgets

object Widgets : SidebarFeature(createIdentifier("bewisclient", "widgets"), "Widgets") {
    init {
        create("defaults", Default)
        create("widgets", WidgetLoader)
    }

    val widgetRenderables = widgets.map(CategorizedFeature::createRenderable)

    val generalWidgetSettings = APIEntrypointLoader.mapEntrypoint { it.getGeneralWidgetSettings() }.flatten()

    val widgetsPlane = Plane { x, y, width, height ->
        listOf(
            Button(createTranslation("general_setting", "General Widget Settings")()) {
                OptionScreen.currentInstance?.openPage(
                    TextElement(createTranslation("general_setting", "General Widget Settings")(), centered = true).setHeight(12), VerticalAlignScrollPlane({ generalWidgetSettings }, 1)
                )
            }(x, y, width, SelectiveScreenDrawer.getSideButtonHeight()),
//            Button(Translation("menu.widgets.presets", "Presets")()) {
//
//            }(x + width - 55, 37, 55, 14),
            VerticalScrollGrid({ widgetRenderables.map { a -> a.setHeight(90) } }, 5, 80).invoke(x, y + SelectiveScreenDrawer.getSideButtonHeight() + 5, width, height - SelectiveScreenDrawer.getSideButtonHeight() - 5)
        )
    }

    override fun getRenderable(): Renderable = widgetsPlane

    object Default : Feature(createIdentifier("bewisclient", "widgets_defaults")) {
        val backgroundColor = color("background_color", StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
        val backgroundOpacity = float("background_opacity", 0.5f, 0f, 1f, 0.01f, 2)
        val borderColor = color("border_color", StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
        val borderOpacity = float("border_opacity", 0f, 0f, 1f, 0.01f, 2)
        val paddingSize = int("padding_size", 5, 0 to 10)
        val lineSpacing = int("line_spacing", 2, 0 to 20)
        val textColor = color("text_color", StaticColorSaver(1f, 1f, 1f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
        val borderRadius = int("border_radius", 0, 0 to 20)
        val shadow = boolean("shadow", true)
        val scale = float("scale", .8f, 0.5f, 2f, 0.01f, 2)
        val gap = int("gap", 1, 0 to 20)
        val screenEdgeDistance = int("screen_edge_distance", 5, 0 to 10)
    }
}