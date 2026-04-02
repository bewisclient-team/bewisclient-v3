package net.bewis09.bewisclient.core

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.bewis09.bewisclient.util.color.color
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.WorldVersion
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.DefaultedRegistry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import java.io.File
import java.util.function.Consumer

fun Component.setFont(id: Identifier?): MutableComponent {
    return (this as? MutableComponent ?: this.copy()).withStyle { it.withFont(id ?: ScreenDrawingInterface.BEWISCLIENT_FONT) }
}

fun GuiGraphics.pop() {
    this.pose().popPose()
}

fun GuiGraphics.push() {
    this.pose().pushPose()
}

fun GuiGraphics.translate(x: Float, y: Float) {
    this.pose().translate(x, y, 0f)
}

fun GuiGraphics.scale(x: Float, y: Float) {
    this.pose().scale(x, y, 1f)
}

fun GuiGraphics.rotate(angle: Float) = this.pose().mulPose(Quaternionf().rotateZ(angle))

fun GuiGraphics.drawTexture(
    texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
) {
    RenderSystem.enableBlend()
    RenderSystem.setShaderColor(color.toLong().color.red / 255f, color.toLong().color.green / 255f, color.toLong().color.blue / 255f, color.toLong().color.alpha / 255f)
    this.blit(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight)
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
}

fun GuiGraphics.drawItemOverlay(textRenderer: Font, itemStack: ItemStack, x: Int, y: Int) {
    this.renderItemDecorations(textRenderer, itemStack, x, y)
}

fun Minecraft.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.register(
        identifier, DynamicTexture(image)
    )
}

object Profiler {
    fun push(name: String) = Minecraft.getInstance().profiler.push(name)

    fun pop() = Minecraft.getInstance().profiler.pop()
}

fun registerKeybind(translation: String, type: InputConstants.Type, default: Int): KeyMapping {
    return KeyMapping(
        translation, type, default, KeyMapping.CATEGORY_MISC
    )
}

fun Minecraft.isKeyPressed(key: Int): Boolean {
    return InputConstants.isKeyDown(this.window.window, key)
}

fun registerWidget(id: Identifier, widget: (context: GuiGraphics) -> Unit) = HudRenderCallback.EVENT.register { context, _ -> if (!Minecraft.getInstance().options.hideGui) widget(context) }

fun ItemStack.appendTooltip(textConsumer: Consumer<Component>) {
    for ((index, text) in this.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).withIndex()) {
        if (index == 0) continue
        textConsumer.accept(text)
    }
}

fun ItemStack.getItemFormattedName(): Component {
    val mutableText: MutableComponent = Component.empty().append(this.hoverName).withStyle(this.rarity.color())
    if (this.has(DataComponents.CUSTOM_NAME)) {
        mutableText.withStyle(ChatFormatting.ITALIC)
    }

    return mutableText
}

fun GuiGraphics.translateToTopOptional() {
    this.pose().translate(0f, 0f, 10000f)
}

fun ScreenDrawing.drawCape(identifier: Identifier, x: Int, y: Int, width: Int, height: Int) {
    this.drawTextureRegion(identifier, x, y, 1f, 1f, width, height, 10, 16, 64, 32)
}

@Suppress("UnusedReceiverParameter")
fun ScreenDrawing.setCursorPointer() {}

fun ScreenDrawing.drawGuiTexture(
    texture: Identifier,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.guiGraphics.blitSprite(
        texture,
        x,
        y,
        width,
        height
    )
}

val WorldVersion.name: String
    get() = this.name

typealias IndependentResourceMetadataSerializer<T> = MetadataSectionSerializer<T>

fun Minecraft.takePanoramaFull(file: File): Component = this.grabPanoramixScreenshot(file, 1024, 1024)

fun isAllowedInIdentifier(char: Char) = Identifier.isAllowedInResourceLocation(char)

typealias Identifier = ResourceLocation

typealias Util = Util

fun <T> ResourceKey<T>.id(): Identifier = this.location()

fun <T> DefaultedRegistry<T>.getOrNull(id: Identifier): T? = this.getOptional(id).orElse(null)