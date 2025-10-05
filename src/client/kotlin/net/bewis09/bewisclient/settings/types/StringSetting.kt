package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.util.string

class StringSetting : Setting<String> {
    constructor(default: String, onChangeListener: (Setting<String>.(oldValue: String?, newValue: String?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: String) : super(default)

    constructor(default: () -> String, onChangeListener: (Setting<String>.(oldValue: String?, newValue: String?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: () -> String) : super(default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): String? = data?.string()
}