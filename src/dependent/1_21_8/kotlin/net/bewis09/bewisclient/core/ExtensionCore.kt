package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.util.InputUtil
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.profiler.Profilers
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.function.Consumer

fun LivingEntity.pos(): Vec3d = this.pos

fun HorseEntity.getColor(): String = this.horseColor.asString().lowercase()

var LivingEntity.beforeHeadYaw: Float
    get() = this.lastHeadYaw
    set(value) {
        this.lastHeadYaw = value
    }

fun Text.setFont(id: Identifier?): MutableText {
    return (this as? MutableText ?: this.copy()).styled { it.withFont(id ?: ScreenDrawingInterface.BEWISCLIENT_FONT) }
}

fun DrawContext.pop() {
    this.matrices.popMatrix()
}

fun DrawContext.push() {
    this.matrices.pushMatrix()
}

fun DrawContext.translate(x: Float, y: Float) {
    this.matrices.translate(x, y)
}

fun DrawContext.scale(x: Float, y: Float) {
    this.matrices.scale(x, y)
}

fun DrawContext.rotate(angle: Float) {
    this.matrices.rotate(angle)
}

fun DrawContext.drawTexture(
    texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
) = this.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)

fun DrawContext.drawEntity(x1: Int, y1: Int, x2: Int, y2: Int, scale: Float, translation: Vector3f?, rotation: Quaternionf?, overrideCameraAngle: Quaternionf?, entity: LivingEntity) = InventoryScreen.drawEntity(
    this, x1, y1, x2, y2, scale, translation, rotation, overrideCameraAngle, entity
)

fun DrawContext.translateToTopOptional() = Unit

fun DrawContext.drawItemOverlay(textRenderer: TextRenderer, itemStack: ItemStack, x: Int, y: Int) {
    this.drawStackOverlay(textRenderer, itemStack, x, y)
}

fun MinecraftClient.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.registerTexture(
        identifier, NativeImageBackedTexture({ identifier.toString() }, image)
    )
}

object Profiler {
    fun push(name: String) = Profilers.get().push(name)

    fun pop() = Profilers.get().pop()
}

fun registerKeybind(translation: String, type: InputUtil.Type, default: Int): KeyBinding {
    return KeyBinding(
        translation, type, default, KeyBinding.MISC_CATEGORY
    )
}

fun MinecraftClient.isKeyPressed(key: Int): Boolean {
    return InputUtil.isKeyPressed(this.window.handle, key)
}

fun registerWidget(id: Identifier, widget: (context: DrawContext) -> Unit) = HudElementRegistry.addLast(id) { context, _ -> widget(context) }

fun ItemStack.appendTooltip(textConsumer: Consumer<Text>) {
    this.appendTooltip(Item.TooltipContext.DEFAULT, TooltipDisplayComponent.DEFAULT, null, TooltipType.BASIC, textConsumer)
}

fun ItemStack.getItemFormattedName(): Text {
    val mutableText: MutableText = Text.empty().append(this.name).formatted(this.rarity.formatting)
    if (this.contains(DataComponentTypes.CUSTOM_NAME)) {
        mutableText.formatted(Formatting.ITALIC)
    }

    return mutableText
}