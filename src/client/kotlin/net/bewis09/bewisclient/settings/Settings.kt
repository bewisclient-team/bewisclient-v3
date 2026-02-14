package net.bewis09.bewisclient.settings

import com.google.gson.*
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.settings.types.Setting

abstract class Settings : BewisclientInterface {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    var isLoading = true
    var dirty: Boolean = false

    /**
     * Returns the ID of the settings.
     * It is used to name the settings file.
     */
    abstract fun getId(): String

    abstract fun getMainSetting(): Setting<*>?

    open fun load() {
        isLoading = true
        val data = readRelativeFile("bewisclient", getId() + ".json")
        getMainSetting()?.setFromElement(gson.fromJson(data, JsonElement::class.java))
        isLoading = false
    }

    fun setDirty() {
        dirty = true
    }

    fun save() {
        if (dirty && !isLoading) {
            val mainSetting = getMainSetting() ?: return
            val jsonElement = mainSetting.convertToElement()
            val jsonString = gson.toJson(jsonElement)
            saveRelativeFile(jsonString, "bewisclient", getId() + ".json")
            dirty = false
        }
    }
}