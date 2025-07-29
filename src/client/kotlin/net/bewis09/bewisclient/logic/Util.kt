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

object TextColors {
    const val BLACK = "§0"
    const val DARK_BLUE = "§1"
    const val DARK_GREEN = "§2"
    const val DARK_AQUA = "§3"
    const val DARK_RED = "§4"
    const val DARK_PURPLE = "§5"
    const val GOLD = "§6"
    const val GRAY = "§7"
    const val DARK_GRAY = "§8"
    const val BLUE = "§9"
    const val GREEN = "§a"
    const val AQUA = "§b"
    const val RED = "§c"
    const val LIGHT_PURPLE = "§d"
    const val YELLOW = "§e"
    const val WHITE = "§f"

    val COLORS = mapOf(
        "black" to BLACK,
        "dark_blue" to DARK_BLUE,
        "dark_green" to DARK_GREEN,
        "dark_aqua" to DARK_AQUA,
        "dark_red" to DARK_RED,
        "dark_purple" to DARK_PURPLE,
        "gold" to GOLD,
        "gray" to GRAY,
        "dark_gray" to DARK_GRAY,
        "blue" to BLUE,
        "green" to GREEN,
        "aqua" to AQUA,
        "red" to RED,
        "light_purple" to LIGHT_PURPLE,
        "yellow" to YELLOW,
        "white" to WHITE
    )
}