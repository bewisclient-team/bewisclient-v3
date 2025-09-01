package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface.Color
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.StyleSpriteSource
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 *
 * @param drawContext The DrawContext used for drawing operations. Using it directly is extremely
 * discouraged.
 */
@Suppress("Unused")
class ScreenDrawing(override val drawContext: DrawContext, override val textRenderer: TextRenderer) : TextDrawing, RoundedDrawing, ItemDrawing {
    override var style: Style = Style.EMPTY
    override val afterDrawStack = hashMapOf<String, ScreenDrawingInterface.AfterDraw>()
    override val colorStack = mutableListOf<Color>()
}