package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButtonElement
import net.bewis09.bewisclient.drawable.renderables.components.setting.Input
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.impl.CustomWidget
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.RenderiteElement.Companion.plus
import net.bewis09.renderite.components.DivElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.Direction
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.style.RenderiteChild

val addLine = Translation("widget.tiwyla_widget.add_line", "Add Line")

@RenderiteChild
fun Renderable.CustomWidgetLineRenderable() = Div top@{
    gap = 3
    cacheChildren = true
    onInit = {
        Empty { height = 0 }
        HorizontalLine { backgroundColor = { General.getThemeColor(alpha = 0.5f) } }
        Div {
            paddingTop = 1
            direction = Direction.VERTICAL
            cacheChildren = true
            lines = 2
            gap = 5
            onInit = {
                Div {
                    direction = Direction.VERTICAL
                    cacheChildren = true
                    gap = 1
                    onInit = {
                        CustomWidget.lines.forEachIndexed { index, line ->
                            Div {
                                cacheChildren = true
                                direction = Direction.HORIZONTAL
                                height = 9
                                gap = 1
                                fitType = FitType.FILL_ITEM
                                onInit = {
                                    ActionButton {
                                        image = createIdentifier("bewisclient", "textures/gui/sprites/remove.png")
                                        imagePadding = 1
                                        onClick = {
                                            CustomWidget.lines.removeAt(index)
                                            this@top.recompute()
                                        }
                                    }
                                    ActionButton {
                                        image = createIdentifier("bewisclient", "textures/gui/sprites/up.png")
                                        imagePadding = 0
                                        onClick = {
                                            if (index > 0) {
                                                val temp = CustomWidget.lines[index - 1]
                                                CustomWidget.lines[index - 1] = CustomWidget.lines[index]
                                                CustomWidget.lines[index] = temp
                                                this@top.recompute()
                                            }
                                        }
                                    }
                                    ActionButton {
                                        image = createIdentifier("bewisclient", "textures/gui/sprites/down.png")
                                        imagePadding = 0
                                        onClick = {
                                            if (index < CustomWidget.lines.size - 1) {
                                                val temp = CustomWidget.lines[index + 1]
                                                CustomWidget.lines[index + 1] = CustomWidget.lines[index]
                                                CustomWidget.lines[index] = temp
                                                this@top.recompute()
                                            }
                                        }
                                    }
                                    Gap(1)
                                    Input {
                                        onChange = { CustomWidget.lines[index] = it }
                                        maxTextLength = 1000
                                        fillParent = true
                                        font = if (General.isMinecrafty) ScreenDrawing.DEFAULT_FONT else null
                                        text = line
                                    }
                                }
                            }
                        }
                    }
                }
                Div {
                    lines = CustomWidget.lines.size
                    fitType = FitType.SCROLL
                    cacheChildren = true
                    direction = Direction.HORIZONTAL
                    heightProvider = { CustomWidget.lines.size * 10 }
                    onInit = {
                        CustomWidget.lines.indices.forEach { line ->
                            Text {
                                renderLogic = { screenDrawing -> updateWidth(getMaxTextWidth(screenDrawing)) }
                                textProvider = { CustomWidget.computeLine(CustomWidget.lines[line]).toText() }
                                color = Color.WHITE
                                animated = {
                                    textAlign = if (CustomWidget.centered.get()) TextAlign.CENTER else TextAlign.START
                                }
                                font = ScreenDrawing.DEFAULT_FONT
                                minWidth = (this@top.width / 2 - 3)
                            }
                        }
                    }
                }
            }
        }
        Div {
            direction = Direction.HORIZONTAL
            cacheChildren = true
            fitType = FitType.FILL_ITEM
            gap = 1
            height = 14
            paddingBottom = 1
            onInit = {
                Button {
                    text = addLine()
                    fillParent = true
                    onClick = {
                        CustomWidget.lines.add("")
                        this@top.recompute()
                    }
                }
                ImageButton {
                    image = createIdentifier("bewisclient", "textures/gui/sprites/help.png")
                    onClick = {
                        OptionScreen.currentInstance?.openPopup(CustomWidgetHelpPopup(), Color.BLACK alpha 0.9f)
                    }
                    imagePadding = 2
                }.updateSize(14, 14)
            }
        }
        HorizontalLine { backgroundColor = { General.getThemeColor(alpha = 0.5f) } }
    }
}


fun getMaxTextWidth(screenDrawing: ScreenDrawing): Int {
    return (CustomWidget.lines.maxOfOrNull {
        screenDrawing.getTextWidth(CustomWidget.computeLine(it).toText()) {
            font = ScreenDrawing.DEFAULT_FONT
        }
    } ?: 0f).toInt()
}

@RenderiteChild
fun Renderable.ActionButton(p: RenderiteElement.Props<ImageButtonElement>) {
    ImageButton(p + {
        small = true
        width = 9
        height = 9
    })
}

fun DivElement<*, *, *, *>.recompute() {
    clearCache()
    resize()
}

fun CustomWidgetHelpPopup(): Renderable = DivElement {
    gap = 3
    fitType = FitType.SCROLL
    background = { SelectiveScreenDrawer.renderPopupBackground(it, x, y, width, height, 10, 0.15f) }
    padding = 10
    width = 200
    heightProvider = { Bewisclient.screenHeight - 100 }
    paddingOverflowVisible = false
    onInit = {
        Text {
            text = CustomWidget.customWidgetParamInfo()
            textAlign = TextAlign.CENTER
            color = General.getThemeColor()
            padding = 0
            heightResize = true
        }
        CustomWidget.widgetStringDataPoints.forEach { dataPoint ->
            Empty { height = 0 }
            Text {
                text = dataPoint.name().append(" ".toText()).append(("{${dataPoint.id}}").toText().setColor((General.getThemeColor(black = 0.5f)).argb))
            }
            Text {
                text = dataPoint.description()
                wrap = true
                color = General.getThemeColor(alpha = 0.7f)
            }
            if (dataPoint.param != null) {
                Text {
                    text = "Param: ".toText().append(dataPoint.param())
                    color = General.getThemeColor(alpha = 0.4f)
                    wrap = true
                }
            }
        }
    }
}
