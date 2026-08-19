package net.bewis09.bewisclient.drawable.draw_methods

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.darken
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.drawable.screen_drawing.translate

object MinecraftyMethods : DrawMethods {
    override fun renderMenuBackground(screenDrawing: ScreenDrawing, screenWidth: Int, screenHeight: Int) {
        renderMenuBackground(screenDrawing, 30, 30, 134, screenHeight - 60)
        renderMenuBackground(screenDrawing, 169, 30, screenWidth - 199, screenHeight - 60)
    }

    fun renderWithBorders(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, backgroundColor: Color, borderColors: Array<Color>, brightness: Float = 1f) {
        screenDrawing.darken(brightness) {
            screenDrawing.fill(x + borderColors.size, y + borderColors.size, width - borderColors.size * 2, height - borderColors.size * 2, backgroundColor)
            borderColors.forEachIndexed { index, color ->
                screenDrawing.drawBorder(x + index, y + index, width - 2 * index, height - 2 * index, color)
            }
        }
    }

    fun renderMenuBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int) {
        renderWithBorders(
            screenDrawing, x, y, width, height, 0x151515 alpha 0.9f, arrayOf(
                !0x222222,
                0x484848 alpha 0.9f,
                !0x222222
            )
        )
    }

    override fun renderButtonBackground(screenDrawing: ScreenDrawing, hover: Float, animation: Float, x: Int, y: Int, width: Int, height: Int, click: Float, mouseX: Int, mouseY: Int, dark: Boolean, small: Boolean) {
        renderWithBorders(
            screenDrawing, x, y, width, height, hover within (!0x333333 to !0x444444), arrayOf(
                if (small) null else !0x222222,
                hover within (!0x5B5B5B to !0xA1A1A1),
                !0x282828
            ).filterNotNull().toTypedArray(), (if (dark) 0.6f else 1f) * (1f - 0.4f * animation)
        )
    }

    override fun renderSettingRenderableBackground(screenDrawing: ScreenDrawing, hover: Float, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int) {
        renderButtonBackground(screenDrawing, hover, 0f, x, y, width, height, 1f, mouseX, mouseY)
    }

    override fun getSideButtonHeight(): Int = 18

    override fun renderSwitch(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, stateAnimation: Float, mouseX: Int, mouseY: Int) {
        screenDrawing.pointerIfWithin(x, y, width, height, mouseX, mouseY)
        renderWithBorders(
            screenDrawing, x + 2, y + 2, width - 4, height - 4, !0x333333, arrayOf(
                !0x434343,
                !0x282828
            )
        )
        screenDrawing.transform(x + ((width - 12) * stateAnimation), y + 0f, 1f, 1f) {
            renderWithBorders(
                screenDrawing, 0, 0, 12, 12, hover within (!0x888888 to !0x999999), arrayOf(
                    !0x626262,
                    Color.WHITE,
                    !0x282828
                ), 0.5f + hover * 0.2f
            )
        }
    }

    override fun renderFader(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, hover: Float, normalizedValue: Float, mouseX: Int, mouseY: Int) {
        screenDrawing.pointerIfWithin(x, y, width, height, mouseX, mouseY)
        screenDrawing.translate(0f, 0.5f) {
            renderWithBorders(
                screenDrawing, x, y + 4, width, 5, !0x333333, arrayOf(
                    !0x434343,
                    !0x282828
                )
            )
        }
        screenDrawing.transform(x + ((width - 10) * normalizedValue), y + 2f, 1f, 1f) {
            renderWithBorders(
                screenDrawing, 0, 0, 10, 10, hover within (!0x888888 to !0x999999), arrayOf(
                    !0x626262,
                    Color.WHITE,
                    !0x282828
                ), 0.5f + hover * 0.2f
            )
        }
    }

    override fun renderSettingsCategoryBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, state: Float, hover: Float, mouseX: Int, mouseY: Int) {
        renderWithBorders(
            screenDrawing, x, y, width, height, hover within (!0x333333 to !0x444444), arrayOf(
                !0x222222,
                hover within (!0x5B5B5B to !0xA1A1A1),
                !0x282828
            ), 0.5f + 0.5f * state
        )
    }

    override fun renderPopupBackground(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, borderRadius: Int, borderAlpha: Float) {
        renderButtonBackground(
            screenDrawing, 0f, 0f, x, y, width, height, 0f, mouseX = -1, mouseY = -1, dark = true
        )
    }
}