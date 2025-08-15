package net.bewis09.bewisclient.drawable.renderables.hud_edit_screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.minecraft.client.gui.screen.Screen
import kotlin.math.abs

class HudEditScreen: Renderable(), BackgroundEffectProvider {
    var selectedWidget: Widget? = null
    var startOffsetX: Float? = null
    var startOffsetY: Float? = null

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false

        WidgetLoader.widgets.forEach {
            if (it.isInBox(mouseX, mouseY)) {
                selectedWidget = it
                startOffsetX = (mouseX - it.getX()).toFloat()
                startOffsetY = (mouseY - it.getY()).toFloat()
                return true
            }
        }

        return false
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        if (button != 0) return false

        val widget = selectedWidget

        if (widget != null && startOffsetX != null && startOffsetY != null) {
            WidgetLoader.widgets.forEach {
                possibleAppendArea(it, widget, mouseX.toInt(), mouseY.toInt())?.let { a ->
                    widget.position.set(a)
                    return true
                }
            }

            val wX = mouseX - (startOffsetX ?: 0f)
            val wY = mouseY - (startOffsetY ?: 0f)

            widget.position.set(createWidgetSidedPosition(widget, wX, wY))
            return true
        }

        return false
    }

    fun possibleAppendArea(widget: Widget, appendWidget: Widget, mouseX: Int, mouseY: Int): RelativePosition? {
        if (widget == appendWidget) return null

        val sides = arrayOf("top", "right", "bottom", "left")

        sides.forEach { side ->
            val position = RelativePosition(widget.getId().toString(), side)

            val x1 = position.getX(appendWidget)
            val y1 = position.getY(appendWidget)
            val x2 = position.getX(appendWidget) + appendWidget.getScaledWidth()
            val y2 = position.getY(appendWidget) + appendWidget.getScaledHeight()

            if (x1 < mouseX && x2 > mouseX && y1 < mouseY && y2 > mouseY) {
                if (position.isInDependencyStack(appendWidget)) return@forEach

                if (x1 < 0) return@forEach
                if (x2 > getWidth()) return@forEach
                if (y1 < 0) return@forEach
                if (y2 > getWidth()) return@forEach

                val overlaps = WidgetLoader.widgets.any { other ->
                    if (other == appendWidget || other == widget) return@any false
                    val ox1 = other.getX().toInt()
                    val oy1 = other.getY().toInt()
                    val ox2 = ox1 + other.getScaledWidth()
                    val oy2 = oy1 + other.getScaledHeight()
                    x1 < ox2 && x2 > ox1 && y1 < oy2 && y2 > oy1
                }

                if (overlaps) return@forEach

                return position
            }
        }

        return null
    }

    fun createWidgetSidedPosition(widget: Widget, wX: Double, wY: Double): SidedPosition {
        val right = wX + widget.getScaledWidth() / 2 > getWidth() / 2
        val end = wY + widget.getScaledHeight() / 2 > getHeight() / 2

        var x = if (right) getWidth() - wX - widget.getScaledWidth() else wX
        var y = if (end) getHeight() - wY - widget.getScaledHeight() else wY

        var xTransform = if (right) SidedPosition.TransformerType.END else SidedPosition.TransformerType.START
        val yTransform = if (end) SidedPosition.TransformerType.END else SidedPosition.TransformerType.START

        if (!Screen.hasShiftDown()) {
            x = x.coerceAtLeast(5.0)
            y = y.coerceAtLeast(5.0)

            if (abs(wX + widget.getScaledWidth() / 2 - getWidth() / 2) < 10) {
                xTransform = SidedPosition.TransformerType.CENTER
            }
        }

        return SidedPosition(x.toInt(),y.toInt(),xTransform,yTransform)
    }

    override fun getBackgroundEffectFactor(): Float {
        return 0f
    }

    override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false

        selectedWidget = null
        startOffsetX = null
        startOffsetY = null

        return super.onMouseRelease(mouseX, mouseY, button)
    }
}