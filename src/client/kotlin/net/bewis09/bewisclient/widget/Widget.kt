package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.catch
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.transform
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.structure.DescriptionFeature
import net.bewis09.bewisclient.settings.types.WidgetPositionSetting
import net.bewis09.bewisclient.widget.impl.CustomWidget
import net.bewis09.bewisclient.widget.logic.WidgetPosition

abstract class Widget(id: Identifier, title: String, description: String) : DescriptionFeature(id, title, description) {
    var position: WidgetPositionSetting = create("position", WidgetPositionSetting(defaultPosition()))

    override fun createTranslation(key: String, @Suppress("LocalVariableName") en_us: String) = Translation(id.namespace, "widget.${id.path}.$key", en_us)

    open fun isEnabledByDefault(): Boolean = true

    override val enabledByDefault: Boolean
        get() = isEnabledByDefault()

    open fun isHidden(): Boolean = false

    fun isShowing(): Boolean {
        return this@Widget.enabled && (!isHidden() || (getCurrentRenderableScreen()?.renderable is HudEditScreen))
    }

    abstract fun defaultPosition(): WidgetPosition

    fun renderScaled(screenDrawing: ScreenDrawing) {
        screenDrawing.transform(getX(), getY(), getScale()) {
            try {
                render(screenDrawing)
            } catch (e: Exception) {
                error("Error rendering widget $id - disabling it to prevent further errors")
                e.printStackTrace()
                enabled = false
            }
        }
    }

    abstract fun render(screenDrawing: ScreenDrawing)

    fun getScaledWidth(): Float = getWidth() * getScale()
    fun getScaledHeight(): Float = getHeight() * getScale()

    abstract fun getWidth(): Int
    abstract fun getHeight(): Int
    open fun getScale() = 1.0f

    fun getX(): Float {
        val x = catch { position.get().getX(this) } ?: 0f
        return x.coerceAtLeast(0f).coerceAtMost(screenWidth - getScaledWidth())
    }

    fun getY(): Float {
        val y = catch { position.get().getY(this) } ?: 0f
        return y.coerceAtLeast(0f).coerceAtMost(screenHeight - getScaledHeight())
    }

    open fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf()

    fun isInBox(mouseX: Double, mouseY: Double) = getX() < mouseX && getX() + getScaledWidth() > mouseX && getY() < mouseY && getY() + getScaledHeight() > mouseY
}
