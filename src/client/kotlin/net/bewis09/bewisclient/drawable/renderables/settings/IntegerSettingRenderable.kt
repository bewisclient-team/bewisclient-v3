package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.util.number.Precision

class IntegerSettingRenderable(p: Props<IntegerSettingRenderable>) : FaderSettingRenderable<Int, IntegerSettingRenderable>(p + {
    precision = Precision(min.toFloat(), max.toFloat(), 1f, 0)
    parser = { original: Float -> original.toInt() }
}) {
    var min: Int = 0
    var max: Int = 10

    init { props() }
}