package net.bewis09.bewisclient.impl.pack

import net.bewis09.bewisclient.core.*
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.Input
import net.bewis09.bewisclient.drawable.renderables.MinecraftButton
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.color
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.toText
import net.minecraft.SharedConstants
import net.minecraft.client.gui.screen.Screen
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.writeBytes

class PackListScreen(val type: Modrinth.Type, val parent: Screen, val folder: Path) : Renderable() {
    var index = 0
    var hasLoaded = false
    var lastTyped = Long.MAX_VALUE

    val search = Input(font = ScreenDrawingInterface.DEFAULT_FONT, maxWidth = 200, onChange = {
        lastTyped = System.currentTimeMillis()
    })

    var query: String = search.text

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawHorizontalLine(centerX - 150, 47, 300, Color.WHITE alpha (51 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, y2 - 32, 300, Color.WHITE alpha (51 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, 48, 300, Color.BLACK alpha (191 / 255f))
        screenDrawing.drawHorizontalLine(centerX - 150, y2 - 33, 300, Color.BLACK alpha (191 / 255f))
        screenDrawing.fill(centerX - 150, 49, 300, height - 49 - 33, Color.BLACK alpha (112 / 255f))

        screenDrawing.drawCenteredTextWithShadow(type.text, centerX, 4, Color.WHITE)
        screenDrawing.drawCenteredTextWithShadow(Translations.DOWNLOAD_FROM_MODRINTH(), centerX, 17, Color.LIGHT_GRAY)

        screenDrawing.fillWithBorder(centerX - 63, 30, 126, 15, Color.BLACK, if(this.selectedElement != search) Color.LIGHT_GRAY else Color.WHITE)

        renderRenderables(screenDrawing, mouseX, mouseY)

        if (!hasLoaded) {
            screenDrawing.drawCenteredText("Loading...", width / 2, height / 2, Color.WHITE)
            if (Modrinth.getPageOfType(type, index, query) != null) {
                hasLoaded = true
                resize()
            }
        }

        screenDrawing.drawCenteredTextWithShadow(Text.literal("${index + 1}/${Modrinth.typeMaps[type to query]?.second?.div(20)?.plus(1)?.toString() ?: "..."}"), centerX + 108, 34, Color.WHITE)

        if (System.currentTimeMillis() - lastTyped > 500) {
            hasLoaded = false
            index = 0
            lastTyped = Long.MAX_VALUE
            query = search.text
            resize()
        }
    }

    override fun init() {
        addRenderable(VerticalAlignScrollPlane({
            Modrinth.getPageOfType(type, index, query)?.map {
                PackEntry(it)
            }?.also {
                hasLoaded = true
            }?.let {
                listOf(Plane { _, _, _, _ -> listOf() }, *it.toTypedArray(), Plane { _, _, _, _ -> listOf() })
            } ?: emptyList()
        }, 4)(width / 2 - 150, 49, 300, height - 49 - 33))

        addRenderable(MinecraftButton(ScreenTexts.DONE) {
            client.setScreen(parent)
        }(centerX - 100, y2 - 26, 200, 20))

        addRenderable(MinecraftButton(Text.literal(">")) {
            if (index < (Modrinth.typeMaps[type to query]?.second ?: 0) / 20) {
                index++
                hasLoaded = false
                resize()
            }
        }(centerX + 136, 31, 14, 14))

        addRenderable(MinecraftButton(Text.literal("<")) {
            if (index > 0) {
                index--
                hasLoaded = false
                resize()
            }
        }(centerX + 66, 31, 14, 14))

        addRenderable(search(centerX - 60, 33, 120, 14))
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            client.setScreen(parent)
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    inner class PackEntry(val pack: Modrinth.ListPack) : Renderable() {
        init {
            internalHeight = 32
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
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
                if (isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && isMouseOver(mouseX.toFloat(), mouseY.toFloat(), centerX - 150, 49, 300, screenHeight - 49 - 33)) {
                    screenDrawing.fill(x + 4, y, 32, 32, 0xA0909090.color)
                }
            }

            if (isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && isMouseOver(mouseX.toFloat(), mouseY.toFloat(), centerX - 150, 49, 300, screenHeight - 49 - 33)) {
                if (screenDrawing.isMouseOver(mouseX.toFloat(), mouseY.toFloat(), x + 4, y, 32, 32)) {
                    screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/download_highlighted.png"), x + 4, y, 32, 32)
                } else {
                    screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/download.png"), x + 4, y, 32, 32)
                }
            }
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (isMouseOver(mouseX.toFloat(), mouseY.toFloat(), x + 4, y, 32, 32)) {
                Modrinth.loadPack(pack.slug) { p ->
                    Modrinth.loadVersions(p) { map ->
                        map.values.filter { it.game_versions.contains(SharedConstants.getGameVersion().name) }.maxByOrNull { it.date_published }?.let { version ->
                            version.files.firstOrNull { it.primary }?.let { file ->
                                downloadFile(URI(file.url).toURL()) {
                                    folder.resolve(file.filename).writeBytes(it)
                                }
                            }
                        }
                    }
                }
            }
            return super.onMouseClick(mouseX, mouseY, button)
        }
    }
}