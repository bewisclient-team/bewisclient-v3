package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object CPSWidget : LineWidget() {
    val leftEnabled: BooleanSetting = boolean("left_enabled", true) { _, new -> if (new == false) rightEnabled.set(true) }
    val rightEnabled: BooleanSetting = boolean("right_enabled", true) { _, new -> if (new == false) leftEnabled.set(true) }

    val cpsWidgetTranslation = Translation("widget.cps_widget.name", "CPS Widget")
    val cpsWidgetDescription = Translation(
        "widget.cps_widget.description", "Displays your current clicks per second (CPS)."
    )

    val leftMouseList = mutableListOf<Long>()
    val rightMouseList = mutableListOf<Long>()

    override fun getTranslation(): Translation = cpsWidgetTranslation
    override fun getDescription(): Translation = cpsWidgetDescription

    override fun getLines(): List<String> {
        if (!rightEnabled.get()) return listOf("${getCPSCount(leftMouseList)} CPS")
        if (!leftEnabled.get()) return listOf("${getCPSCount(rightMouseList)} CPS")

        return listOf("${getCPSCount(leftMouseList)} | ${getCPSCount(rightMouseList)} CPS")
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:day_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "cps_widget")

    override fun getMinimumWidth(): Int = 80

    fun getCPSCount(list: MutableList<Long>): Int {
        val currentTime = System.currentTimeMillis()
        list.removeIf { it < currentTime - 1000 }
        return list.size
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            leftEnabled.createRenderable(
                "widget.cps_widget.left_enabled", "Left Mouse Button CPS Shown"
            )
        )
        list.add(
            rightEnabled.createRenderable(
                "widget.cps_widget.right_enabled", "Right Mouse Button CPS Shown"
            )
        )
        super.appendSettingsRenderables(list)
    }
}
