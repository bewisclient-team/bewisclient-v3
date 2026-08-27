package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.`snake_toWord With Spaces`
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.cosmetics.CosmeticIdentifier
import net.bewis09.bewisclient.cosmetics.CosmeticType
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.element.Cape
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.cosmetics.Cosmetic
import net.bewis09.bewisclient.features.cosmetics.CosmeticLoader
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.components.Hoverable
import net.bewis09.renderite.drawer.darken
import net.bewis09.renderite.logic.*

class SelectCapeElement(p: Props<SelectCapeElement>) : Hoverable<SelectCapeElement>(p + {
    heightProvider = { (width - 16) * 16 / 10 + 25 }
}) {
    lateinit var identifier: CosmeticIdentifier
    lateinit var cosmetic: Cosmetic

    init { props() }

    val selected = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 0f)

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.renderLogic(screenDrawing, mouseX, mouseY)
        selected.set(if (CosmeticLoader.selected[CosmeticType.CAPE.id] == identifier.id) 1f else 0f)
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (General.isMinecrafty) {
            screenDrawing.darken(0.6f) {
                screenDrawing.fill(x + 3, y + 3, width - 6, height - 6, (selected.get() / 2f) within (hoverFactor within (!0x333333 to !0x444444) to Color.WHITE))
            }
        } else {
            screenDrawing.fillRounded(x, y, width, height, 5, General.getThemeColor(alpha = selected.get() * 0.3f + hoverFactor * 0.15f + 0.1f))
        }
    }

    override fun Init.init() {
        Cape {
            overflowVisible = true
            idProvider = { cosmetic.getIdentifier() }
        }(x + 8, y + 8, width - 16, height - 25)
        Text {
            text = `snake_toWord With Spaces`(identifier.id).toText()
            overflowVisible = true
            verticalAlign = TextAlign.END
            textAlign = TextAlign.CENTER
            lineHeight = 7 / 9f
            wrap = true
        }(x + 4, y2 - 7, width - 8, 0)
        Rectangle {
            background = { screenDrawing ->
                if (General.isMinecrafty) {
                    screenDrawing.darken(0.6f + selected.get() * 0.4f) {
                        screenDrawing.drawBorder(x, y, width, height, !0x222222)
                        screenDrawing.drawBorder(x + 1, y + 1, width - 2, height - 2, hoverFactor within (!0x5B5B5B to !0xA1A1A1))
                        screenDrawing.drawBorder(x + 2, y + 2, width - 4, height - 4, !0x282828)
                    }
                } else {
                    screenDrawing.drawBorderRounded(x, y, width, height, 5, 0.2f within ((selected.get() within (Color.DARK_GRAY to Color.WHITE)) to General.getThemeColor()))
                }
            }
        }(x, y, width, height)
        if (CosmeticLoader.elytraCosmetics.contains(identifier)) {
            Image {
                background = { screenDrawing ->
                    if (General.isMinecrafty) {
                        screenDrawing.drawBorder(x, y, 22, 22, Color.BLACK alpha 0.5f)
                        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x + 1, y + 1, 20, 20, 1f, small = true)
                    } else {
                        screenDrawing.fillWithBorderRounded(x, y, 22, 22, 5, 0.3f within (Color.BLACK to General.getThemeColor()), 0.2f within ((selected.get() within (Color.DARK_GRAY to Color.WHITE)) to General.getThemeColor()), topLeft = false, bottomRight = false)
                    }
                }
                image = createIdentifier("textures/item/elytra.png")
                padding = 3
            }(x2 - 22, y, 22, 22)
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false
        CosmeticLoader.selected[CosmeticType.CAPE.id] = if (CosmeticLoader.selected[CosmeticType.CAPE.id] == identifier.id) null else identifier.id
        CosmeticLoader.timestamp.set(System.currentTimeMillis())
        return true
    }
}