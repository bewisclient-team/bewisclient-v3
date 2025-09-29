package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.core.CoreUtil
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.screen.RenderableScreen

/**
 * The entrypoint for the Bewisclient widget events.
 * This is used to register widget-related events in the Bewisclient.
 */
object WidgetLoader : EventEntrypoint {
    val widgets: MutableList<Widget> = mutableListOf()

    override fun onInitializeClient() {
        APIEntrypointLoader.mapEntrypoint {
            widgets.addAll(it.getWidgets())
        }

        widgets.forEach {
            CoreUtil.registerWidget(
                it.id
            ) { context ->
                if (it.isShowing() && (client.currentScreen as? RenderableScreen)?.renderable !is HudEditScreen) it.renderScaled(ScreenDrawing(context, client.textRenderer))
            }
        }
    }

    fun getEnabledWidgets(): List<Widget> {
        return widgets.filter { it.isEnabled() }
    }
}
