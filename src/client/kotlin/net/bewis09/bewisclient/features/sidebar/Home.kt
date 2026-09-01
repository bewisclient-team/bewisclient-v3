package net.bewis09.bewisclient.features.sidebar

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ButtonElement
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.util.string
import net.bewis09.renderite.components.DivElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.FitType
import net.minecraft.network.chat.Component

object Home : SidebarFeature(createIdentifier("bewisclient", "home"), "Bewisclient") {
    val quickSettings = create("quick_settings", ListSetting(mutableListOf(), { it.string() }, ::JsonPrimitive))

    override fun getRenderable(): Renderable = HomePlane

    object HomePlane : PropedRenderable<HomePlane>() {
        val editQuickSettings = createTranslation("edit_quick_settings", "Edit Quick Settings")
        val no_quick_settings = createTranslation("no_quick_settings", "Here you can add settings that you need frequently, so you don't have to search for them in the different categories and have quicker access to them.")

        var borderRadius = Widgets.Default.borderRadius.get().toFloat()

        val quickSettingsOptions = mutableMapOf<String, MutableMap<String, Renderable>>()

        val checkTexture = createIdentifier("bewisclient", "textures/gui/sprites/check.png")

        val editButton = ButtonElement {
            text = editQuickSettings()
            onClick = {
                OptionScreen.currentInstance?.openPage(
                    editQuickSettings(),
                    DivElement.create {
                        initForEach(quickSettingsOptions) {
                            Empty {
                                height = 5
                            }
                            InfoTextRenderable {
                                text = Component.translatable(it.key)
                                centered = true
                                color = General.getTextThemeColor()
                                padding = 0
                            }.add()
                            Empty {
                                height = 3
                            }
                            it.value.forEach { a ->
                                ConfigureRenderableVisibilityPlane {
                                    category = it.key
                                    id = a.key
                                    renderable = a.value
                                }.add()
                            }
                        }
                        gap = 1
                        fitType = FitType.SCROLL
                    }
                )
            }
        }

        override fun Init.init() {
            Div {
                gap = 5
                fitType = FitType.SCROLL
                onInit = {
                    Div {
                        gap = 1
                        cacheChildren = true
                        onInit = {
                            (quickSettings.asSequence().filter { it.split("/").size >= 2 }.groupBy { it.split("/")[0] }).forEach {
                                val options = it.value.mapNotNull { a -> quickSettingsOptions[it.key]?.get(a.split("/")[1]) }.ifEmpty { return@forEach }

                                Empty { height = 5 }
                                InfoTextRenderable {
                                    text = Component.translatable(it.key)
                                    centered = true
                                    color = General.getTextThemeColor()
                                    padding = 0
                                }.updateHeight(14)
                                Empty { height = 3 }
                                addRenderables(options)
                            }

                            renderables.ifEmpty {
                                InfoTextRenderable {
                                    text = no_quick_settings()
                                    centered = true
                                    color = General.getTextThemeColor() alpha 0.66f
                                }(x + width / 2 - 100, y + height / 4, 200, 0)
                            }
                        }
                    }
                    object : SimpleRenderable() {
                        override fun Init.init() {
                            editButton.add(x + width / 2 - 50, y, 100, height)
                        }
                    }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight()).add()
                }
            }(x, y, width, height)
        }

        class ConfigureRenderableVisibilityPlane(p: Props<ConfigureRenderableVisibilityPlane>) : PropedRenderable<ConfigureRenderableVisibilityPlane>(p + {
            minWidth = 20
        }) {
            lateinit var category: String
            lateinit var id: String
            lateinit var renderable: Renderable

            init {
                props()
            }

            override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                height = renderable.height
            }

            override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                if (isMinecrafty) {
                    SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x, y + height / 2 - 9, 18, 18, 0f)
                } else {
                    screenDrawing.fillWithBorderRounded(x, y + height / 2 - 8, 16, 16, 5, General.getThemeColor(alpha = 0.15f), General.getThemeColor(alpha = 0.15f))
                }
            }

            override fun renderAccessories(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                if (!quickSettings.contains("$category/$id")) return

                screenDrawing.afterDraw("selection:$category/$id", {
                    screenDrawing.enableScissors(this@HomePlane.x, this@HomePlane.y, this@HomePlane.width, this@HomePlane.height) {
                        screenDrawing.drawTexture(checkTexture, x + if (isMinecrafty) 2 else 1, y + height / 2 - 7, 14, 14, if (isMinecrafty) Color.WHITE else General.getThemeColor())
                    }
                })
            }

            override fun Init.init() {
                renderable.updateWidth(width - 20)
                renderable.addPositioned(x + 20, y)
            }

            override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
                if (mouseX >= x && mouseX <= x + 16 && mouseY >= y + height / 2 - 8 && mouseY <= y + height / 2 + 8) {
                    val key = "$category/$id"
                    if (!quickSettings.remove(key)) {
                        quickSettings.add(key)
                    }
                    return true
                }
                return super.onMouseClick(mouseX, mouseY, button)
            }
        }
    }

    fun <T : Renderable> T.addToQuickSettings(category: String, id: String): T {
        HomePlane.quickSettingsOptions.getOrPut(category, ::mutableMapOf)[id] = this
        return this
    }
}