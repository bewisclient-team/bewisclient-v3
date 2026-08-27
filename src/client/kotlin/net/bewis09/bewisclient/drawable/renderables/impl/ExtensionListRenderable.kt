package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.common.*
import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.color
import net.fabricmc.loader.api.ModContainer
import kotlin.math.roundToInt

class ExtensionListRenderable(p: Props<ExtensionListRenderable>) : SettingRenderable<ExtensionListRenderable>(p + { height = 22 }) {
    lateinit var modContainer: ModContainer
    lateinit var entrypoint: BewisclientAPIEntrypoint

    init { props() }

    val notFoundIdentifier: Identifier = createIdentifier("textures/misc/unknown_pack.png")

    val menuAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 0f)

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.push()
        screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(("${entrypoint.getExtensionTitle(modContainer)} ").toText().append(("(${modContainer.metadata.id})").toText().setColor(0xAAAAAA)), x + 32, y, Color.WHITE)
        val lines = screenDrawing.drawWrappedText(entrypoint.getExtensionDescription(modContainer), x + 32, y + 10, width - 40, 0xAAAAAA.color alpha 0.8f)
        screenDrawing.pop()
        screenDrawing.drawTexture(entrypoint.getIcon(modContainer) ?: notFoundIdentifier, x + 8, centerY - 8, 0f, 0f, 16, 16, 16, 16)
        height = 22 + lines.size * 9 + 1 + (menuAnimation.get() * 19).roundToInt()
    }
}