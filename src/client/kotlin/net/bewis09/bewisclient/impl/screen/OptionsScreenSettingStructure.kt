package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.ScrollGrid
import net.bewis09.bewisclient.drawable.renderables.ImageSettingCategory
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.WidgetLoader
import kotlin.collections.listOf

class OptionsScreenSettingStructure(val screen: OptionScreen): BewisclientInterface {
    val utilities = listOf(
        ImageSettingCategory("fullbright", Translation("menu.category.fullbright", "Fullbright"), arrayOf(
            getSettings().fullbright.enabled.createRenderable("fullbright.enabled", "Fullbright", "Enable or disable fullbright functionality"),
            getSettings().fullbright.brightness.createRenderable("fullbright.brightness", "Brightness", "Adjust the brightness level. 0.0 to 1.0 are the normal levels, while 1.0 to 15.0 is lighting up the world according to the brightness level"),
            getSettings().fullbright.nightVision.createRenderable("fullbright.night_vision", "Night Vision", "Allows you to have the visual effect of night vision without actually having it"),
        )),
        ImageSettingCategory("block_highlight", Translation("menu.category.block_highlight", "Block Highlight"), arrayOf(
//            getSettings()
        )),
        ImageSettingCategory("entity_highlight", Translation("menu.category.entity_highlight", "Entity Highlight"), arrayOf()),
        ImageSettingCategory("held_item_info", Translation("menu.category.held_item_info", "Held Item Info"), arrayOf()),
        ImageSettingCategory("zoom", Translation("menu.category.zoom", "Zoom"), arrayOf()),
        ImageSettingCategory("pumpkin_overlay", Translation("menu.category.pumpkin_overlay", "Pumpkin Overlay"), arrayOf()),
        ImageSettingCategory("crosshair", Translation("menu.category.crosshair", "Crosshair"), arrayOf()),
        ImageSettingCategory("better_visibility", Translation("menu.category.better_visibility", "Better Visibility"), arrayOf()),
        ImageSettingCategory("scoreboard", Translation("menu.category.scoreboard", "Scoreboard"), arrayOf()),
        ImageSettingCategory("cleaner_debug_menu", Translation("menu.category.cleaner_debug_menu", "Cleaner Debug Menu"), arrayOf()),
        ImageSettingCategory("chat_enhancements", Translation("menu.category.chat_enhancements", "Chat Enhancements"), arrayOf()),
    )

    val widgets = WidgetLoader.widgets.map {
        DescriptionSettingCategory(it.getTranslation(), it.getDescription(), arrayListOf<Renderable>().also { list -> it.appendSettingsRenderables(list) }.toTypedArray())
    }

    val settings = listOf<ImageSettingCategory>(

    )

    val cosmetics = listOf<ImageSettingCategory>(

    )

    val extensions = listOf<ImageSettingCategory>(

    )

    val contact = listOf<ImageSettingCategory>(

    )

    val defaultWidgetSettings = listOf(
        getSettings().widgetSettings.defaults.paddingSize.createRenderable("widget.padding_size", "Padding Size"),
        getSettings().widgetSettings.defaults.lineSpacing.createRenderable("widget.line_spacing", "Line Spacing"),
        getSettings().widgetSettings.defaults.borderRadius.createRenderable("widget.border_radius", "Border Radius"),
        getSettings().widgetSettings.defaults.scale.createRenderable("widget.scale", "Scale"),
        getSettings().widgetSettings.defaults.gap.createRenderable("widget.gap", "Gap"),
    )

    val widgetsPlane = Plane { x, y, width, height ->
        listOf(
            Button(Translation("menu.widgets.default_widget_settings", "Default Widget Settings").getTranslatedString()) {
                screen.transformInside(
                    Text(Translation("menu.widgets.default_widget_settings", "Default Widget Settings").getTranslatedString(), centered = true).setHeight(12),
                    VerticalAlignScrollPlane({ defaultWidgetSettings }, 5)
                )
            }(x, y, width, 14),
            ScrollGrid({ widgets.map { a -> a.setHeight(90) } }, 5, 80).invoke(x, y + 19, width, height - 19)
        )
    }

    val sidebarCategories = listOf(
        createSidebarCategory(Translation("menu.category.widgets", "Widgets"), widgetsPlane),
        createSidebarCategory(Translation("menu.category.utilities", "Utilities"), utilities),
        createSidebarCategory(Translation("menu.category.settings", "Settings"), settings),
        createSidebarCategory(Translation("menu.category.cosmetics", "Cosmetics"), cosmetics),
        createSidebarCategory(Translation("menu.category.extensions", "Extensions"), extensions),
        createSidebarCategory(Translation("menu.category.contact", "Contact"), contact)
    )

    fun createSidebarCategory(name: Translation, settings: List<Renderable>): Button {
        return Button(name.getTranslatedString()) {
            screen.transformInside(
                Text(name.getTranslatedString(), centered = true).setHeight(12),
                ScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80)
            )
        }.setHeight(14) as Button
    }

    fun createSidebarCategory(name: Translation, renderable: Renderable): Button {
        return Button(name.getTranslatedString()) {
            screen.transformInside(
                Text(name.getTranslatedString(), centered = true).setHeight(12),
                renderable
            )
        }.setHeight(14) as Button
    }
}