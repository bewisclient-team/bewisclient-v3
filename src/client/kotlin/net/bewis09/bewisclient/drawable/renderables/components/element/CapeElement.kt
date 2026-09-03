package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.version.drawCape
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.style.RenderiteChild

class CapeElement(p: Props<CapeElement>): PropedRenderable<CapeElement>(p) {
    var idProvider: (() -> Identifier)? = null

    init { props() }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawCape((require { idProvider })(), x, y, width, height)
    }
}

@RenderiteChild
fun Renderable.Cape(p: RenderiteElement.Props<CapeElement>) = addRenderable(CapeElement(p))