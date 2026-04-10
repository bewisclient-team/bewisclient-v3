// @VersionReplacement

package net.bewis09.bewisclient.version

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.cursor.CursorTypes
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.WorldVersion
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import java.io.File

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

// @[26.1] screen @[] gui.screen()
fun getScreen() = Minecraft.getInstance()./*[@]*/gui.screen()/*[!@]*/

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

val WorldVersion.name: String
    // @[1.21.5] name @[] name()
    get() = this./*[@]*/name()/*[!@]*/

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

// @[1.21.10] isAllowedInResourceLocation @[] isAllowedInIdentifier
fun isAllowedInIdentifier(char: Char) = Identifier./*[@]*/isAllowedInIdentifier/*[!@]*/(char)

// @[1.21.10] location() @[] identifier()
fun <T: Any> ResourceKey<T>.id(): Identifier = this./*[@]*/identifier()/*[!@]*/

// @[1.21.8] @[] guiGraphics.requestCursor(CursorTypes.POINTING_HAND)
fun ScreenDrawing.setCursorPointer() = /*[@]*/guiGraphics.requestCursor(CursorTypes.POINTING_HAND)/*[!@]*/

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