package net.bewis09.bewisclient.logic

fun <T> catch(block: () -> T, or: T) = try {
    block()
} catch (_: Throwable) {
    or
}

fun <T> catch(block: () -> T) = try {
    block()
} catch (_: Throwable) {
    null
}