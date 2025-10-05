package net.bewis09.bewisclient.drawable.renderables.elements

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.color
import net.bewis09.bewisclient.logic.createIdentifier
import net.bewis09.bewisclient.logic.setColor
import net.bewis09.bewisclient.logic.toText
import net.fabricmc.loader.api.ModContainer
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

class ExtensionListRenderable(val modContainer: ModContainer, val entrypoint: BewisclientAPIEntrypoint) : SettingRenderable(null, 22) {
    val notFoundIdentifier: Identifier = createIdentifier("textures/misc/unknown_pack.png")

    val menuAnimation = animate(OptionsMenuSettings.animationTime.get().toLong(), Animator.EASE_IN_OUT, "menu" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(("${entrypoint.getExtensionTitle(modContainer)} ").toText().append(("(${modContainer.metadata.id})").toText().setColor(0xAAAAAA)), x + 32, y, Color.WHITE)
        val lines = screenDrawing.drawWrappedText(entrypoint.getExtensionDescription(modContainer), x + 32, y + 10, width - 40, 0xAAAAAA.color alpha 0.8f)
        screenDrawing.pop()
        screenDrawing.drawTexture(entrypoint.getIcon(modContainer) ?: notFoundIdentifier, x + 8, centerY - 8, 0f, 0f, 16, 16, 16, 16)
        internalHeight = 22 + lines.size * 9 + 1 + (menuAnimation["menu"] * 19).roundToInt()
    }
}