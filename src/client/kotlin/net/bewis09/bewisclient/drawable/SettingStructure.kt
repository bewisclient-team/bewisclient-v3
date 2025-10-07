package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.cosmetics.drawable.SelectCapeElement
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.elements.ExtensionListRenderable
import net.bewis09.bewisclient.drawable.renderables.options_structure.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.color
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.WidgetLoader

class SettingStructure(val screen: OptionScreen) : BewisclientInterface {
    val widgets = WidgetLoader.widgets.map {
        DescriptionSettingCategory(it.widgetTitle, it.widgetDescription, arrayListOf<Renderable>().also { list -> it.appendSettingsRenderables(list) }.toTypedArray(), it.enabled)
    }

    val utilities = APIEntrypointLoader.mapEntrypoint { it.getUtilities() }.flatten()

    val settings = VerticalAlignScrollPlane(
        listOf(
//            OptionsMenuSettings.animationTime.createRenderable("menu.settings.animation_time", "Animation Time", "The time (in milliseconds) it takes for animations to complete"),
            OptionsMenuSettings.blurBackground.createRenderable("menu.settings.blur_background", "Blur Background", "Whether to blur the background when opening menus"),
            OptionsMenuSettings.buttonInTitleScreen.createRenderable("menu.settings.button_in_title_screen", "Button in Title Screen", "Whether to show the Bewisclient button in the title screen"),
            OptionsMenuSettings.buttonInGameScreen.createRenderable("menu.settings.button_in_game_screen", "Button in Game Screen", "Whether to show the Bewisclient button in the in-game pause menu"),
            OptionsMenuSettings.themeColor.createRenderable("menu.settings.theme_color", "Theme Color", "The theme color used throughout the client"),
            OptionsMenuSettings.backgroundColor.createRenderableWithFader("menu.settings.background_color", "Background Color", "The background color used for menus. Reset to use the theme color.", OptionsMenuSettings.backgroundOpacity)
        ), 1
    )

    val cosmetics = InfoTextRenderable(Translation("menu.category.cosmetics.info", "Cosmetics are not yet available in this version of Bewisclient. We are working on making them available again with new features and online support in the new future.").getTranslatedString(), 0xAAAAAA.color, centered = true, selfResize = false)

//    val cosmetics = Plane { x, y, _, _ ->
//        listOf(
//            SelectCapeElement().setPosition(x, y)
//        )
//    }

    val extensions = VerticalAlignScrollPlane(APIEntrypointLoader.mapContainer { ExtensionListRenderable(it.provider, it.entrypoint) }, 1)

    val generalWidgetSettings = APIEntrypointLoader.mapEntrypoint { it.getGeneralWidgetSettings() }.flatten()

    val widgetsPlane = Plane { x, y, width, height ->
        listOf(
            Button(Translation("menu.widgets.general_setting", "General Widget Settings")()) {
                screen.transformInside(
                    TextElement(Translation("menu.widgets.general_setting", "General Widget Settings")(), centered = true).setHeight(12), VerticalAlignScrollPlane({ generalWidgetSettings }, 1)
                )
            }(x, y, width, 14), VerticalScrollGrid({ widgets.map { a -> a.setHeight(90) } }, 5, 80).invoke(x, y + 19, width, height - 19)
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