package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.version.Profiler
import net.bewis09.bewisclient.version.registerWidget

/**
 * The entrypoint for the Bewisclient widget events.
 * This is used to register widget-related events in the Bewisclient.
 */
object WidgetLoader : ObjectSetting(), EventEntrypoint {
    val widgets: List<Widget> = APIEntrypointLoader.mapEntrypoint(BewisclientAPIEntrypoint::getWidgets).flatten().apply {
        forEach { create(it.id.toString(), it) }
    }

    override fun onInitializeClient() {
        widgets.forEach {
            registerWidget(
                it.id
            ) { context ->
                Profiler.push("widget_" + it.id.toString().replace(":", "_"))
                if (it.isShowing() && getCurrentRenderableScreen()?.renderable !is HudEditScreen) {
                    it.renderScaled(ScreenDrawing(context, client.font))
                }
                Profiler.pop()
            }
        }
    }

    fun getEnabledWidgets(): List<Widget> {
        return widgets.filter { it.enabled }
    }
}
