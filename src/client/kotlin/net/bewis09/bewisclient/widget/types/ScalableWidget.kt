package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.widget.Widget

abstract class ScalableWidget(): Widget() {
    var scale = getSettings().widgetSettings.defaults.scale.cloneWithDefault()

    init {
        create("scale", scale)
    }

    override fun getScale(): Float {
        return scale.get()
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(scale.createRenderable("widget.scale", "Scale"))
    }
}