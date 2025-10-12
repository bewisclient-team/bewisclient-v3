package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.cosmetics.CosmeticLoader
import net.bewis09.bewisclient.cosmetics.CosmeticType
import net.bewis09.bewisclient.cosmetics.drawable.SelectCapeElement
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.elements.ExtensionListRenderable
import net.bewis09.bewisclient.drawable.renderables.options_structure.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.createIdentifier
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

    val cosmetics = Plane { x, y, width, height ->
        listOf(
            CosmeticLoader.elytra.createRenderable("cosmetics.elytra", "Apply cape to elytra", "Some capes include a unique texture for the elytra, which can be disabled here if desired.")(x, y, width, 22),
            VerticalScrollGrid({
                CosmeticLoader.cosmetics.filter {
                    it.key.type == CosmeticType.CAPE && CosmeticLoader.allowedCosmetics.contains(it.key)
                }.map { a -> SelectCapeElement(a.key, a.value) }
            }, 5, 65)(x, y + 27, width, height - 27)
        )
    }

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

    val widgetsCategory = SidebarCategory(createIdentifier("bewisclient", "widgets"), "Widgets", this.widgetsPlane)
    val utilitiesCategory = SidebarCategory(createIdentifier("bewisclient", "utilities"), "Utilities", this.utilities)
    val settingsCategory = SidebarCategory(createIdentifier("bewisclient", "settings"), "Settings", this.settings)
    val cosmeticsCategory = SidebarCategory(createIdentifier("bewisclient", "cosmetics"), "Cosmetics", this.cosmetics)
    val extensionsCategory = SidebarCategory(createIdentifier("bewisclient", "extensions"), "Extensions", this.extensions)

    val sidebarCategories = arrayListOf<Renderable>(
        widgetsCategory(screen), utilitiesCategory(screen), settingsCategory(screen), cosmeticsCategory(screen), extensionsCategory(screen)
    ).also {
        APIEntrypointLoader.mapEntrypoint { a -> a.getSidebarCategories().forEach { b -> it.add(b(screen)) } }
    }
}