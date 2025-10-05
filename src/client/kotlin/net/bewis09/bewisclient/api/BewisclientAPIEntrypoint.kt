package net.bewis09.bewisclient.api

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.options_structure.SettingCategory
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.util.*
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.Widget
import net.fabricmc.loader.api.ModContainer
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

/**
 * The Bewisclient API entrypoint interface.
 * It is used to provide access to the Bewisclient API and register extensions for the Bewisclient mod.
 *
 * To add your own Bewisclient API entrypoint, implement this interface in your mod.
 * Then register your entrypoint in your `fabric.mod.json` file under `custom`, `bewisclient`
 */
open class BewisclientAPIEntrypoint : BewisclientInterface {
    val iconIdentifier: Identifier? = null

    open fun getExtensionTitle(modContainer: ModContainer): String = modContainer.metadata.name

    open fun getExtensionDescription(modContainer: ModContainer): String = modContainer.metadata.description

    open fun getIcon(modContainer: ModContainer): Identifier? {
        return iconIdentifier ?: modContainer.metadata.getIconPath(64).getOrNull()?.let {
            modContainer.findPath(it).getOrNull()?.let { path ->
                createTexture(createIdentifier(modContainer.metadata.id, "extension_icon_${(1..99999).random()}"), path.toUri().toURL())
            }
        }
    }

    /**
     * Returns a list of [EventEntrypoint]s that are registered in the mod.
     * This is used to register event handlers for the Bewisclient API.
     *
     * @return A list of [EventEntrypoint]s that are registered in the mod.
     */
    open fun getEventEntrypoints(): List<EventEntrypoint> {
        return emptyList()
    }

    /**
     * Returns a list of [Settings] objects that are registered in the mod.
     * This is used to register settings for the Bewisclient API.
     * Each [Settings] object should be a singleton that holds the settings for your mod.
     *
     * @return A list of [Settings] objects that are registered in the mod.
     */
    open fun getSettingsObjects(): List<Settings> {
        return emptyList()
    }

    /**
     * Returns a list of [Keybind]s that are registered in the mod.
     * This is used to register keybinds for the Bewisclient API.
     *
     * @return A list of [Keybind]s that are registered in the mod.
     */
    open fun getKeybinds(): List<Keybind> {
        return emptyList()
    }

    /**
     * Returns a list of [Widget]s that are registered in the mod.
     * This is used to register widgets for the Bewisclient API.
     *
     * @return A list of [Widget]s that are registered in the mod.
     */
    open fun getWidgets(): List<Widget> {
        return emptyList()
    }

    /**
     * Should return a list of [Renderable]s that are displayed in the Bewisclient utilities tab.
     * Those should preferably be a subclass of [SettingCategory]
     */
    open fun getUtilities(): List<Renderable> {
        return emptyList()
    }

    /**
     * Should return a list of the sidebar categories that are displayed in the Bewisclient options screen.
     */
    open fun getSidebarCategories(): List<SidebarCategory> {
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
}