package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.features.sidebar.Widgets
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.widget.Widget

abstract class ScalableWidget(id: Identifier, title: String, description: String) : Widget(id, title, description) {
    var scale = create("scale", FloatSetting({ getDefaultScale() }, Widgets.Default.scale.precision))

    companion object {
        val feature = Feature(createIdentifier("bewisclient", "widget.scalable_widget"))
    }

    open fun getDefaultScale(): Float = Widgets.Default.scale.get()

    override fun getScale(): Float {
        return scale.get()
    }

    override fun appendSettingsRenderables(list: Renderable) {
        list.addRenderable(scale.createRenderable(feature, "scale", "Scale", "Set the scale of the widget"))
    }
}
