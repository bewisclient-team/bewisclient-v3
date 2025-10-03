package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.core.wrapper.TextWrapper
import net.minecraft.text.MutableText
import net.minecraft.text.Text

inline fun <T> catch(block: () -> T, or: T) = catch(block) ?: or

inline fun <T> catch(block: () -> T) = try {
    block()
} catch (_: Throwable) {
    null
}

fun <T> T.staticFun(): () -> T = { this }

fun Int.toText(): MutableText = Text.literal(this.toString())

infix fun <T> Boolean.then(other: T): T? = if (this) other else null

fun BewisclientID.toTranslationKey(): String {
    return this.namespace + "." + this.path
}

fun BewisclientID.toTranslationKey(prefix: String): String {
    return prefix + "." + this.toTranslationKey()
}

fun BewisclientID.toTranslationKey(prefix: String, suffix: String): String {
    return prefix + "." + this.toTranslationKey() + "." + suffix
}