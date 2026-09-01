package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.renderables.components.setting.InputElement
import net.bewis09.bewisclient.drawable.renderables.components.button.MinecraftButton
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.renderables.notification.ProgressNotification
import net.bewis09.bewisclient.drawable.renderables.notification.SimpleTextNotification
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.server.Modrinth
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.version.setScreen
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.color
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.writeBytes

class PackListScreen(p: Props<PackListScreen>) : PropedRenderable<PackListScreen>(p) {
    lateinit var type: Modrinth.Type
    lateinit var parent: Screen
    lateinit var folder: Path

    init { props() }

    private var index = 0
    private var hasLoaded = false
    private var lastTyped = Long.MAX_VALUE

    val search = InputElement {
        font = ScreenDrawing.DEFAULT_FONT
        maxTextLength = 200
        onChange = { lastTyped = System.currentTimeMillis() }
    }

    var query: String = search.text

    companion object {
        val downloadFromModrinthText = Translation("menu.pack.download_from_modrinth", "Select and download packs from Modrinth")
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawHorizontalLine(centerX - 150, 47, 300, Color.WHITE alpha (51 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, y2 - 32, 300, Color.WHITE alpha (51 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, 48, 300, Color.BLACK alpha (191 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, y2 - 33, 300, Color.BLACK alpha (191 / 255f))
        screenDrawing.fill(centerX - 150, 49, 300, height - 49 - 33, Color.BLACK alpha (112 / 255f))

        screenDrawing.drawCenteredTextWithShadow(type.text, centerX, 4, Color.WHITE)
        screenDrawing.drawCenteredTextWithShadow(downloadFromModrinthText(), centerX, 17, Color.LIGHT_GRAY)

        screenDrawing.fillWithBorder(centerX - 63, 30, 126, 15, Color.BLACK, if (this.selectedElement != search) Color.LIGHT_GRAY else Color.WHITE)

        if (!hasLoaded) {
            screenDrawing.drawCenteredText("Loading...", width / 2, height / 2, Color.WHITE)
            if (Modrinth.getPageOfType(type, index, query) != null) {
                hasLoaded = true
                resize()
            }
        }

        screenDrawing.drawCenteredTextWithShadow(Component.literal("${index + 1}/${Modrinth.typeMaps[type to query]?.second?.div(20)?.plus(1)?.toString() ?: "..."}"), centerX + 108, 34, Color.WHITE)

        if (System.currentTimeMillis() - lastTyped > 500) {
            hasLoaded = false
            index = 0
            lastTyped = Long.MAX_VALUE
            query = search.text
            resize()
        }
    }

    override fun Init.init() {
        Div {
            onInit = onInit@{
                val page = Modrinth.getPageOfType(type, index, query)?.map(::PackEntry) ?: return@onInit

                hasLoaded = true

                Empty()
                addRenderables(page)
                Empty()
            }
            gap = 4
            fitType = FitType.SCROLL
        }(width / 2 - 150, 49, 300, height - 49 - 33)

        MinecraftButton {
            text = CommonComponents.GUI_DONE
            onClick = { setScreen(parent) }
        }(centerX - 100, y2 - 26, 200, 20)

        MinecraftButton {
            text = Component.literal(">")
            onClick = {
                if (index < (Modrinth.typeMaps[type to query]?.second ?: 0) / 20) {
                    index++
                    hasLoaded = false
                    resize()
                }
            }
        }(centerX + 136, 31, 14, 14)

        MinecraftButton {
            text = Component.literal("<")
            onClick = {
                if (index > 0) {
                    index--
                    hasLoaded = false
                    resize()
                }
            }
        }(centerX + 66, 31, 14, 14)

        addRenderable(search(centerX - 60, 33, 120, 14))
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            setScreen(parent)
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    inner class PackEntry(val pack: Modrinth.ListPack) : SimpleRenderable({ height = 32 }) {
        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.drawText(pack.title.toText(), x + 38, y + 1, Color.WHITE)
            val lists = screenDrawing.wrapText(pack.description, width - 40)
            for (i in 0 until minOf(2, lists.size)) {
                if (i == 1 && lists.size > 2) {
                    screenDrawing.drawTextWithShadow(screenDrawing.wrapText(lists[i], width - 50)[0] + "...", x + 38, y + 12 + i * (screenDrawing.getTextHeight() + 1), Color.LIGHT_GRAY)
                    break
                }
                screenDrawing.drawTextWithShadow(lists[i], x + 38, y + 12 + i * (screenDrawing.getTextHeight() + 1), Color.LIGHT_GRAY)
            }

            Modrinth.getImageByURL(URI(pack.icon_url))?.let {
                screenDrawing.drawTexture(it, x + 4, y, 32, 32)
                if (isMouseOver(mouseX, mouseY) && screenDrawing.isMouseOver(mouseX, mouseY, centerX - 150, 49, 300, Bewisclient.screenHeight - 49 - 33)) {
                    screenDrawing.fill(x + 4, y, 32, 32, 0xA0909090.color)
                }
            }

            if (isMouseOver(mouseX, mouseY) && screenDrawing.isMouseOver(mouseX, mouseY, centerX - 150, 49, 300, Bewisclient.screenHeight - 49 - 33)) {
                if (screenDrawing.isMouseOver(mouseX, mouseY, x + 4, y, 32, 32)) {
                    screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/download_highlighted.png"), x + 4, y, 32, 32)
                } else {
                    screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/download.png"), x + 4, y, 32, 32)
                }
            }
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (isMouseOver(mouseX.toInt(), mouseY.toInt(), x + 4, y, 32, 32)) {
                Modrinth.loadPack(pack.slug) { p ->
                    Modrinth.loadVersions(p) { map ->
                        map.values.filter { it.loaders.contains(type.loader) && it.game_versions.contains(getModrinthVersion()) }.maxByOrNull { it.date_published }?.let { version ->
                            version.files.firstOrNull { it.primary }?.also { file ->
                                val progressNotification = ProgressNotification { text = Modrinth.downloading(pack.title) }
                                NotificationManager.addNotification(progressNotification)
                                Bewisclient.downloadFileWithProgress(URI(file.url), {
                                    progressNotification.progress = it
                                }, {
                                    folder.resolve(file.filename).writeBytes(it)
                                }) {
                                    NotificationManager.addNotification(SimpleTextNotification { text = Modrinth.downloadFailedReason(it.message ?: "Unknown error") })
                                }
                            } ?: run {
                                NotificationManager.addNotification(SimpleTextNotification { text = Modrinth.downloadFailed() })
                            }
                        } ?: run {
                            NotificationManager.addNotification(SimpleTextNotification { text = Modrinth.downloadFailed() })
                        }
                    }
                }
            }
            return super.onMouseClick(mouseX, mouseY, button)
        }
    }
}