package net.bewis09.bewisclient.api

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.features.utilities.TntTimer
import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.settings.structure.CategorizedFeature
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.BewisclientDataGenerator
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.fabricmc.loader.api.ModContainer
import kotlin.jvm.optionals.getOrNull

/**
 * The Bewisclient API entrypoint interface.
 * It is used to provide access to the Bewisclient API and register extensions for the Bewisclient mod.
 *
 * To add your own Bewisclient API entrypoint, implement this interface in your mod.
 * Then register your entrypoint in your `fabric.mod.json` file under `custom`, `bewisclient`
 */
open class BewisclientAPIEntrypoint : ClientInterface {
    val iconIdentifier: Identifier? = null

    open fun getExtensionTitle(modContainer: ModContainer): String = modContainer.metadata.name

    open fun getExtensionDescription(modContainer: ModContainer): String = modContainer.metadata.description

    open fun getIcon(modContainer: ModContainer): Identifier? {
        if (BewisclientDataGenerator.datagenEnabled) return null
        return iconIdentifier ?: modContainer.metadata.getIconPath(64).getOrNull()?.let {
            modContainer.findPath(it).getOrNull()?.let { path ->
                createTexture(createIdentifier(modContainer.metadata.id, "extension_icon_${(1..99999).random()}"), path.toUri().toURL())
            }
        }
    }

    open fun getEventEntrypoints(): List<EventEntrypoint> {
        return emptyList()
    }

    /**
     * Returns a list of [Keybind]s that are registered in the mod.
     * This is used to register keybinds for the Bewisclient API.
     */
    open fun getKeybinds(): List<Keybind> {
        return emptyList()
    }

    open fun getWidgets(): List<Widget> {
        return emptyList()
    }

    /**
     * Should return a list of [CategorizedFeature]s that are displayed in the Bewisclient utilities tab.
     */
    open fun getUtilities(): List<CategorizedFeature> {
        return emptyList()
    }

    /**
     * Should return a map of other settings that are not part of a utility or a widget.
     * This should preferably be a very general ObjectSetting, not individual settings
     */
    open fun getOtherSettings(): List<Feature> {
        return emptyList()
    }

    /**
     * Should return a list of the sidebar categories that are displayed in the Bewisclient options screen.
     */
    open fun getSidebarCategories(): List<SidebarFeature> {
        return emptyList()
    }

    /**
     * Should return a list of [Renderable]s that are settings for multiple widgets so you can change the default/general settings for all widgets at once.
     */
    open fun getGeneralWidgetSettings(): List<Renderable> {
        return emptyList()
    }

    /**
     * Should return a list of extra [TiwylaWidget.EntityInfoProvider]s that provide extra information to be displayed in the Tiwyla widget for specific entities.
     */
    open fun getTiwylaEntityExtraInfoProviders(): List<TiwylaWidget.EntityInfoProvider<*>> {
        return emptyList()
    }

    /**
     * Should return a list of [TntTimer.FuseProvider]s that provide entities for the TNT Timer feature.
     */
    open fun getTntTimerEntities(): List<TntTimer.FuseProvider<*>> {
        return emptyList()
    }

    /**
     * Should return a list of [BewisclientResourcePack.CustomResourceProvider]s that are used to provide custom resources for the Bewisclient.
     */
    open fun getCustomResourceProviders(): List<BewisclientResourcePack.CustomResourceProvider> {
        return emptyList()
    }
}