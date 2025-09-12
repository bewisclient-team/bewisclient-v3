package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.widget.Widget
import net.minecraft.util.Identifier

abstract class ScalableWidget(id: Identifier) : Widget(id) {
    var scale = create("scale", FloatSetting({ getDefaultScale() }, DefaultWidgetSettings.scale.precision))

    open fun getDefaultScale(): Float = DefaultWidgetSettings.scale.get()

    override fun getScale(): Float {
        return scale.get()
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(scale.createRenderable("widget.scale", "Scale", "Set the scale of the widget"))
    }
}
