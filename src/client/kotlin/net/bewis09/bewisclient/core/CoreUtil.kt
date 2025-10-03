package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.core.wrapper.wrap
import net.bewis09.bewisclient.interfaces.BreakingProgressAccessor
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text

object CoreUtil {
    fun isKeyPressed(key: Int): Boolean {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().window, key)
    }

    fun registerWidget(id: BewisclientID, widget: (context: DrawContext) -> Unit) = HudElementRegistry.addLast(id.toIdentifier()) { context, _ -> widget(context) }

    fun getBreakingProgress() = (MinecraftClient.getInstance().interactionManager as BreakingProgressAccessor?)?.getCurrentBreakingProgress() ?: 0f

    fun sendMessage(title: Text, bool: Boolean) {
        MinecraftClient.getInstance().player?.sendMessage(title, bool)
    }

    fun isInWorld() = MinecraftClient.getInstance().world != null

    fun getWorld() = MinecraftClient.getInstance().world?.wrap()
}