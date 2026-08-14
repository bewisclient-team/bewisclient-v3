package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component

object CPSWidget : LineWidget(
    createIdentifier("bewisclient", "cps_widget"),
    "CPS Widget",
    "Displays your current clicks per second (CPS)."
) {
    val singleCPSText = createTranslation("singular_cps", "%s CPS")
    val multiCPSText = createTranslation("multiple_cps", "%s | %s CPS")

    val leftEnabled: BooleanSetting = boolean("left_enabled", true) { _, new -> if (new == false) rightEnabled.set(true) }
    val rightEnabled: BooleanSetting = boolean("right_enabled", true) { _, new -> if (new == false) leftEnabled.set(true) }

    val leftMouseList = mutableListOf<Long>()
    val rightMouseList = mutableListOf<Long>()

    override fun getLine(): Component {
        if (!rightEnabled.get()) return singleCPSText(getCPSCount(leftMouseList))
        if (!leftEnabled.get()) return singleCPSText(getCPSCount(rightMouseList))

        return multiCPSText(getCPSCount(leftMouseList), getCPSCount(rightMouseList))
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition(DayWidget, BOTTOM)

    override fun getMinimumWidth(): Int = 80

    fun getCPSCount(list: MutableList<Long>): Int {
        val currentTime = System.currentTimeMillis()
        list.removeIf { it < currentTime - 1000 }
        return list.size
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.addRenderableQS(leftEnabled, "Show Left Mouse Button CPS")
        list.addRenderableQS(rightEnabled, "Show Right Mouse Button CPS")
        super.appendSettingsRenderables(list)
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("cps_left", "Left CPS", "Your current left clicks per second", { getCPSCount(leftMouseList).toText() }),
        CustomWidget.WidgetStringData("cps_right", "Right CPS", "Your current right clicks per second", { getCPSCount(rightMouseList).toText() }),
        CustomWidget.WidgetStringData("cps_total", "Total CPS", "Your current total clicks per second", { (getCPSCount(leftMouseList) + getCPSCount(rightMouseList)).toText() }),
    )
}
