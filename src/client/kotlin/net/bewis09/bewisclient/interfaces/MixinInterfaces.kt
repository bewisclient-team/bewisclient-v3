package net.bewis09.bewisclient.interfaces

import net.minecraft.client.render.BufferBuilderStorage
import net.minecraft.client.util.InputUtil

interface KeyBindingAccessor {
    fun getBoundKey(): InputUtil.Key
}

interface BreakingProgressAccessor {
    fun getCurrentBreakingProgress(): Float
}

interface GameRendererBuffersAccessor {
    fun getBuffers(): BufferBuilderStorage
}