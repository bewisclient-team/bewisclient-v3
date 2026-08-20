// @VersionReplacement

package net.bewis09.bewisclient.version

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.ChatFormatting
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.io.File
import java.util.function.Consumer

fun GuiGraphics.pop() {
    // @[1.21.5] popPose @[] popMatrix
    this.pose()./*[@]*/popMatrix/*[!@]*/()
}

fun GuiGraphics.push() {
    // @[1.21.5] pushPose @[] pushMatrix
    this.pose()./*[@]*/pushMatrix/*[!@]*/()
}

fun GuiGraphics.translate(x: Float, y: Float) {
    // @[1.21.5] translate(x, y, 0f) @[] translate(x, y)
    this.pose()./*[@]*/translate(x, y)/*[!@]*/
}

fun GuiGraphics.scale(x: Float, y: Float) {
    // @[1.21.5] scale(x, y, 1f) @[] scale(x, y)
    this.pose()./*[@]*/scale(x, y)/*[!@]*/
}

val ClientLevel.clockTime
    // @[1.21.11] dayTime @[] overworldClockTime
    get() = this./*[@]*/overworldClockTime/*[!@]*/

// @[26.1] setScreen @[] gui.setScreen
fun setScreen(screen: Screen?) = Minecraft.getInstance()./*[@]*/gui.setScreen/*[!@]*/(screen)

@Suppress("UNNECESSARY_SAFE_CALL")
// @[26.1] screen @[] gui?.screen()
fun getScreen() = Minecraft.getInstance()./*[@]*/gui?.screen()/*[!@]*/

// @[1.21.11] net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding @[] net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping
fun registerKeyBinding(keyMapping: KeyMapping) = /*[@]*/net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping/*[!@]*/(keyMapping)

fun GuiGraphics.string(font: Font, text: Component, x: Int, y: Int, color: Int, shadow: Boolean) {
    // @[1.21.11] drawString @[] text
    this./*[@]*/text/*[!@]*/(font, text, x, y, color, shadow)
}

fun GuiGraphics.drawItem(itemStack: ItemStack, x: Int, y: Int) {
    // @[1.21.11] renderItem @[] item
    this./*[@]*/item/*[!@]*/(itemStack, x, y)
}

object Profiler {
    // @[1.21.1] Minecraft.getInstance().profiler @[] net.minecraft.util.profiling.Profiler.get()
    fun push(name: String) = /*[@]*/net.minecraft.util.profiling.Profiler.get()/*[!@]*/.push(name)

    // @[1.21.1] Minecraft.getInstance().profiler @[] net.minecraft.util.profiling.Profiler.get()
    fun pop() = /*[@]*/net.minecraft.util.profiling.Profiler.get()/*[!@]*/.pop()
}

fun Minecraft.isKeyPressed(key: Int): Boolean {
    // @[1.21.8] this.window.window @[] this.window
    return InputConstants.isKeyDown(/*[@]*/this.window/*[!@]*/, key)
}

// @[1.21.5] , 1024, 1024) @[] )
fun Minecraft.takePanoramaFull(file: File): Component = this.grabPanoramixScreenshot(file/*[@]*/)/*[!@]*/

// @[1.21.8] Unit @[] requestCursor(com.mojang.blaze3d.platform.cursor.CursorTypes.POINTING_HAND)
fun GuiGraphics.setCursorPointer() = /*[@]*/requestCursor(com.mojang.blaze3d.platform.cursor.CursorTypes.POINTING_HAND)/*[!@]*/

fun ScreenDrawing.drawGuiTexture(
    texture: Identifier,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.guiGraphics.blitSprite(
        // @[1.21.1] @[1.21.5] { texture: Identifier -> net.minecraft.client.renderer.RenderType.guiTextured(texture) }, @[] net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
        /*[@]*/net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,/*[!@]*/
        texture,
        x,
        y,
        width,
        height
    )
}

fun GuiGraphics.rotate(angle: Float) {
    // @[1.21.5] mulPose(org.joml.Quaternionf().rotateZ(angle)) @[] rotate(angle)
    this.pose()./*[@]*/rotate(angle)/*[!@]*/
}

val model by lazy {
    // @[1.21.1] net.minecraft.client.model.PlayerModel<net.minecraft.world.entity.player.Player> @[1.21.10] net.minecraft.client.model.PlayerModel @[26.1] net.minecraft.client.model.player.PlayerModel @[] net.minecraft.client.model.Model.Simple
    /*[@]*/net.minecraft.client.model.Model.Simple/*[!@]*/(
        Minecraft.getInstance().entityModels.run {
            // @[1.21.1] PLAYER @[] PLAYER_CAPE
            bakeLayer(ModelLayers./*[@]*/PLAYER_CAPE/*[!@]*/)
        }
        // @[26.1] , false) @[] ) { net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(it) }
    /*[@]*/) { net.minecraft.client.renderer.rendertype.RenderTypes.entitySolid(it) }/*[!@]*/
}

// @[1.21.5] this.pose().translate(0f, 0f, 10000f) @[] Unit
fun GuiGraphics.translateToTopOptional() = /*[@]*/Unit/*[!@]*/

fun Component.setFont(id: Identifier?): MutableComponent {
    // @[1.21.8] id ?: ScreenDrawingInterface.BEWISCLIENT_FONT @[] net.minecraft.network.chat.FontDescription.Resource((id ?: ScreenDrawing.BEWISCLIENT_FONT))
    return (this as? MutableComponent ?: this.copy()).withStyle { it.withFont(/*[@]*/net.minecraft.network.chat.FontDescription.Resource((id ?: ScreenDrawing.BEWISCLIENT_FONT))/*[!@]*/) }
}

fun GuiGraphics.drawItemOverlay(textRenderer: Font, itemStack: ItemStack, x: Int, y: Int) {
    // @[1.21.11] renderItemDecorations @[] itemDecorations
    this./*[@]*/itemDecorations/*[!@]*/(textRenderer, itemStack, x, y)
}

// @[1.21.8] "key.category.${name.namespace}.${name.path}" @[] KeyMapping.Category.register(name)
fun createCategory(name: Identifier) = /*[@]*/KeyMapping.Category.register(name)/*[!@]*/

fun ItemStack.getItemFormattedName(): Component {
    // @[1.21.1] hoverName @[] hoverName
    val mutableText: MutableComponent = Component.empty().append(this./*[@]*/hoverName/*[!@]*/).withStyle(this.rarity.color())
    if (this.has(DataComponents.CUSTOM_NAME)) {
        mutableText.withStyle(ChatFormatting.ITALIC)
    }

    return mutableText
}

fun Minecraft.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.register(
        // @[1.21.4] image @[] { identifier.toString() }, image
        identifier, DynamicTexture(/*[@]*/{ identifier.toString() }, image/*[!@]*/)
    )
}

// @[1.21.5] net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register { context, _ -> if (!Minecraft.getInstance().options.hideGui) widget(context) } @[] net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(id) { context, _ -> widget(context) }
fun registerWidget(id: Identifier, widget: (context: GuiGraphics) -> Unit) = /*[@]*/net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(id) { context, _ -> widget(context) }/*[!@]*/

// @[1.21.4] this.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).filterIndexed { index, _ -> index != 0 }.forEach(textConsumer::accept) @[] this.addDetailsToTooltip(Item.TooltipContext.EMPTY, net.minecraft.world.item.component.TooltipDisplay.DEFAULT, null, TooltipFlag.NORMAL, textConsumer)
fun ItemStack.appendTooltip(textConsumer: Consumer<Component>) {
    /*[@]*/this.addDetailsToTooltip(Item.TooltipContext.EMPTY, net.minecraft.world.item.component.TooltipDisplay.DEFAULT, null, TooltipFlag.NORMAL, textConsumer)/*[!@]*/
}

fun GuiGraphics.drawTexture(
    texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
) {
    enableBlend()
    setShaderColor(Color(color).red / 255f, Color(color).green / 255f, Color(color).blue / 255f, Color(color).alpha / 255f)
    // @[1.21.1] this.blit(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight) @[1.21.5] this.blit({ texture: Identifier -> net.minecraft.client.renderer.RenderType.guiTextured(texture) }, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color) @[] this.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)
    /*[@]*/this.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)/*[!@]*/
    setShaderColor(1f, 1f, 1f, 1f)
    disableBlend()
}

// @[1.21.1] com.mojang.blaze3d.systems.RenderSystem.enableBlend() @[] Unit
fun enableBlend() = /*[@]*/Unit/*[!@]*/

// @[1.21.1] com.mojang.blaze3d.systems.RenderSystem.disableBlend() @[] Unit
fun disableBlend() = /*[@]*/Unit/*[!@]*/

// @[1.21.1] com.mojang.blaze3d.systems.RenderSystem.setShaderColor(r, g, b, a) @[] Unit
fun setShaderColor(r: Float, g: Float, b: Float, a: Float) = /*[@]*/Unit/*[!@]*/

fun ScreenDrawing.drawCape(identifier: Identifier, x: Int, y: Int, width: Int, height: Int) {
    val xOffset = (width * (255 - this.getCurrentColorModifier().alpha)) / 127
    this.enableScissors(x - 8, y - 8, width + 16, height + 16)
    // @[1.21.8] this.drawTextureRegion(identifier, x, y, 1f, 1f, width, height, 10, 16, 64, 32) @[]
    /*[@]*//*[!@]*/
    // @[1.21.8] @[1.21.11] this.guiGraphics.submitSkinRenderState(model, identifier, height.toFloat() * 0.9f, 18f, -195f, -10f, x - xOffset, y, x + (width * 1.13).toInt() - xOffset, y + (height * 1.13).toInt()) @[] this.guiGraphics.skin(model, identifier, height.toFloat() * 0.9f, 18f, -195f, -10f, x - xOffset, y, x + (width * 1.13).toInt() - xOffset, y + (height * 1.13).toInt())
    /*[@]*/this.guiGraphics.skin(model, identifier, height.toFloat() * 0.9f, 18f, -195f, -10f, x - xOffset, y, x + (width * 1.13).toInt() - xOffset, y + (height * 1.13).toInt())/*[!@]*/
    this.disableScissors()
}