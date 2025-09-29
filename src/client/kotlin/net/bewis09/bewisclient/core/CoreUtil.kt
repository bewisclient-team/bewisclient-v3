package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.logic.BewisclientInterface
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry

object CoreUtil: BewisclientInterface {
    fun isKeyPressed(key: Int): Boolean {
        return InputUtil.isKeyPressed(client.window, key)
    }

    fun registerWidget(id: Identifier, widget: (context: DrawContext) -> Unit) = HudElementRegistry.addLast(id) { context, _ -> widget(context) }
}