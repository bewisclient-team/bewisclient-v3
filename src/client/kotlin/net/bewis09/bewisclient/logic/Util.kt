package net.bewis09.bewisclient.logic

import net.minecraft.text.MutableText
import net.minecraft.text.Text

inline fun <T> catch(block: () -> T, or: T) = catch(block) ?: or

inline fun <T> catch(block: () -> T) = try {
    block()
} catch (_: Throwable) {
    null
}

fun <T> T.staticFun(): () -> T = { this }

fun String.toText(): MutableText = Text.literal(this)

fun Int.toText(): MutableText = Text.literal(this.toString())

infix fun <T> Boolean.then(other: T): T? = if (this) other else null