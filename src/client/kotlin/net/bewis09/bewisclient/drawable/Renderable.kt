package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.components.Div
import net.bewis09.renderite.components.Rectangle
import net.bewis09.renderite.components.Text
import net.minecraft.network.chat.Component

typealias Renderable = RenderiteElement<ScreenDrawing, *, Component, Identifier, Identifier>
typealias PropedRenderable<P> = RenderiteElement<ScreenDrawing, P, Component, Identifier, Identifier>

open class SimpleRenderable(p: Props<SimpleRenderable> = {}): PropedRenderable<SimpleRenderable>(p) {
    init {
        props()
    }
}

typealias Div = Div<ScreenDrawing, Component, Identifier, Identifier>
typealias Text = Text<ScreenDrawing, Component, Identifier, Identifier>
typealias Rectangle = Rectangle<ScreenDrawing, Component, Identifier, Identifier>

typealias Init = RenderiteElement<ScreenDrawing, *, Component, Identifier, Identifier>.Init