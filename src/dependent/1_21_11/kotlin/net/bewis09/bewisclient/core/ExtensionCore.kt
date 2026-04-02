package net.bewis09.bewisclient.core

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.platform.cursor.CursorTypes
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.ChatFormatting
import net.minecraft.WorldVersion
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.player.PlayerModel
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayerResolver
import net.minecraft.client.renderer.PlayerSkinRenderCache
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.resources.model.EquipmentAssetManager
import net.minecraft.core.DefaultedRegistry
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.metadata.MetadataSectionType
import net.minecraft.util.Util
import net.minecraft.util.profiling.Profiler
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.equine.Horse
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.phys.Vec3
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
    return (this as? MutableComponent ?: this.copy()).withStyle { it.withFont(FontDescription.Resource((id ?: ScreenDrawingInterface.BEWISCLIENT_FONT))) }
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
        translation, type, default, KeyMapping.Category.MISC
    )
}

fun Minecraft.isKeyPressed(key: Int): Boolean {
    return InputConstants.isKeyDown(this.window, key)
}

fun registerWidget(id: Identifier, widget: (context: GuiGraphics) -> Unit) = HudElementRegistry.addLast(id) { context, _ -> widget(context) }

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

val model by lazy {
    PlayerModel(
        Minecraft.getInstance().run {
            EntityRendererProvider.Context(
                entityRenderDispatcher,
                itemModelResolver,
                mapRenderer,
                blockRenderer,
                resourceManager,
                entityModels,
                EquipmentAssetManager(),
                atlasManager,
                font,
                PlayerSkinRenderCache(
                    textureManager,
                    skinManager,
                    LocalPlayerResolver(this, services().profileResolver())
                )
            ).bakeLayer(ModelLayers.PLAYER_CAPE)
        }, false
    )
}

fun ScreenDrawing.drawCape(identifier: Identifier, x: Int, y: Int, width: Int, height: Int) {
    val xOffset = (width * (255 - this.getCurrentColorModifier().alpha)) / 127
    this.enableScissors(x - 8, y - 8, width + 16, height + 16)
    this.guiGraphics.submitSkinRenderState(model, identifier, height.toFloat() * 0.9f, 18f, -195f, -10f, x - xOffset, y, x + (width * 1.13).toInt() - xOffset, y + (height * 1.13).toInt())
    this.disableScissors()
}

fun ScreenDrawing.setCursorPointer() {
    guiGraphics.requestCursor(CursorTypes.POINTING_HAND)
}

fun ScreenDrawing.drawGuiTexture(
    texture: Identifier,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.guiGraphics.blitSprite(
        RenderPipelines.GUI_TEXTURED,
        texture,
        x,
        y,
        width,
        height
    )
}

val WorldVersion.name: String
    get() = this.name()

fun Minecraft.takePanoramaFull(file: File): Component = this.grabPanoramixScreenshot(file)

fun isAllowedInIdentifier(char: Char) = Identifier.isAllowedInIdentifier(char)

typealias Identifier = net.minecraft.resources.Identifier

typealias Util = Util

fun <T: Any> ResourceKey<T>.id(): Identifier = this.identifier()

fun <T: Any> DefaultedRegistry<T>.getOrNull(id: Identifier): T? = this.getOptional(id).orElse(null)

typealias IndependentResourceMetadataSerializer<T> = MetadataSectionType<T>

typealias GuiGraphics = GuiGraphics

fun GuiGraphics.string(font: Font, text: Component, x: Int, y: Int, color: Int, shadow: Boolean) {
    this.drawString(font, text, x, y, color, shadow)
}

fun registerKeyBinding(keyBinding: KeyMapping) = KeyBindingHelper.registerKeyBinding(keyBinding)

fun GuiGraphics.renderItem(itemStack: ItemStack, x: Int, y: Int) {
    this.renderItem(itemStack, x, y)
}

typealias FabricDataOutput = FabricDataOutput

fun Minecraft.displayOverlayMessage(message: Component) = this.player?.displayClientMessage(message, true)

fun Minecraft.displaySystemMessage(message: Component) = this.player?.displayClientMessage(message, false)

typealias TooltipComponentCallback = TooltipComponentCallback

typealias ClientCommandManager = ClientCommandManager

val ClientLevel.dayTime
    get() = this.dayTime