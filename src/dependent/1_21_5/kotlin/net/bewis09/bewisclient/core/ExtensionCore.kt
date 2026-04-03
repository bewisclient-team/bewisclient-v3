package net.bewis09.bewisclient.core

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.WorldVersion
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.DefaultedRegistry
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.metadata.MetadataSectionType
import net.minecraft.util.profiling.Profiler
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import java.io.File
import java.util.function.Consumer

fun LivingEntity.pos(): Vec3 = this.position()

fun Horse.getColor(): String = this.variant.name.lowercase()

var LivingEntity.beforeHeadYaw: Float
    get() = this.yHeadRotO
    set(value) {
        this.yHeadRotO = value
    }

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
) = this.blit({ texture: Identifier -> RenderType.guiTextured(texture) }, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)

fun GuiGraphics.translateToTopOptional() {
    this.pose().translate(0f, 0f, 10000f)
}

fun GuiGraphics.drawItemOverlay(textRenderer: Font, itemStack: ItemStack, x: Int, y: Int) {
    this.renderItemDecorations(textRenderer, itemStack, x, y)
}

fun Minecraft.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.register(
        identifier, DynamicTexture({ identifier.toString() }, image)
    )
}

object Profiler {
    fun push(name: String) = Profiler.get().push(name)

    fun pop() = Profiler.get().pop()
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
    this.addDetailsToTooltip(
        Item.TooltipContext.EMPTY,
        TooltipDisplay.DEFAULT,
        null,
        TooltipFlag.NORMAL,
        textConsumer
    )
}

fun ItemStack.getItemFormattedName(): Component {
    val mutableText: MutableComponent = Component.empty().append(this.itemName).withStyle(this.rarity.color())
    if (this.has(DataComponents.CUSTOM_NAME)) {
        mutableText.withStyle(ChatFormatting.ITALIC)
    }

    return mutableText
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
        { texture: Identifier -> RenderType.guiTextured(texture) },
        texture,
        x,
        y,
        width,
        height
    )
}

val WorldVersion.name: String
    get() = this.name

fun Minecraft.takePanoramaFull(file: File): Component = this.grabPanoramixScreenshot(file, 1024, 1024)

fun isAllowedInIdentifier(char: Char) = Identifier.isAllowedInResourceLocation(char)

typealias Identifier = ResourceLocation

typealias Util = Util

fun <T> ResourceKey<T>.id(): Identifier = this.location()

fun <T> DefaultedRegistry<T>.getOrNull(id: Identifier): T? = this.getOptional(id).orElse(null)

typealias IndependentResourceMetadataSerializer<T> = MetadataSectionType<T>

typealias GuiGraphics = GuiGraphics

fun GuiGraphics.string(font: Font, text: Component, x: Int, y: Int, color: Int, shadow: Boolean) {
    this.drawString(font, text, x, y, color, shadow)
}

fun registerKeyBinding(keyBinding: KeyMapping): KeyMapping = KeyBindingHelper.registerKeyBinding(keyBinding)

fun GuiGraphics.drawItem(itemStack: ItemStack, x: Int, y: Int) {
    this.renderItem(itemStack, x, y)
}

typealias FabricDataOutput = FabricDataOutput

fun Minecraft.displayOverlayMessage(message: Component) = this.player?.displayClientMessage(message, true)

fun Minecraft.displaySystemMessage(message: Component) = this.player?.displayClientMessage(message, false)

typealias TooltipComponentCallback = TooltipComponentCallback

typealias ClientCommandManager = ClientCommandManager

val ClientLevel.clockTime
    get() = this.dayTime