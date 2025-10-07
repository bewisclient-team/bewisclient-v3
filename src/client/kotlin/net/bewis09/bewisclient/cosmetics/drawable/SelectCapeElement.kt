package net.bewis09.bewisclient.cosmetics.drawable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.within

class SelectCapeElement: Renderable() {
    init {
        this.internalWidth = 60
        this.internalHeight = 130
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fill(x, y, width, height - 18, OptionsMenuSettings.themeColor.get().getColor() alpha 0.1f)
        client.player?.let {
            screenDrawing.drawEntity(x, y, x + width, y + height - 20, 50, 1.0f, mouseX.toFloat(), mouseY.toFloat(), it)
        }
        screenDrawing.drawBorder(x, y, width, height - 18, 0.5f within (Color.BLACK to OptionsMenuSettings.themeColor.get().getColor()))
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(Button(Translations.SELECT(), {}, selected = { true })(x, y + height - 14, width, 14))
    }
}