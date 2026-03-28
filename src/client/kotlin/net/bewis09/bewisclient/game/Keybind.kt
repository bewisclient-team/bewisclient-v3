@file:Suppress("UnusedImport")

package net.bewis09.bewisclient.game

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.core.registerKeybind

open class Keybind(default: Int, id: String, name: String, val action: (() -> Unit)?, val tick: ((isPressed: Boolean) -> Unit)? = null) {
    constructor(default: Int, id: String, name: String, action: (() -> Unit)) : this(default, id, name, action = action, tick = null)

    val keyBinding = registerKeybind(
        registerTranslation("key.$id", name), InputConstants.Type.KEYSYM, default
    )

    fun isPressed(): Boolean {
        return keyBinding.isDown
    }
}