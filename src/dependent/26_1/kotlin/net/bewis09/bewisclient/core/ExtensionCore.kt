package net.bewis09.bewisclient.core

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.GameVersion
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.Minecraft
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.cursor.StandardCursors
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.item.Item
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resource.metadata.ResourceMetadataSerializer
import net.minecraft.text.MutableText
import net.minecraft.text.StyleSpriteSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.resources.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.profiler.Profilers
import net.minecraft.world.item.ItemStack
import java.io.File
import java.util.function.Consumer

fun LivingEntity.pos(): Vec3d = this.entityPos

fun HorseEntity.getColor(): String = this.horseColor.asString().lowercase()

var LivingEntity.beforeHeadYaw: Float
    get() = this.lastHeadYaw
    set(value) {
        this.lastHeadYaw = value
    }

fun Text.setFont(id: Identifier?): MutableText {
    return (this as? MutableText ?: this.copy()).styled { it.withFont(StyleSpriteSource.Font((id ?: ScreenDrawingInterface.BEWISCLIENT_FONT))) }
}

fun GuiGraphics.pop() {
    this.pose().popMatrix()
}

fun GuiGraphics.push() {
    this.pose().pushMatrix()
}

fun GuiGraphics.translate(x: Float, y: Float) {
    this.pose().translate(x, y)
}

fun GuiGraphics.scale(x: Float, y: Float) {
    this.pose().scale(x, y)
}

fun GuiGraphics.rotate(angle: Float) {
    this.pose().rotate(angle)
}

fun GuiGraphics.drawTexture(
    texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
) = this.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)

fun GuiGraphics.translateToTopOptional() = Unit

fun GuiGraphics.drawItemOverlay(textRenderer: Font, itemStack: ItemStack, x: Int, y: Int) {
    this.drawStackOverlay(textRenderer, itemStack, x, y)
}

fun Minecraft.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.registerTexture(
        identifier, NativeImageBackedTexture({ identifier.toString() }, image)
    )
}

object Profiler {
    fun push(name: String) = Profilers.get().push(name)

    fun pop() = Profilers.get().pop()
}

fun registerKeybind(translation: String, type: InputConstants.Type, default: Int): KeyMapping {
    return KeyMapping(
        translation, type, default, KeyMapping.Category.MISC
    )
}

fun Minecraft.isKeyPressed(key: Int): Boolean {
    return InputConstants.isKeyDown(this.window, key)
}

fun registerWidget(id: Identifier, widget: (context: DrawContext) -> Unit) = HudElementRegistry.addLast(id) { context, _ -> widget(context) }

fun ItemStack.appendTooltip(textConsumer: Consumer<Component>) {
    this.appendTooltip(Item.TooltipContext.DEFAULT, TooltipDisplayComponent.DEFAULT, null, TooltipType.BASIC, textConsumer)
}

fun ItemStack.getItemFormattedName(): Component {
    val mutableText: MutableComponent = Component.empty().append(this.name).formatted(this.rarity.formatting)
    if (this.contains(DataComponentTypes.CUSTOM_NAME)) {
        mutableText.formatted(Formatting.ITALIC)
    }

    return mutableText
}

val model by lazy {
    PlayerEntityModel(
        EntityRendererFactory.Context(
            Minecraft.getInstance().entityRenderDispatcher,
            Minecraft.getInstance().itemModelResolver,
            Minecraft.getInstance().mapRenderer,
            Minecraft.getInstance().blockRenderer,
            Minecraft.getInstance().resourceManager,
            Minecraft.getInstance().loadedEntityModels,
            null,
            Minecraft.getInstance().atlasManager,
            Minecraft.getInstance().textRenderer,
            null
        ).getPart(EntityModelLayers.PLAYER_CAPE), false
    )
}

fun ScreenDrawing.drawCape(identifier: Identifier, x: Int, y: Int, width: Int, height: Int) {
    val xOffset = (width * (255 - this.getCurrentColorModifier().alpha)) / 127
    this.enableScissors(x - 8, y - 8, width + 16, height + 16)
    this.guiGraphics.addPlayerSkin(model, identifier, height.toFloat() * 0.9f, 18f, -195f, -10f, x - xOffset, y, x + (width * 1.13).toInt() - xOffset, y + (height * 1.13).toInt())
    this.disableScissors()
}

fun ScreenDrawing.setCursorPointer() {
    guiGraphics.setCursor(StandardCursors.POINTING_HAND)
}

fun ScreenDrawing.drawGuiTexture(
    texture: Identifier,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.guiGraphics.drawGuiTexture(
        RenderPipelines.GUI_TEXTURED,
        texture,
        x,
        y,
        width,
        height
    )
}

val GameVersion.name: String
    get() = this.name()

typealias IndependentResourceMetadataSerializer<T> = ResourceMetadataSerializer<T>

fun Minecraft.takePanoramaFull(file: File): Text = this.takePanorama(file)