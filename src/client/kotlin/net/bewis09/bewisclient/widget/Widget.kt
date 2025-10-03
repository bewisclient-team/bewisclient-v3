package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.widget.CustomWidget
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.settings.types.WidgetPositionSetting
import net.bewis09.bewisclient.widget.logic.WidgetPosition

abstract class Widget(val id: BewisclientID) : ObjectSetting() {
    var position: WidgetPositionSetting = create("position", WidgetPositionSetting(defaultPosition()))
    var enabled = boolean("enabled", isEnabledByDefault())

    protected abstract val title: String
    protected abstract val description: String

    val widgetTitle by lazy { createTranslation("name", title) }
    val widgetDescription by lazy { createTranslation("description", description) }

    fun createTranslation(key: String, @Suppress("LocalVariableName") en_us: String) = Translation(id.namespace, "widget.${id.path}.$key", en_us)

    open fun isEnabledByDefault(): Boolean = true

    open fun isHidden(): Boolean = false

    fun isShowing(): Boolean {
        return isEnabled() && (!isHidden() || ((client.currentScreen as? RenderableScreen)?.renderable is HudEditScreen))
    }

    fun isEnabled(): Boolean = enabled.get()

    abstract fun defaultPosition(): WidgetPosition

    fun renderScaled(screenDrawing: ScreenDrawing) {
        screenDrawing.transform(getX(), getY(), getScale()) {
            catch { render(screenDrawing) } ?: run {
                error("Error rendering widget $id - disabling it to prevent further errors")
                enabled.set(false)
            }
        }
    }

    abstract fun render(screenDrawing: ScreenDrawing)

    fun getScreenWidth(): Int = client.window.scaledWidth
    fun getScreenHeight(): Int = client.window.scaledHeight
    fun getScaledWidth(): Float = getWidth() * getScale()
    fun getScaledHeight(): Float = getHeight() * getScale()

    abstract fun getWidth(): Int
    abstract fun getHeight(): Int
    open fun getScale() = 1.0f

    fun getX(): Float {
        val x = catch { position.get().getX(this) } ?: 0f
        return x.coerceAtLeast(0f).coerceAtMost(getScreenWidth() - getScaledWidth())
    }

    fun getY(): Float {
        val y = catch { position.get().getY(this) } ?: 0f
        return y.coerceAtLeast(0f).coerceAtMost(getScreenHeight() - getScaledHeight())
    }

    open fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf()

    open fun appendSettingsRenderables(list: ArrayList<Renderable>) {}

    fun isInBox(mouseX: Double, mouseY: Double) = getX() < mouseX && getX() + getScaledWidth() > mouseX && getY() < mouseY && getY() + getScaledHeight() > mouseY
}
