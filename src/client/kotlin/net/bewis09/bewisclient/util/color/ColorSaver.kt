package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.catch
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.translations.Translation

interface ColorSaver {
    fun getColor(): Color
    fun getType(): String
    fun saveToJson(): JsonElement

    fun getColorInt(): Int {
        return getColor().argb
    }

    companion object {
        val types = listOf(
            StaticColorSaver.Factory, ChangingColorSaver.Factory, ThemeColorSaver.Factory
        ).also { it.forEach { a -> a.getDefault() } }

        @Suppress("UNCHECKED_CAST")
        fun <T : ColorSaver> getFactory(color: T): ColorSaverFactory<T>? {
            return catch { types.firstOrNull { it.getType() == color.getType() } as? ColorSaverFactory<T> }
        }

        fun getType(type: String): ColorSaverFactory<*>? {
            return types.firstOrNull { it.getType() == type }
        }

        fun fromJson(jsonElement: JsonElement?): ColorSaver? {
            if (jsonElement?.isJsonObject == true) {
                val jsonObject = jsonElement.asJsonObject
                val type = jsonObject.get("type")?.asString ?: return null
                val factory = types.firstOrNull { it.getType() == type } ?: return null
                val data = jsonObject.get("data") ?: return null

                return factory.createFromJson(data)
            }
            return null
        }
    }

    fun convertToElement(): JsonElement? = JsonObject().apply {
        addProperty("type", getType())
        add("data", saveToJson())
    }

    fun toInfoString(): String
}

interface ColorSaverFactory<T : ColorSaver> {
    fun createFromJson(jsonElement: JsonElement): T?
    fun getType(): String
    fun getTranslation(): Translation
    fun getDefault(): T
    fun getDescription(): Translation? = null
    fun getSettingsRenderable(get: () -> T, set: (ColorSaver) -> Unit): Renderable
}