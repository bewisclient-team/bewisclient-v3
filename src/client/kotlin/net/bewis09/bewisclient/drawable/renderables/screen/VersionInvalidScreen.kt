package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.common.Util
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.renderables.components.button.MinecraftButton
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen.Companion.modrinthButtonText
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing.Companion.DEFAULT_FONT
import net.bewis09.bewisclient.server.Security
import net.bewis09.bewisclient.version.setScreen
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.TextAlign
import net.minecraft.network.chat.CommonComponents
import org.lwjgl.glfw.GLFW

object VersionInvalidScreen : PopupScreen() {
    const val SECURITY_MESSAGE =
        "Your version of Bewisclient could not be verified. This probably means that the file your are using was changed after downloading or the version you are using was removed from Modrinth due to a critical bug.\n\nPlease download the newest version from Modrinth to ensure you are using a safe version.\n\nIf you believe this is an error, please let us know on GitHub."

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.wrapText((SECURITY_MESSAGE + "\n\nError message: ${(Security.verificationState as? Security.ILLEGAL)?.reason ?: "Unknown"}").toText(), 300).let {
            screenDrawing.drawWrappedText(it, width / 2, height / 2 - it.size * 9 / 2 - 30) {
                textAlign = TextAlign.CENTER
                color = Color.WHITE
                font = DEFAULT_FONT
                shadow = true
            }
        }
    }

    override fun init() {
        Text {
            text = (SECURITY_MESSAGE + "\n\nError message: ${(Security.verificationState as? Security.ILLEGAL)?.reason ?: "Unknown"}").toText()
            textAlign = TextAlign.CENTER
            color = Color.WHITE
            font = DEFAULT_FONT
            shadow = true
            paddingBottom = 60
            width = 300
        }.updateX(width / 2 - 150)
        MinecraftButton {
            text = CommonComponents.GUI_BACK
            onClick = { setScreen(null) }
        }(width / 2 - 102, height / 2 + 50, 100, 20)
        MinecraftButton {
            text = modrinthButtonText()
            onClick = { Util.getPlatform().openUri(Constants.MODRINTH_URL) }
        }(width / 2 + 2, height / 2 + 50, 100, 20)
        VersionText()
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            setScreen(null)
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }
}