package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.core.CoreUtil
import net.minecraft.text.Text

interface InGameLogic {
    fun showTitle(title: Text) {
        core.sendMessage(title, true)
    }

    val core: CoreUtil
        get() = CoreUtil
}