package net.bewis09.bewisclient.drawable.renderables.settings

class FloatSettingRenderable(p: Props<FloatSettingRenderable>) : FaderSettingRenderable<Float, FloatSettingRenderable>(p + { parser = { it } }) {
    init {
        props()
    }
}