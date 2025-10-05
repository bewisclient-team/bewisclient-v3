package net.bewis09.bewisclient.settings

import com.google.gson.*
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.settings.types.Setting

interface Settings : BewisclientInterface {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    /**
     * Returns the ID of the settings.
     * It is used to name the settings file.
     */
    fun getId(): String

    fun getMainSetting(): Setting<*>?

    fun load() {
        val data = readRelativeFile("bewisclient", getId() + ".json")
        getMainSetting()?.setFromElement(gson.fromJson(data, JsonElement::class.java))
    }

    fun save() {
        val mainSetting = getMainSetting() ?: return
        val jsonElement = mainSetting.convertToElement()
        val jsonString = gson.toJson(jsonElement)
        saveRelativeFile(jsonString, "bewisclient", getId() + ".json")
    }
}