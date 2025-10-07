package net.bewis09.bewisclient.interfaces

import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl
import net.minecraft.client.util.InputUtil

interface KeyBindingAccessor {
    fun getBoundKey(): InputUtil.Key
}

interface BreakingProgressAccessor {
    fun getCurrentBreakingProgress(): Float
}

interface WorldRendererQueueAccessor {
    fun getEntityRenderCommandQueue(): OrderedRenderCommandQueueImpl?
}