package net.bewis09.bewisclient.interfaces

import com.mojang.blaze3d.platform.InputConstants

interface KeyBindingAccessor {
    fun getKey(): InputConstants.Key
}

interface BreakingProgressAccessor {
    fun getDestroyProgress(): Float
}