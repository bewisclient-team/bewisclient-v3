package net.bewis09.bewisclient.core

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface
import net.bewis09.bewisclient.version.Identifier
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.ChatFormatting
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.DefaultedRegistry
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.util.profiling.Profiler
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.item.component.TooltipDisplay
import org.joml.Quaternionf
import java.io.File
import java.util.function.Consumer
import java.util.stream.Stream

fun Horse.getColor(): String = this.variant.name.lowercase()

fun Component.setFont(id: Identifier?): MutableComponent {
    return (this as? MutableComponent ?: this.copy()).withStyle { it.withFont(id ?: ScreenDrawingInterface.BEWISCLIENT_FONT) }
}

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

fun registerKeybind(translation: String, type: InputConstants.Type, default: Int): KeyMapping {
    return KeyMapping(
        translation, type, default, KeyMapping.CATEGORY_MISC
    )
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