// @VersionReplacement

package net.bewis09.bewisclient.api.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.common.EntityTypes
import net.bewis09.bewisclient.common.id
import net.bewis09.bewisclient.drawable.ImageIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.features.sidebar.Contact
import net.bewis09.bewisclient.features.cosmetics.CosmeticLoader
import net.bewis09.bewisclient.features.sidebar.Extensions
import net.bewis09.bewisclient.features.sidebar.Screenshot
import net.bewis09.bewisclient.features.sidebar.Utilities
import net.bewis09.bewisclient.features.utilities.*
import net.bewis09.bewisclient.features.utilities.TntTimer.FuseProvider
import net.bewis09.bewisclient.game.BewisclientCommand
import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.game.ShulkerBoxTooltipComponent
import net.bewis09.bewisclient.game.Ticker
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.game.keybinds.KeybindingImplementer
import net.bewis09.bewisclient.game.keybinds.OpenOptionScreen
import net.bewis09.bewisclient.game.translations.TranslationLoader
import net.bewis09.bewisclient.server.Authorization
import net.bewis09.bewisclient.server.AutoUpdater
import net.bewis09.bewisclient.server.Security
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.features.sidebar.Home
import net.bewis09.bewisclient.features.sidebar.Widgets
import net.bewis09.bewisclient.settings.logic.Settings
import net.bewis09.bewisclient.settings.structure.CategorizedFeature
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.bewisclient.widget.impl.*
import kotlin.jvm.optionals.getOrNull

class BewisclientSelfAPIEntrypoint : BewisclientAPIEntrypoint() {
    override fun getEventEntrypoints(): List<EventEntrypoint> = listOf(
        WidgetLoader, Settings, KeybindingImplementer, TranslationLoader, BiomeWidget, SpeedWidget, TiwylaWidget, ShulkerBoxTooltipComponent.Entrypoint, CosmeticLoader, BewisclientCommand, Security, ImageIdentifier, Panorama, Ticker, AutoUpdater, Authorization
    )

    override fun getKeybinds(): List<Keybind> = listOf(
        OpenOptionScreen, Fullbright.ToggleNightVision, Fullbright.ToggleFullbright, Fullbright.IncreaseBrightness, Fullbright.DecreaseBrightness, Zoom.ZoomKeybind, Perspective.EnablePerspective, Panorama.TakePanoramaScreenshot
    )

    override fun getWidgets(): List<Widget> = listOf(
        CoordinatesWidget, FPSWidget, BiomeWidget, DayWidget, DaytimeWidget, PingWidget, CPSWidget, KeyWidget, InventoryWidget, SpeedWidget, TiwylaWidget, CustomWidget, ServerWidget, ArmorWidget
    )

    override fun getUtilities(): List<CategorizedFeature> = listOf(
        Fullbright, BlockHighlight, EntityHighlight, HeldItemTooltip, Zoom, PumpkinOverlay, BetterVisibility, Scoreboard, ShulkerBoxTooltip, Perspective, FireHeight, Panorama, TntTimer, PackAdder
    )

    override fun getOtherSettings(): List<Feature> {
        return listOf(Home)
    }

    override fun getGeneralWidgetSettings(): List<Renderable> = listOf(
        Widgets.Default.gap.createRenderable(General, "gap", "Gap", "Set the gap between widgets in a row"),
        Widgets.Default.screenEdgeDistance.createRenderable(General, "screen_edge_distance", "Screen Edge Distance", "Set the snapping distance of a widget to the screen edge"),
        Widgets.Default.backgroundColor.createRenderableWithFader(General, "default_background", "Default Background", "Set the default color and opacity of a widget", Widgets.Default.backgroundOpacity),
        Widgets.Default.borderColor.createRenderableWithFader(General, "default_border", "Default Border", "Set the default color and opacity of a widget's border", Widgets.Default.borderOpacity),
        Widgets.Default.paddingSize.createRenderable(General, "default_padding_size", "Default Padding Size", "Set the default padding at the edge of a widget to the text"),
        Widgets.Default.lineSpacing.createRenderable(General, "default_line_spacing", "Default Line Spacing", "Set the default spacing between lines of text in a widget"),
        Widgets.Default.shadow.createRenderable(General, "default_text_shadow", "Default Text Shadow", "Set whether text in a widget has a shadow by default"),
        Widgets.Default.textColor.createRenderable(General, "default_text_color", "Default Text Color", "Set the default color of the text in a widget"),
        Widgets.Default.borderRadius.createRenderable(General, "default_border_radius", "Default Border Radius", "Set the default radius of a widget's border corners"),
        Widgets.Default.scale.createRenderable(General, "default_scale", "Default Scale", "Set the default scale of a widget"),
    )

    override fun getSidebarCategories(): List<SidebarFeature> = listOf(
        Widgets, Utilities, General, CosmeticLoader, Extensions, Screenshot, Contact
    )

    override fun getTiwylaEntityExtraInfoProviders(): List<TiwylaWidget.EntityInfoProvider<*>> = listOf(
        TiwylaWidget.EntityInfoProvider(EntityTypes.CAT) { it.variant.unwrapKey().getOrNull()?.id()?.toString() },
        TiwylaWidget.EntityInfoProvider(EntityTypes.FROG) { it.variant.unwrapKey().getOrNull()?.id()?.toString() },
        TiwylaWidget.EntityInfoProvider(EntityTypes.AXOLOTL) { it.variant.name },
        TiwylaWidget.EntityInfoProvider(EntityTypes.HORSE) { it.markings.name.lowercase() + ", " + it.variant.name.lowercase() },
        TiwylaWidget.EntityInfoProvider(EntityTypes.RABBIT) { it.variant.name.lowercase() },
        TiwylaWidget.EntityInfoProvider(EntityTypes.LLAMA) { it.variant.name.lowercase() },
    )

    override fun getCustomResourceProviders(): List<BewisclientResourcePack.CustomResourceProvider> = listOf(
        Panorama
    )

    override fun getTntTimerEntities() = listOf(
        FuseProvider(EntityTypes.TNT, true) { entity -> entity.fuse },
        FuseProvider(EntityTypes.TNT_MINECART, false) { entity -> entity.fuse },
        // @[26.1] @[] FuseProvider(EntityTypes.SULFUR_CUBE, false) { entity -> entity.fuse }
        /*[@]*/FuseProvider(EntityTypes.SULFUR_CUBE, false) { entity -> entity.fuse }/*[!@]*/
    )
}