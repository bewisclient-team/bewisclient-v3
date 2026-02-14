package net.bewis09.bewisclient.cosmetics.drawable

import net.bewis09.bewisclient.core.drawCape
import net.bewis09.bewisclient.cosmetics.Cosmetic
import net.bewis09.bewisclient.cosmetics.CosmeticIdentifier
import net.bewis09.bewisclient.cosmetics.CosmeticLoader
import net.bewis09.bewisclient.cosmetics.CosmeticType
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.within
import net.bewis09.bewisclient.util.createIdentifier

class SelectCapeElement(val identifier: CosmeticIdentifier, val cosmetic: Cosmetic) : TooltipHoverable() {
    val selected = Animator(200, Animator.EASE_IN_OUT, "selected" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        selected["selected"] = if (CosmeticLoader.selected[CosmeticType.CAPE.id] == identifier.id) 1f else 0f

        screenDrawing.fillRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = selected["selected"] * 0.3f + hoverAnimation["hovering"] * 0.15f + 0.1f))

        screenDrawing.drawCape(cosmetic.getIdentifier(), x + 8, y + 8, width - 16, height - 25)
        screenDrawing.drawCenteredText(`snake_toWord With Spaces`(identifier.id), x + width / 2, y + height - 13, OptionsMenuSettings.getTextThemeColor())

        if (CosmeticLoader.elytraCosmetics.contains(identifier)) {
            screenDrawing.fillWithBorderRounded(x + width - 20, y, 20, 20, 5, 0.3f within (Color.BLACK to OptionsMenuSettings.getThemeColor()), 0.2f within ((selected["selected"] within (Color.DARK_GRAY to Color.WHITE)) to OptionsMenuSettings.getThemeColor()), topLeft = false, bottomRight = false)
            screenDrawing.drawTexture(createIdentifier("textures/item/elytra.png"), x + width - 18, y + 2, 16, 16)
        }

        screenDrawing.drawBorderRounded(x, y, width, height, 5, 0.2f within ((selected["selected"] within (Color.DARK_GRAY to Color.WHITE)) to OptionsMenuSettings.getThemeColor()))
    }

    @Suppress("FunctionName")
    private fun `snake_toWord With Spaces`(str: String): String {
        return str.split("_".toRegex()).filter { it.isNotEmpty() }.joinToString(" ") {
            it.replaceFirstChar(Char::uppercaseChar)
        }
    }

    override fun init() {
        super.init()
        internalHeight = (width - 16) * 16 / 10 + 25
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false
        CosmeticLoader.selected[CosmeticType.CAPE.id] = if (CosmeticLoader.selected[CosmeticType.CAPE.id] == identifier.id) null else identifier.id
        return true
    }
}