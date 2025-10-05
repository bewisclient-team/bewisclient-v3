package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.core.Profiler
import net.bewis09.bewisclient.core.registerWidget
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.EventEntrypoint
import net.minecraft.client.MinecraftClient

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
            registerWidget(
                it.id
            ) { context ->
                Profiler.push("widget_"+it.id.toString().replace(":", "_"))
                if (it.isShowing() && util.getCurrentRenderableScreen()?.renderable !is HudEditScreen) {
                    it.renderScaled(ScreenDrawing(context, MinecraftClient.getInstance().textRenderer))
                }
                Profiler.pop()
            }
        }
    }

    fun getEnabledWidgets(): List<Widget> {
        return widgets.filter { it.isEnabled() }
    }
}
