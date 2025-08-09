package net.bewis09.bewisclient.widget

import com.google.gson.JsonObject
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

abstract class Widget: BewisclientInterface {
    var position: WidgetPosition? = null

    abstract fun defaultPosition(): WidgetPosition

    abstract fun getId(): Identifier

    fun renderScaled(screenDrawing: ScreenDrawing) {
        screenDrawing.push()
        screenDrawing.translate(getX(), getY())
        screenDrawing.scale(getScale(), getScale())
        render(screenDrawing)
        screenDrawing.pop()
    }

    abstract fun render(screenDrawing: ScreenDrawing)

    /**
     * Loads the properties of the widget from the given JSON object.
     */
    open fun loadProperties(properties: JsonObject) {}

    /**
     * Save the properties of the to the given JSON object.
     */
    open fun saveProperties(properties: JsonObject) {}

    fun loadFromJson(json: JsonObject) {
        json.get("properties")?.let {
            if (it.isJsonObject) {
                loadProperties(it.asJsonObject)
            } else {
                loadProperties(JsonObject())
            }
        }
        position = null
        json.get("position")?.let { positionJSON ->
            WidgetPosition.types.forEach {
                if (positionJSON.isJsonObject && json.get("positionType").isJsonPrimitive && json.get("positionType")?.asString == it.getType()) {
                    it.createFromJson(positionJSON.asJsonObject)?.let { a ->
                        position = a
                    }
                }
            }
        }
    }

    fun saveToJson(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add("properties", JsonObject().also {saveProperties(it)})
        position?.let {
            jsonObject.add("position", it.saveToJson())
            jsonObject.addProperty("positionType", it.getType())
        }

        return jsonObject
    }

    fun getScreenWidth(): Int {
        return (MinecraftClient.getInstance().window.scaledWidth)
    }

    fun getScreenHeight(): Int {
        return (MinecraftClient.getInstance().window.scaledHeight)
    }

    fun getScaledWidth(): Float {
        return (getWidth() * getScale())
    }

    fun getScaledHeight(): Float {
        return (getHeight() * getScale())
    }

    abstract fun getWidth(): Int
    abstract fun getHeight(): Int
    open fun getScale() = 1.0f

    fun getX() = (position ?: defaultPosition()).getX(this)
    fun getY() = (position ?: defaultPosition()).getY(this)

    abstract fun getTranslation(): Translation
    abstract fun getDescription(): Translation

    fun appendSettingsRenderables(list: ArrayList<Renderable>) {}
}