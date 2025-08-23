package net.bewis09.bewisclient.interfaces

import net.minecraft.client.util.InputUtil

interface KeyBindingAccessor {
    fun getBoundKey(): InputUtil.Key
}