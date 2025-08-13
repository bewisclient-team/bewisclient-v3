package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.option_screen.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.option_screen.SettingCategory
import net.bewis09.bewisclient.drawable.renderables.option_screen.SidebarCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.WidgetLoader

class SettingStructure(val screen: OptionScreen): BewisclientInterface {
    val widgets = WidgetLoader.widgets.map {
        DescriptionSettingCategory(it.getTranslation(), it.getDescription(), arrayListOf<Renderable>().also { list -> it.appendSettingsRenderables(list) }.toTypedArray())
    }

    val utilities = APIEntrypointLoader.mapEntrypoint { it.getUtilities() }.flatten()

    val settings = emptyList<SettingCategory>()

    val cosmetics = emptyList<SettingCategory>()

    val extensions = emptyList<SettingCategory>()

    val defaultWidgetSettings = APIEntrypointLoader.mapEntrypoint { it.getDefaultWidgetSettings() }.flatten()

    val widgetsPlane = Plane { x, y, width, height ->
        listOf(
            Button(Translation("menu.widgets.default_widget_settings", "Default Widget Settings").getTranslatedString()) {
                screen.transformInside(
                    Text(Translation("menu.widgets.default_widget_settings", "Default Widget Settings").getTranslatedString(), centered = true).setHeight(12),
                    VerticalAlignScrollPlane({ defaultWidgetSettings }, 5)
                )
            }(x, y, width, 14),
            VerticalScrollGrid({ widgets.map { a -> a.setHeight(90) } }, 5, 80).invoke(x, y + 19, width, height - 19)
        )
    }

    val sidebarCategories = arrayListOf<Renderable>(
        SidebarCategory(Translation("menu.category.widgets", "Widgets"), widgetsPlane)(screen),
        SidebarCategory(Translation("menu.category.utilities", "Utilities"), utilities)(screen),
        SidebarCategory(Translation("menu.category.settings", "Settings"), settings)(screen),
        SidebarCategory(Translation("menu.category.cosmetics", "Cosmetics"), cosmetics)(screen),
        SidebarCategory(Translation("menu.category.extensions", "Extensions"), extensions)(screen),
    ).also {
        APIEntrypointLoader.mapEntrypoint { a -> a.getSidebarCategories().forEach { b -> it.add(b(screen)) } }
    }
}