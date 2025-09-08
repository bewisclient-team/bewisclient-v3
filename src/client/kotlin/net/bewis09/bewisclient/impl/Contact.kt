package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import kotlin.math.roundToInt

object Contact : SidebarCategory(
    Translation("menu.category.contact", "Contact"), VerticalAlignScrollPlane(
        listOf(
            ContactLinkElement("modrinth", Constants.MODRINTH_URL, "Modrinth", "The official download page of Bewisclient on Modrinth."),
            ContactLinkElement("github", Constants.GITHUB_URL, "GitHub", "The source code of Bewisclient is available on GitHub."),
            ContactLinkElement("issues", "${Constants.GITHUB_URL}/issues", "Issue Tracker", "Report bugs or request features on our GitHub issue tracker."),
            ContactLinkElement("discord", Constants.DISCORD_URL, "Discord", "Join our Discord server to chat with the community and get support."),
        ), 1
    )
) {
    var hoveredElement: ContactLinkElement? = null
}

class ContactLinkElement(val id: String, val url: String, val title: String, val description: String) : SettingRenderable() {
    companion object {
        val COPY_TO_CLIPBOARD = Translation("contact.copy_to_clipboard", "Copy to clipboard")
        val OPEN_LINK = Translation("contact.open_link", "Open link in browser")
    }

    val titleTranslation = Translation("contact.$id.title", title)
    val descriptionTranslation = Translation("contact.$id.description", description)
    val identifier: Identifier = Identifier.of("bewisclient", "textures/gui/contact/$id.png")

    val menuAnimation = animate(OptionsMenuSettings.animationTime.get().toLong(), Animator.EASE_IN_OUT, "menu" to 0f)

    val copyButton = ThemeButton(COPY_TO_CLIPBOARD.getTranslatedString()) {
        client.keyboard.clipboard = this.url
    }.setSize(100, 14)
    val openButton = ThemeButton(OPEN_LINK.getTranslatedString()) {
        Util.getOperatingSystem().open(url)
    }.setSize(100, 14)

    var simpleHeight = 22

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.enableScissors(getX(), getY(), getWidth(), getHeight())
        screenDrawing.push()
        screenDrawing.translate(0f, 11 - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(titleTranslation.getTranslatedString(), getX() + 32, getY(), 0xFFFFFF, 1.0F)
        val lines = screenDrawing.drawWrappedText(descriptionTranslation.getTranslatedString(), getX() + 32, getY() + 10, getWidth() - 40, 0xAAAAAA, 0.8f)
        screenDrawing.pop()
        screenDrawing.drawTexture(identifier, getX() + 8, getY() + getHeight() / 2 - 8, 0f, 0f, 16, 16, 16, 16)
        renderRenderables(screenDrawing, mouseX, mouseY)
        simpleHeight = 22 + lines.size * 9 + 1
        setHeight(simpleHeight + (menuAnimation["menu"] * 19).roundToInt())
        screenDrawing.disableScissors()
    }

    override fun init() {
        super.init()
        addRenderable(copyButton.setPosition(getX() + getWidth() - 210, getY() + simpleHeight))
        addRenderable(openButton.setPosition(getX() + getWidth() - 105, getY() + simpleHeight))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        menuAnimation["menu"] = 1f
        Contact.hoveredElement?.let { it.menuAnimation["menu"] = 0f }
        Contact.hoveredElement = if (Contact.hoveredElement != this) this else null
        return true
    }
}