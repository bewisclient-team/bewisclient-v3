package net.bewis09.bewisclient.drawable.draw_methods

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.logic.ClientInterface

interface DrawMethods: ClientInterface {
    fun renderMenuBackground(screenDrawing: ScreenDrawing, screenWidth: Int, screenHeight: Int)

    fun renderButtonBackground(screenDrawing: ScreenDrawing, hover: Float, animation: Float, x: Int, y: Int, width: Int, height: Int, click: Float, mouseX: Int, mouseY: Int, dark: Boolean = false, small: Boolean = false)

    fun getSideButtonHeight(): Int

    fun renderSwitch(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, stateAnimation: Float, mouseX: Int, mouseY: Int)

    fun renderFader(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, normalizedValue: Float, mouseX: Int, mouseY: Int)

    fun renderSettingsCategoryBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, state: Float, hover: Float, mouseX: Int, mouseY: Int)

    fun renderPopupBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, borderRadius: Int, borderAlpha: Float)
}