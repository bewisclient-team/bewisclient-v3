package net.bewis09.bewisclient.widget.logic

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.widget.Widget

class SidedPosition(val x: Int, val y: Int, val xTransformer: TransformerType, val yTransformer: TransformerType) : WidgetPosition {
    companion object {
        val START = TransformerType("start") { pos: Int, _: Int, _: Float -> pos.toFloat() }
        val CENTER = TransformerType("center") { _: Int, size: Int, widgetSize: Float -> size / 2 - widgetSize / 2 }
        val END = TransformerType("end") { pos: Int, size: Int, widgetSize: Float -> size - pos - widgetSize }

        val transformerTypes = listOf(START, CENTER, END)
    }

    override fun getX(widget: Widget): Float {
        return xTransformer.transformer(x, widget.getScreenWidth(), widget.getScaledWidth())
    }

    override fun getY(widget: Widget): Float {
        return yTransformer.transformer(y, widget.getScreenHeight(), widget.getScaledHeight())
    }

    override fun saveToJson(): JsonElement {
        val jsonObject = JsonObject()

        jsonObject.addProperty("x", x)
        jsonObject.addProperty("y", y)
        jsonObject.addProperty("x_transformer", xTransformer.id)
        jsonObject.addProperty("y_transformer", yTransformer.id)

        return jsonObject
    }

    override fun getType(): String = "sided"

    class TransformerType(val id: String, val transformer: (Int, Int, Float) -> Float)

    object Factory : WidgetPositionFactory<SidedPosition> {
        override fun createFromJson(jsonElement: JsonElement): SidedPosition? {
            if (!jsonElement.isJsonObject) return null

            val jsonObject = jsonElement.asJsonObject

            val xObj = jsonObject.get("x")
            val yObj = jsonObject.get("y")

            val xTransformerObj = jsonObject.get("x_transformer")
            val yTransformerObj = jsonObject.get("y_transformer")

            if (xObj == null || yObj == null || !xObj.isJsonPrimitive || !yObj.isJsonPrimitive || !xObj.asJsonPrimitive.isNumber || !yObj.asJsonPrimitive.isNumber) {
                return null
            }

            if (xTransformerObj == null || yTransformerObj == null || !xTransformerObj.isJsonPrimitive || !yTransformerObj.isJsonPrimitive) {
                return null
            }

            val x = xObj.asInt
            val y = yObj.asInt

            val xTransformer = transformerTypes.find { it.id == xTransformerObj.asString } ?: return null
            val yTransformer = transformerTypes.find { it.id == yTransformerObj.asString } ?: return null

            return SidedPosition(x, y, xTransformer, yTransformer)
        }

        override fun getType(): String = "sided"
    }
}