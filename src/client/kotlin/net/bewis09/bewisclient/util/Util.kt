package net.bewis09.bewisclient.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.io.File

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

fun Int.toText(): Text = this.toString().toText()

inline infix fun <T> Boolean.then(other: () -> T): T? = if (this) other() else null

fun createIdentifier(namespace: String, path: String): Identifier = Identifier.of(namespace, path)

fun createIdentifier(path: String): Identifier = Identifier.of(path)