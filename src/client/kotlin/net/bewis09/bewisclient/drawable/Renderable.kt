package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.RenderiteElement

typealias Renderable = RenderiteElement<ScreenDrawing, *>
typealias PropedRenderable<P> = RenderiteElement<ScreenDrawing, P>

open class SimpleRenderable(p: Props<SimpleRenderable> = {}): PropedRenderable<SimpleRenderable>(p) {
    init {
        props()
    }
}