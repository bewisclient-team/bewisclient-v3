package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.FloatSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.float
import net.bewis09.bewisclient.util.number.Precision

class FloatSetting(default: () -> Float, val precision: Precision) : Setting<Float>(default), RenderableCreator<FloatSettingRenderable> {
    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Float? = processChange(data?.float())

    override fun createRenderable(
        feature: Feature, id: String, title: String, description: String?
    ) = FloatSettingRenderable {
        this.title = feature.createTranslation(id, title)
        tooltip = description?.let { Translation("$id.description", it)() }
        setting = this@FloatSetting
        precision = this@FloatSetting.precision
    }

    fun createIntRenderable(
        feature: Feature, id: String, title: String, description: String? = null
    ): IntegerSettingRenderable = IntegerSettingRenderable(
        {
            this.title = feature.createTranslation(id, title)
            tooltip = description?.let { feature.createTranslation("$id.description", it)() }
            setting = object : SettingInterfaceWithDefault<Int> {
                override fun set(value: Int?) = this@FloatSetting.set(value?.toFloat())

                override fun get(): Int = this@FloatSetting.get().toInt()

                override fun getDefault(): Int = this@FloatSetting.getDefault().toInt()
            }
            min = this@FloatSetting.precision.min.toInt()
            max = this@FloatSetting.precision.max.toInt()
        }
    )

    override fun processChange(value: Float?): Float? = value?.let {
        precision.parse(it)
    }

    fun cloneWithDefault(): FloatSetting = FloatSetting(::get, precision)
}