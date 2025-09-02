package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.drawable.SettingStructure
import net.bewis09.bewisclient.drawable.renderables.ImageButton
import net.bewis09.bewisclient.drawable.renderables.popup.AddWidgetPopup
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.logic.hoverSeparate
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.bewisclient.widget.WidgetLoader.widgets
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import kotlin.math.abs

class HudEditScreen : PopupScreen(), BackgroundEffectProvider {
    companion object {
        val scrollToZoom = Translation("hud_edit_screen.scroll_to_zoom", "Scroll to Zoom (%.2fx)")
        val rightClickForOptions = Translation("hud_edit_screen.right_click_for_options", "Right Click for Options")
        val shiftForNoSnapping = Translation("hud_edit_screen.shift_for_no_snapping", "Shift to disable Snapping")
    }

    val mouseMap: HashMap<Int, Boolean> = hashMapOf()

    var selectedWidget: Widget? = null
    var startOffsetX: Float? = null
    var startOffsetY: Float? = null

    val removeTexture: Identifier = Identifier.of("bewisclient", "textures/gui/sprites/remove.png")

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        mouseMap[button] = true

        WidgetLoader.getEnabledWidgets().forEach {
            if (it.isInBox(mouseX, mouseY)) {
                hoverSeparate(mouseX.toFloat(), mouseY.toFloat(), (it.getX() + it.getScaledWidth() - 8).toInt(), (it.getY()).toInt(), 8, 8, {}) {
                    if (button == 0) {
                        it.enabled.set(false)

                        return true
                    }
                }

                if (button == 1) {
                    getClient().setScreen(RenderableScreen(OptionScreen().also { a ->
                        val widgetsCategory = SettingStructure(a).widgets.first { b -> b.enableSetting == it.enabled }
                        a.optionsHeader = widgetsCategory.getHeader()
                        a.optionsPane = widgetsCategory.getPane()
                        a.optionsHeaderBooleanSetting = it.enabled
                    }))

                    return true
                }

                selectedWidget = it
                startOffsetX = (mouseX - it.getX()).toFloat()
                startOffsetY = (mouseY - it.getY()).toFloat()
                return true
            }
        }

        return false
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int, popupShown: Boolean) {
        widgets.forEach {
            if (it.isEnabled()) {
                it.renderScaled(ScreenDrawing(screenDrawing.drawContext, MinecraftClient.getInstance().textRenderer))

                hoverSeparate(mouseX.toFloat(), mouseY.toFloat(), (it.getX() + it.getScaledWidth() - 8).toInt(), (it.getY()).toInt(), 8, 8, {
                    screenDrawing.pushColor(1f, 1f, 1f, 1f)
                }) {
                    screenDrawing.pushColor(1f, 0f, 0f, 1f)
                }

                screenDrawing.drawTexture(removeTexture, (it.getX() + it.getScaledWidth() - 8).toInt(), (it.getY()).toInt(), 8, 8)

                screenDrawing.popColor()
            }
        }
        renderRenderables(screenDrawing, mouseX, mouseY)
        renderTooltip(screenDrawing, mouseX, mouseY)
    }

    fun renderTooltip(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        WidgetLoader.getEnabledWidgets().asReversed().forEach {
            if (it.isInBox(mouseX.toDouble(), mouseY.toDouble())) {
                screenDrawing.setBewisclientFont()

                val lines = mutableListOf<String>()
                lines.add(it.getTranslation().getTranslatedString())
                lines.add("")
                if (it is ScalableWidget) {
                    lines.add(scrollToZoom(Precision(0.5f, 2f, 0.01f, 2).roundToString(it.scale.get())).string)
                }
                lines.add(rightClickForOptions().string)
                lines.add(shiftForNoSnapping().string)

                val textHeight = screenDrawing.getTextHeight()
                val tooltipHeight = lines.size * textHeight + 10
                val width = lines.maxOfOrNull { line -> screenDrawing.getTextWidth(line) }?.plus(10) ?: 210

                var drawX = mouseX
                var drawY = mouseY - tooltipHeight

                if (drawX + width > MinecraftClient.getInstance().window.scaledWidth) {
                    drawX -= width
                }
                if (drawY < 0) {
                    drawY = mouseY
                }

                screenDrawing.afterDraw("tooltip", {
                    screenDrawing.fillRounded(drawX, drawY, width, tooltipHeight, 5, 0x000000, 0.8f)
                    screenDrawing.drawWrappedText(lines, drawX + 5, drawY + 5, -1)
                })
            }
        }
    }

    override fun init() {
        addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/add.png")) {
            openPopup(AddWidgetPopup(this), 0xA0000000.toInt())
        }.setImagePadding(0)(getWidth() - 16, getHeight() - 16, 14, 14))
        addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/settings.png")) {
            getClient().setScreen(RenderableScreen(OptionScreen().also {
                val widgetsCategory = SettingStructure(it).widgetsCategory
                it.optionsHeader = widgetsCategory.getHeader()
                it.optionsPane = widgetsCategory.renderable
            }))
        }.setImagePadding(2)(getWidth() - 32, getHeight() - 16, 14, 14))
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        if (button != 0) return false

        val widget = selectedWidget

        if (widget != null && startOffsetX != null && startOffsetY != null) {
            WidgetLoader.getEnabledWidgets().forEach {
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

                val overlaps = WidgetLoader.getEnabledWidgets().any { other ->
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

        if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().window, GLFW.GLFW_MOD_SHIFT)) {
            if (abs(x - DefaultWidgetSettings.screenEdgeDistance.get()) < 10) {
                x = DefaultWidgetSettings.screenEdgeDistance.get().toDouble()
            }

            if (abs(y - DefaultWidgetSettings.screenEdgeDistance.get()) < 10) {
                y = DefaultWidgetSettings.screenEdgeDistance.get().toDouble()
            }

            if (abs(wX + widget.getScaledWidth() / 2 - getWidth() / 2) < 10) {
                xTransform = SidedPosition.TransformerType.CENTER
            }
        }

        return SidedPosition(x.toInt(), y.toInt(), xTransform, yTransform)
    }

    override fun getBackgroundEffectFactor(): Float {
        return 0f
    }

    override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int): Boolean {
        mouseMap[button] = false

        if (button != 0) return false

        selectedWidget = null
        startOffsetX = null
        startOffsetY = null

        return super.onMouseRelease(mouseX, mouseY, button)
    }

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        WidgetLoader.getEnabledWidgets().forEach {
            if (it.isInBox(mouseX, mouseY) && it is ScalableWidget) {
                val newScale = it.scale.get() + (if (verticalAmount > 0) 0.1f else -0.1f)
                it.scale.set(newScale.coerceIn(0.5f, 2f))
                return true
            }
        }

        return super.onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            getClient().setScreen(RenderableScreen(OptionScreen()))
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }
}