package net.bewis09.bewisclient.util

import net.bewis09.bewisclient.version.Identifier
import net.minecraft.core.DefaultedRegistry
import net.minecraft.network.chat.Component

inline fun <T> catch(block: () -> T, or: T) = catch(block) ?: or

inline fun <T> catch(block: () -> T) = try {
    block()
} catch (_: Throwable) {
    null
}

inline fun <T> catchAndPrint(block: () -> T) = try {
    block()
} catch (e: Throwable) {
    e.printStackTrace()
}

fun <T> T.staticFun(): () -> T = { this }

fun Int.toText(): Component = this.toString().toText()

inline infix fun <T> Boolean.then(other: () -> T): T? = if (this) other() else null

fun createIdentifier(namespace: String, path: String): Identifier = Identifier.tryBuild(namespace, path)!!

fun createIdentifier(path: String): Identifier = Identifier.tryParse(path)!!

fun <T: Any> DefaultedRegistry<T>.getOrNull(id: Identifier): T? = this.getOptional(id).orElse(null)

@Suppress("FunctionName")
fun `snake_toWord With Spaces`(str: String): String {
    return str.split("_".toRegex()).filter { it.isNotEmpty() }.joinToString(" ") {
        it.replaceFirstChar(Char::uppercaseChar)
    }
}

@Suppress("FunctionName")
fun snake_toCamelCase(str: String): String {
    return str.split("_".toRegex()).filter { it.isNotEmpty() }.joinToString("") {
        it.replaceFirstChar(Char::uppercaseChar)
    }
}