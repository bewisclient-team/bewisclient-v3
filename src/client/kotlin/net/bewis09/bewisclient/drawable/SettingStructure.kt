package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.options_structure.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.options_structure.SettingCategory
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.WidgetLoader

class SettingStructure(val screen: OptionScreen): BewisclientInterface {
    val widgets = WidgetLoader.widgets.map {
        DescriptionSettingCategory(it.getTranslation(), it.getDescription(), arrayListOf<Renderable>().also { list -> it.appendSettingsRenderables(list) }.toTypedArray(), it.enabled)
    }

    val utilities = APIEntrypointLoader.mapEntrypoint { it.getUtilities() }.flatten()

    val settings = emptyList<SettingCategory>()

    val cosmetics = emptyList<SettingCategory>()

    val extensions = emptyList<SettingCategory>()

    val generalWidgetSettings = APIEntrypointLoader.mapEntrypoint { it.getGeneralWidgetSettings() }.flatten()

    val widgetsPlane = Plane { x, y, width, height ->
        listOf(
            Button(Translation("menu.widgets.general_setting", "General Widget Settings").getTranslatedString()) {
                screen.transformInside(
                    Text(Translation("menu.widgets.general_setting", "General Widget Settings").getTranslatedString(), centered = true).setHeight(12),
                    VerticalAlignScrollPlane({ generalWidgetSettings }, 5)
                )
            }(x, y, width, 14),
            VerticalScrollGrid({ widgets.map { a -> a.setHeight(90) } }, 5, 80).invoke(x, y + 19, width, height - 19)
        )
    }

    val widgetsCategory = SidebarCategory(Translation("menu.category.widgets", "Widgets"), this.widgetsPlane)
    val utilitiesCategory = SidebarCategory(Translation("menu.category.utilities", "Utilities"), this.utilities)
    val settingsCategory = SidebarCategory(Translation("menu.category.settings", "Settings"), this.settings)
    val cosmeticsCategory = SidebarCategory(Translation("menu.category.cosmetics", "Cosmetics"), this.cosmetics)
    val extensionsCategory = SidebarCategory(Translation("menu.category.extensions", "Extensions"), this.extensions)

    val sidebarCategories = arrayListOf<Renderable>(
        widgetsCategory(screen), utilitiesCategory(screen), settingsCategory(screen), cosmeticsCategory(screen), extensionsCategory(screen)
    ).also {
        APIEntrypointLoader.mapEntrypoint { a -> a.getSidebarCategories().forEach { b -> it.add(b(screen)) } }
    }
}