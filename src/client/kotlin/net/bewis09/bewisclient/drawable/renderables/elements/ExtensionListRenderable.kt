package net.bewis09.bewisclient.drawable.renderables.elements

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.alpha
import net.bewis09.bewisclient.logic.color.color
import net.fabricmc.loader.api.ModContainer
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.awt.Color
import kotlin.math.roundToInt

class ExtensionListRenderable(val modContainer: ModContainer, val entrypoint: BewisclientAPIEntrypoint) : SettingRenderable() {
    val notFoundIdentifier: Identifier = Identifier.of("textures/misc/unknown_pack.png")

    val menuAnimation = animate(OptionsMenuSettings.animationTime.get().toLong(), Animator.EASE_IN_OUT, "menu" to 0f)

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(Text.literal("${entrypoint.getExtensionTitle(modContainer)} ").append(Text.literal("(${modContainer.metadata.id})").formatted(Formatting.GRAY)), getX() + 32, getY(), Color.WHITE)
        val lines = screenDrawing.drawWrappedText(entrypoint.getExtensionDescription(modContainer), getX() + 32, getY() + 10, getWidth() - 40, 0xAAAAAA.color alpha 0.8f)
        screenDrawing.pop()
        screenDrawing.drawTexture(entrypoint.getIcon(modContainer) ?: notFoundIdentifier, getX() + 8, getY() + getHeight() / 2 - 8, 0f, 0f, 16, 16, 16, 16)
        setHeight(22 + lines.size * 9 + 1 + (menuAnimation["menu"] * 19).roundToInt())
    }
}