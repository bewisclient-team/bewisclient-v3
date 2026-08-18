package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.Util
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.renderables.notification.SimpleTextNotification
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import kotlin.math.roundToInt

object Contact : SidebarFeature(
    createIdentifier("bewisclient", "contact"), "Contact"
) {
    var hoveredElement: ContactLinkElement? = null

    override fun getRenderable(): Renderable {
        return VerticalAlignScrollPlane(
            listOf(
                ContactLinkElement("modrinth", Constants.MODRINTH_URL, "Modrinth", "The official download page of Bewisclient on Modrinth."),
                ContactLinkElement("github", Constants.GITHUB_URL, "GitHub", "The source code of Bewisclient is available on GitHub."),
                ContactLinkElement("issues", "${Constants.GITHUB_URL}/issues", "Issue Tracker", "Report bugs or request features on our GitHub issue tracker."),
                ContactLinkElement("discord", Constants.DISCORD_URL, "Discord", "Join our Discord server to chat with the community and get support."),
            ), 1
        )
    }

    class ContactLinkElement(val id: String, val url: String, val title: String, val description: String) : SettingRenderable(null, 22) {
        companion object {
            val copyToClipboardText = createTranslation("copy_to_clipboard", "Copy to clipboard")
            val openLinkText = createTranslation("open_link", "Open link in browser")
            val copyLinkSuccessText = createTranslation("copy_link_success", "Copied link to clipboard")
        }

        val titleTranslation = createTranslation("$id.title", title)
        val descriptionTranslation = createTranslation("$id.description", description)
        val identifier = createIdentifier("bewisclient", "textures/gui/contact/$id.png")

        val menuAnimation = Animator({ animationDuration }, Animator.EASE_IN_OUT, 0f)

        var simpleHeight = 22

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)
            screenDrawing.enableScissors(x, y, width, height)
            screenDrawing.push()
            screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
            screenDrawing.drawText(titleTranslation.getTranslatedString(), x + 32, y, Color.WHITE)
            val lines = screenDrawing.drawWrappedText(descriptionTranslation.getTranslatedString(), x + 32, y + 10, width - 40, 0xAAAAAA alpha 0.8f)
            screenDrawing.pop()
            screenDrawing.drawTexture(identifier, x + 8, y + height / 2 - 8, 0f, 0f, 16, 16, 16, 16)
            renderRenderables(screenDrawing, mouseX, mouseY)
            simpleHeight = 22 + lines.size * 9 + 1
            setHeight(simpleHeight + (menuAnimation.get() * (5 + SelectiveScreenDrawer.getSideButtonHeight())).roundToInt())
            screenDrawing.disableScissors()
        }

        override fun init() {
            super.init()
            addRenderable(ThemeButton(copyToClipboardText()) {
                client.keyboardHandler.clipboard = this.url
                NotificationManager.addNotification(SimpleTextNotification(copyLinkSuccessText()))
            }(x + width - 210, y + simpleHeight, 100, SelectiveScreenDrawer.getSideButtonHeight()))
            addRenderable(ThemeButton(openLinkText()) {
                Util.getPlatform().openUri(url)
            }(x + width - 105, y + simpleHeight, 100, SelectiveScreenDrawer.getSideButtonHeight()))
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            menuAnimation.set(1f)
            hoveredElement?.menuAnimation?.set(0f)
            hoveredElement = if (hoveredElement != this) this else null
            return true
        }
    }
}