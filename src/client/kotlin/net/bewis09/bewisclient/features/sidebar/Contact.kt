package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.common.Util
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.renderables.notification.SimpleTextNotification
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.alpha
import kotlin.math.roundToInt

object Contact : SidebarFeature(
    createIdentifier("bewisclient", "contact"), "Contact"
) {
    var hoveredElement: ContactLinkElement? = null

    override fun getRenderable(): Renderable {
        return Grid {
            gap = 1
            fitType = Grid.FitType.SCROLL
            children = listOf(
                ContactLinkElement {
                    id = "modrinth"
                    url = Constants.MODRINTH_URL
                    title = "Modrinth"
                    description = "The official download page of Bewisclient on Modrinth."
                },
                ContactLinkElement {
                    id = "github"
                    url = Constants.GITHUB_URL
                    title = "GitHub"
                    description = "The source code of Bewisclient is available on GitHub."
                },
                ContactLinkElement {
                    id = "issues"
                    url = "${Constants.GITHUB_URL}/issues"
                    title = "Issue Tracker"
                    description = "Report bugs or request features on our GitHub issue tracker."
                },
                ContactLinkElement {
                    id = "discord"
                    url = Constants.DISCORD_URL
                    title = "Discord"
                    description = "Join our Discord server to chat with the community and get support."
                }
            )
        }
    }

    class ContactLinkElement(p: Props<ContactLinkElement>) : SettingRenderable<ContactLinkElement>(p + {
        height = 22
    }) {
        lateinit var id: String
        lateinit var url: String
        lateinit var title: String
        lateinit var description: String

        init { props() }

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
            screenDrawing.push()
            screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
            screenDrawing.drawText(titleTranslation.getTranslatedString(), x + 32, y, Color.WHITE)
            val lines = screenDrawing.drawWrappedText(descriptionTranslation.getTranslatedString(), x + 32, y + 10, width - 40, 0xAAAAAA alpha 0.8f)
            screenDrawing.pop()
            screenDrawing.drawTexture(identifier, x + 8, y + height / 2 - 8, 0f, 0f, 16, 16, 16, 16)
            renderRenderables(screenDrawing, mouseX, mouseY)
            simpleHeight = 22 + lines.size * 9 + 1
            updateHeight(simpleHeight + (menuAnimation.get() * (5 + SelectiveScreenDrawer.getSideButtonHeight())).roundToInt())
        }

        override fun init() {
            super.init()
            addRenderable(ThemeButton {
                text = copyToClipboardText()
                onClick = {
                    client.keyboardHandler.clipboard = this@ContactLinkElement.url
                    NotificationManager.addNotification(SimpleTextNotification { text = copyLinkSuccessText() })
                }
            }(x + width - 210, y + simpleHeight, 100, SelectiveScreenDrawer.getSideButtonHeight()))
            addRenderable(ThemeButton {
                text = openLinkText()
                onClick = { Util.getPlatform().openUri(url) }
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