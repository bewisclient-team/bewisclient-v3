package net.bewis09.bewisclient.drawable.draw_methods

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.features.sidebar.General
import kotlin.math.abs

object FlatMethods : DrawMethods {
    override fun renderMenuBackground(screenDrawing: ScreenDrawing, screenWidth: Int, screenHeight: Int) {
        screenDrawing.fillWithBorderRounded(30, 30, 134, screenHeight - 60, 5, General.getBackgroundColor(), General.getThemeColor(alpha = 0.3f))
        screenDrawing.fillWithBorderRounded(169, 30, screenWidth - 199, screenHeight - 60, 5, General.getBackgroundColor(), General.getThemeColor(alpha = 0.3f))
    }

    override fun renderButtonBackground(screenDrawing: ScreenDrawing, hover: Float, animation: Float, x: Int, y: Int, width: Int, height: Int, click: Float, mouseX: Int, mouseY: Int, dark: Boolean, small: Boolean) {
        screenDrawing.transform(x + width / 2f, y + height / 2f, 0.9f + 0.1f * click, 0.9f + 0.1f * click) {
            screenDrawing.translate(-width / 2f, -height / 2f)
            screenDrawing.fillWithBorderRounded(0, 0, width, height, 5, General.getThemeColor(alpha = (hover.coerceAtLeast(animation) + 1) * (if (dark) 0.05f else 0.15f)), General.getThemeColor(alpha = animation * 0.5f))
        }
    }

    override fun getSideButtonHeight(): Int = 14

    override fun renderSwitch(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, stateAnimation: Float, mouseX: Int, mouseY: Int) {
        screenDrawing.fillWithBorderRounded(
            x, y, width, height, 6, stateAnimation within (!0x333333 to General.getThemeColor()) alpha hover.coerceAtLeast(stateAnimation) * 0.15f + 0.15f, stateAnimation within (!0x888888 to General.getThemeColor()) alpha hover * 0.5f + 0.5f
        )
        val scaleFactor = 0.5f
        screenDrawing.transform(x + ((width - 12) * stateAnimation) + 6f, y + 6f, 1 - scaleFactor + abs(stateAnimation - 0.5f) * 2 * scaleFactor, 1f) {
            screenDrawing.fillRounded(
                -4, -4, 8, 8, 4, stateAnimation within (!0x888888 to General.getThemeColor()) alpha hover * 0.5f + 0.5f
            )
        }
    }

    override fun renderFader(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, normalizedValue: Float, mouseX: Int, mouseY: Int) {
        screenDrawing.fillRounded(
            x, y + 5, width, 4, 2, 0xAAAAAA alpha hover * 0.15f + 0.15f
        )

        screenDrawing.transform(x + normalizedValue * (width - 8) + 4, y + 2f, 0.1f) {
            screenDrawing.fillRounded(
                -20, 0, 40, 100, 20, (hover within (!0xCCCCCC to !0xFFFFFF)) * General.getThemeColor()
            )
        }
    }

    override fun renderSettingsCategoryBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, state: Float, hover: Float, mouseX: Int, mouseY: Int) {
        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, General.getThemeColor(black = (state + 0.5f) / 1.5f, alpha = hover * 0.15f + 0.15f), General.getThemeColor(alpha = hover * 0.15f + 0.15f))
    }

    override fun renderPopupBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, borderRadius: Int, borderAlpha: Float) {
        screenDrawing.fillWithBorderRounded(x, y, width, height, borderRadius, General.getBackgroundColor(), General.getThemeColor(alpha = borderAlpha))
    }
}