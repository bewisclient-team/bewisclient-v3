package net.bewis09.bewisclient.game.translations

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.renderables.popup.AddWidgetPopup
import net.bewis09.bewisclient.drawable.renderables.popup.TiwylaLinesSettingsPopup
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.screen.PackListScreen
import net.bewis09.bewisclient.features.cosmetics.AcceptPrivacyPage
import net.bewis09.bewisclient.features.cosmetics.EnableOnlineModeSettingsRenderable
import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.server.Modrinth
import net.bewis09.bewisclient.settings.structure.CategorizedFeature
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.color.colors

@Suppress("unusedExpression")
object TranslationLoader : EventEntrypoint {
    override fun onDatagen() {
        OptionScreen()
        colors
        AddWidgetPopup
        HudEditScreen
        TiwylaLinesSettingsPopup
        Modrinth
        APIEntrypointLoader.mapEntrypointForList(BewisclientAPIEntrypoint::getWidgets).forEach(CategorizedFeature::getSettingRenderables)
        APIEntrypointLoader.mapEntrypointForList(BewisclientAPIEntrypoint::getUtilities).forEach(CategorizedFeature::getSettingRenderables)
        APIEntrypointLoader.mapEntrypointForList(BewisclientAPIEntrypoint::getSidebarCategories).forEach(SidebarFeature::getRenderable)
        BewisclientResourcePack
        PackListScreen.Companion
        AcceptPrivacyPage
        EnableOnlineModeSettingsRenderable.Companion
    }

    override fun onMinecraftClientInitFinished() {
        onDatagen()
    }
}