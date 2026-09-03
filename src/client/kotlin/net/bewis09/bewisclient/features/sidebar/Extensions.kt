package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.renderite.components.DivElement
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.renderite.drawer.pushColor
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.Direction
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.ItemAlign
import net.bewis09.renderite.logic.LineType
import net.bewis09.renderite.logic.color

object Extensions : SidebarFeature(createIdentifier("bewisclient", "extensions"), "Extensions") {
    val notFoundIdentifier: Identifier = createIdentifier("textures/misc/unknown_pack.png")

    override fun getRenderable(): Renderable = DivElement {
        gap = 1
        fitType = FitType.SCROLL
        onInit = {
            APIEntrypointLoader.mapContainer { ext ->
                Div {
                    background = { screenDrawing ->
                        screenDrawing.pushColor(0.7f, 0.7f, 0.7f, 1f) {
                            SelectiveScreenDrawer.renderSettingRenderableBackground(screenDrawing, hoverAnimation.get(), x, y, width, height)
                        }
                    }
                    padding = 8
                    direction = Direction.HORIZONTAL
                    fitType = FitType.FILL_ITEM
                    lineType = LineType.ENLARGE
                    itemAlign = ItemAlign.CENTER
                    gap = 5
                    onInit = {
                        Image {
                            image = ext.entrypoint.getIcon(ext.provider) ?: notFoundIdentifier
                            width = 16
                            height = 16
                        }
                        Div {
                            fillParent = true
                            cacheChildren = true
                            onInit = {
                                Text {
                                    text = ("${ext.entrypoint.getExtensionTitle(ext.provider)} ").toText().append(("(${ext.provider.metadata.id})").toText().setColor(0xAAAAAA))
                                    height = 9
                                    color = Color.WHITE
                                }
                                Text {
                                    text = ext.entrypoint.getExtensionDescription(ext.provider).toText()
                                    heightResize = true
                                    wrap = true
                                    height = 0
                                    color = 0xAAAAAA.color alpha 0.8f
                                }
                            }
                        }
                    }
                }(x, y, width, height)
            }
        }
    }
}