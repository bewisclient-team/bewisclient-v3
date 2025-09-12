package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object CustomWidget : LineWidget(Identifier.of("bewisclient", "custom_widget")) {
    class WidgetStringData(val id: String, val name: Translation, val description: Translation, val func: (param: String?) -> Text, val param: String? = null) {
        constructor(id: String, name: String, description: String, func: (param: String?) -> Text, param: String? = null) : this(id, createTranslation("data.$id", name), createTranslation("data.$id.description", description), { func(it) }, param)
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

    override fun getLines(): List<Text> = lines.get().mapNotNull {
        Regex("\\{\\w+(?:\\|\\w+)?}|.+?|").findAll(it).toList().map { a ->
            Regex("\\{(\\w+)(?:\\|(\\w+))?}|.+").findAll(a.value).map { b ->
                val id = b.groupValues.getOrNull(1) ?: return@map b.value.toText()
                val param = b.groupValues.getOrNull(2)

                widgetStringDataPoints.find { a -> a.id == id }?.func(param)?.copy() ?: b.value.toText()
            }.toList()
        }.flatten().reduce { a, b -> a.append(b) }
    }

    override fun getMinimumWidth(): Int = minimumWidth.get()

    override fun getMaximumWidth(): Int = maximumWidth.get()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.START, SidedPosition.START)

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