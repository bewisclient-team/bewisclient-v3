package net.bewis09.bewisclient.widget.impl

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.impl.CustomWidgetLineRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.util.string
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component

object CustomWidget : LineWidget(
    createIdentifier("bewisclient", "custom_widget"),
    "Custom Widget",
    "A widget which you can customize in any way you want"
) {
    val customWidgetParamInfo = createTranslation("param_info", "You can include live information in the Custom Widget using curly brackets, e.g. {biome_id}. Some data points can take parameters, which can be specified after a | character. For example, {real_time|seconds} will show the real time including seconds.")

    class WidgetStringData(val id: String, val name: Translation, val description: Translation, val func: (param: String?) -> String, val param: Translation? = null) {
        constructor(id: String, name: String, description: String, func: (param: String?) -> String, param: String? = null) : this(id, createTranslation("data.$id", name), createTranslation("data.$id.description", description), { func(it) }, param?.let { createTranslation("data.$id.param", it) })
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

    override fun getLines(): List<Component> = lines.get().map(::computeLine).map(String::toText)

    fun computeLine(line: String): String {
        return Regex("\\{\\w+(?:\\|\\w+)?}|.+?|").findAll(line).toList().map { a ->
            Regex("\\{(\\w+)(?:\\|(\\w+))?}|.+").findAll(a.value).map { b ->
                val id = b.groupValues.getOrNull(1) ?: return@map b.value.toText()
                val param = b.groupValues.getOrNull(2)

                widgetStringDataPoints.find { a -> a.id == id }?.func(param) ?: b.value
            }.toList()
        }.flatten().joinToString("")
    }

    override fun getMinimumWidth(): Int = minimumWidth.get().toInt()

    override fun getMaximumWidth(): Int = maximumWidth.get().toInt()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.START, SidedPosition.START)

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(minimumWidth.createIntRenderable(this, "minimum_width", "Minimum Width", "The minimum width of the widget"))
        list.add(maximumWidth.createIntRenderable(this, "maximum_width", "Maximum Width", "The maximum width of the widget"))
        list.add(centered.createRenderable(this, "centered", "Centered", "Whether the text should be centered"))
        list.add(CustomWidgetLineRenderable())
        super.appendSettingsRenderables(list)
    }

    override fun isCentered(): Boolean = centered.get()
}