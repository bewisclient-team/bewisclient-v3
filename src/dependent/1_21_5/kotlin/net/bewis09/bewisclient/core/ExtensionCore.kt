package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.util.InputUtil
import net.minecraft.component.DataComponentTypes
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

fun HorseEntity.getColor(): String = this.horseColor.name.lowercase()

var LivingEntity.beforeHeadYaw: Float
    get() = this.lastHeadYaw
    set(value) { this.lastHeadYaw = value }

fun Text.setFont(id: Identifier?): Text {
    return (this as? MutableText ?: this.copy()).styled { it.withFont((id ?: ScreenDrawingInterface.BEWISCLIENT_FONT)) }
}

fun DrawContext.pop() = this.matrices.pop()

fun DrawContext.push() = this.matrices.push()

fun DrawContext.translate(x: Float, y: Float) {
    this.matrices.translate(x.toDouble(), y.toDouble(), 0.0)
}

fun DrawContext.scale(x: Float, y: Float) {
    this.matrices.scale(x, y, 1f)
}

fun DrawContext.rotate(angle: Float) = this.matrices.multiply(Quaternionf().rotateZ(angle))

fun DrawContext.drawTexture(
    texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
) = this.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)

fun DrawContext.drawEntity(x1: Int, y1: Int, x2: Int, y2: Int, scale: Float, translation: Vector3f?, rotation: Quaternionf?, overrideCameraAngle: Quaternionf?, entity: LivingEntity) = InventoryScreen.drawEntity(
    this, (x1 + x2) / 2f, (y1 + y2) / 2f, scale, translation, rotation, overrideCameraAngle, entity
)

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

fun registerWidget(id: Identifier, widget: (context: DrawContext) -> Unit) = HudRenderCallback.EVENT.register { context, _ -> widget(context) }

fun ItemStack.appendTooltip(textConsumer: Consumer<Text>) {
    for ((index, text) in this.getTooltip(Item.TooltipContext.DEFAULT, null, TooltipType.BASIC).withIndex()) {
        if (index == 0) continue
        textConsumer.accept(text)
    }
}

fun ItemStack.getItemFormattedName(): Text {
    val mutableText: MutableText = Text.empty().append(this.name).formatted(this.rarity.formatting)
    if (this.contains(DataComponentTypes.CUSTOM_NAME)) {
        mutableText.formatted(Formatting.ITALIC)
    }

    return mutableText
}

fun DrawContext.translateToTopOptional() {
    this.matrices.translate(0f, 0f, 10000f)
}