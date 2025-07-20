package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.game.KeybindingImplementer.CATEGORY
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class Keybind(default: Int, id: String, name: String, val action: () -> Unit) {
    val keyBinding = KeyBinding(
        registerTranslation("key.$id", name),
        InputUtil.Type.KEYSYM,
        default,
        CATEGORY.getKey()
    )
}