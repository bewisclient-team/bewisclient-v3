package net.bewis09.bewisclient.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.settings.types.WidgetPositionSetting
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

abstract class Widget: ObjectSetting() {
    var position: WidgetPositionSetting = WidgetPositionSetting(defaultPosition())

    init {
        create("position", position)
    }

    abstract fun defaultPosition(): WidgetPosition

    abstract fun getId(): Identifier

    fun renderScaled(screenDrawing: ScreenDrawing) {
        screenDrawing.push()
        screenDrawing.translate(getX(), getY())
        screenDrawing.scale(getScale(), getScale())
        render(screenDrawing)
        screenDrawing.pop()
    }

    abstract fun render(screenDrawing: ScreenDrawing)

    fun getScreenWidth(): Int {
        return (MinecraftClient.getInstance().window.scaledWidth)
    }

    fun getScreenHeight(): Int {
        return (MinecraftClient.getInstance().window.scaledHeight)
    }

    fun getScaledWidth(): Float {
        return (getWidth() * getScale())
    }

    fun getScaledHeight(): Float {
        return (getHeight() * getScale())
    }

    abstract fun getWidth(): Int
    abstract fun getHeight(): Int
    open fun getScale() = 1.0f

    fun getX() = catch { position.get().getX(this) } ?: 0f
    fun getY() = catch { position.get().getY(this) } ?: 0f

    abstract fun getTranslation(): Translation
    abstract fun getDescription(): Translation

    open fun appendSettingsRenderables(list: ArrayList<Renderable>) {}

    fun isInBox(mouseX: Double, mouseY: Double) = getX() < mouseX &&
                getX() + getScaledWidth() > mouseX &&
                getY() < mouseY &&
                getY() + getScaledHeight() > mouseY
}