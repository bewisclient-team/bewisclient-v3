package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.renderable.CustomWidgetLineRenderable
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.string
import net.bewis09.bewisclient.util.toText
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.text.Text

object CustomWidget : LineWidget(createIdentifier("bewisclient", "custom_widget")) {
    val customWidgetParamInfo = createTranslation("param_info", "You can include live information in the Custom Widget using curly brackets, e.g. {biome_id}. Some data points can take parameters, which can be specified after a | character. For example, {real_time|seconds} will show the real time including seconds.")

    class WidgetStringData(val id: String, val name: Translation, val description: Translation, val func: (param: String?) -> Text, val param: Translation? = null) {
        constructor(id: String, name: String, description: String, func: (param: String?) -> Text, param: String? = null) : this(id, createTranslation("data.$id", name), createTranslation("data.$id.description", description), { func(it) }, param?.let { createTranslation("data.$id.param", it) })
    }

    val widgetStringDataPoints = APIEntrypointLoader.mapEntrypoint {
        it.getWidgets().map(Widget::getCustomWidgetDataPoints)
    }.flatten().flatten()

    val minimumWidth = float("minimum_width", 100f, 10f, 500f, 10f, -1)
    val maximumWidth = float("maximum_width", 200f, 10f, 500f, 10f, -1)
    val centered = boolean("centered", true)

    val lines = create("lines", ListSetting(listOf(), from = {
        return@ListSetting it.string()
    }, to = {
        return@ListSetting JsonPrimitive(it)
    }))

    override fun isEnabledByDefault(): Boolean = false

    override fun getLines(): List<Text> = lines.get().map(::computeLine)

    fun computeLine(line: String): Text {
        return Regex("\\{\\w+(?:\\|\\w+)?}|.+?|").findAll(line).toList().map { a ->
            Regex("\\{(\\w+)(?:\\|(\\w+))?}|.+").findAll(a.value).map { b ->
                val id = b.groupValues.getOrNull(1) ?: return@map b.value.toText()
                val param = b.groupValues.getOrNull(2)

                widgetStringDataPoints.find { a -> a.id == id }?.func(param)?.copy() ?: b.value.toText()
            }.toList()
        }.flatten().reduceOrNull { a, b -> "".toText().append(a.append(b)) } ?: "".toText()
    }

    override fun getMinimumWidth(): Int = minimumWidth.get().toInt()

    override fun getMaximumWidth(): Int = maximumWidth.get().toInt()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.START, SidedPosition.START)

    override val title = "Custom Widget"
    override val description = "A widget which you can customize in any way you want"

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(minimumWidth.createIntRenderable("widget.minimum_width", "Minimum Width", "The minimum width of the widget"))
        list.add(maximumWidth.createIntRenderable("widget.maximum_width", "Maximum Width", "The maximum width of the widget"))
        list.add(centered.createRenderable("widget.centered", "Centered", "Whether the text should be centered"))
        list.add(CustomWidgetLineRenderable())
        super.appendSettingsRenderables(list)
    }

    override fun isCentered(): Boolean = centered.get()
}