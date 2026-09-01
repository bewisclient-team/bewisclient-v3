package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.components.DivElement
import net.bewis09.renderite.components.RectangleElement
import net.bewis09.renderite.components.TextElement
import net.minecraft.network.chat.Component

typealias Renderable = RenderiteElement<ScreenDrawing, *, Component, Identifier, Identifier>
typealias PropedRenderable<P> = RenderiteElement<ScreenDrawing, P, Component, Identifier, Identifier>

open class SimpleRenderable(p: Props<SimpleRenderable> = {}): PropedRenderable<SimpleRenderable>(p) {
    init {
        props()
    }
}

typealias Div = DivElement<ScreenDrawing, Component, Identifier, Identifier>
typealias Text = TextElement<ScreenDrawing, Component, Identifier, Identifier>
typealias Rectangle = RectangleElement<ScreenDrawing, Component, Identifier, Identifier>

typealias Init = RenderiteElement<ScreenDrawing, *, Component, Identifier, Identifier>.Init