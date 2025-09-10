package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object CustomWidget : LineWidget() {
    val customWidgetTranslation = Translation("widget.custom_widget.name", "Custom Widget")
    val customWidgetDescription = Translation("widget.custom_widget.description", "A widget which you can customize in any way you want")

    val minimumWidth = int("minimum_width", 100, 10, 500)
    val maximumWidth = int("maximum_width", 200, 10, 500)

    val lines = ListSetting(listOf(), from = {
        if (!it.isJsonPrimitive) return@ListSetting null
        return@ListSetting it.asString
    }, to = {
        return@ListSetting JsonPrimitive(it)
    })

    override fun isEnabledByDefault(): Boolean = false

    override fun getLines(): List<String> = lines.get()

    override fun getMinimumWidth(): Int = minimumWidth.get()

    override fun getMaximumWidth(): Int = maximumWidth.get()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.START, SidedPosition.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "custom_widget")

    override fun getTranslation(): Translation = customWidgetTranslation

    override fun getDescription(): Translation = customWidgetDescription
}