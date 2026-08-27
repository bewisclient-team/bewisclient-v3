package net.bewis09.renderite.components

import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.logic.Color

class Image<S: RenderiteDrawer<I, T, F>, T: Any, F, I: Any>(p: Props<Image<S, T, F, I>>): RenderiteElement<S, Image<S, T, F, I>, T, F, I>(p) {
    lateinit var image: I
    var u = 0f
    var v = 0f
    var regionWidth: Int? = null
    var regionHeight: Int? = null
    var textureWidth: Int? = null
    var textureHeight: Int? = null
    var colorProvider = { color }
    var color = Color.WHITE
    var padding: Int = 0
    var verticalPadding: Int? = null
    var horizontalPadding: Int? = null
    var paddingLeft: Int? = null
    var paddingTop: Int? = null
    var paddingRight: Int? = null
    var paddingBottom: Int? = null

    init { props() }

    override fun renderElement(screenDrawing: S, mouseX: Int, mouseY: Int) {
        val paddingTop = paddingTop ?: verticalPadding ?: padding
        val paddingBottom = paddingBottom ?: verticalPadding ?: padding
        val paddingLeft = paddingLeft ?: horizontalPadding ?: padding
        val paddingRight = paddingRight ?: horizontalPadding ?: padding

        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        screenDrawing.drawTextureRegion(image, x + paddingLeft, y + paddingTop, u, v, width, height, regionWidth ?: width, regionHeight ?: height, textureWidth ?: regionWidth ?: width, textureHeight ?: regionHeight ?: height, colorProvider())
    }
}