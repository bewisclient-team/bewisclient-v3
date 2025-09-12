package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object CustomWidget : LineWidget() {
    class WidgetStringData(val id: String, val name: Translation, val description: Translation, val func: (param: String?) -> String, val param: String? = null) {
        constructor(id: String, name: String, description: String, func: (param: String?) -> Any, param: String? = null) : this(id, Translation("widget.custom_widget.data.$id", name), Translation("widget.custom_widget.data.$id.description", description), { func(it).toString() }, param )
    }

    val widgetStringDataPoints = APIEntrypointLoader.mapEntrypoint {
        it.getWidgets().map(Widget::getCustomWidgetDataPoints)
    }.flatten().flatten()

    val minimumWidth = int("minimum_width", 100, 10, 500)
    val maximumWidth = int("maximum_width", 200, 10, 500)
    val centered = boolean("centered", true)

    val lines = create("lines", ListSetting(listOf(), from = {
        if (!it.isJsonPrimitive) return@ListSetting null
        return@ListSetting it.asString
    }, to = {
        return@ListSetting JsonPrimitive(it)
    }))

    override fun isEnabledByDefault(): Boolean = false

    override fun getLines(): List<String> = lines.get().map {
        Regex("\\{([a-z_]+)(?:\\|([\\w-]+))?}").replace(it) { matchResult ->
            if(matchResult.groups.isEmpty()) return@replace matchResult.value
            val id = matchResult.groups[1] ?: return@replace matchResult.value
            val param = if (matchResult.groups.size >= 3) matchResult.groups[2] else null

            widgetStringDataPoints.firstOrNull { a -> a.id == id.value }?.func(param?.value) ?: matchResult.value
        }
    }

    override fun getMinimumWidth(): Int = minimumWidth.get()

    override fun getMaximumWidth(): Int = maximumWidth.get()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.START, SidedPosition.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "custom_widget")

    override val title = "Custom Widget"
    override val description = "A widget which you can customize in any way you want"

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(minimumWidth.createRenderable("widget.minimum_width", "Minimum Width", "The minimum width of the widget"))
        list.add(maximumWidth.createRenderable("widget.maximum_width", "Maximum Width", "The maximum width of the widget"))
        list.add(centered.createRenderable("widget.centered", "Centered", "Whether the text should be centered"))
        super.appendSettingsRenderables(list)
    }

    override fun isCentered(): Boolean = centered.get()
}