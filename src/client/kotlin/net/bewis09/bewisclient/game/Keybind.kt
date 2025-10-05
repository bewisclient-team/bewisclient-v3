@file:Suppress("UnusedImport")

package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.core.registerKeybind
import net.minecraft.client.util.InputUtil

open class Keybind(default: Int, id: String, name: String, val action: (() -> Unit)?, val tick: ((isPressed: Boolean) -> Unit)? = null) {
    constructor(default: Int, id: String, name: String, action: (() -> Unit)) : this(default, id, name, action = action, tick = null)

    val keyBinding = registerKeybind(
        registerTranslation("key.$id", name), InputUtil.Type.KEYSYM, default
    )

    fun isPressed(): Boolean {
        return keyBinding.isPressed
    }
}