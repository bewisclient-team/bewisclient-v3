package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.drawable.screen_drawing.ArgbColor
import net.bewis09.bewisclient.game.Translation
import java.awt.Color

inline fun <T> catch(block: () -> T, or: T) = try {
    block()
} catch (_: Throwable) {
    or
}

inline fun <T> catch(block: () -> T) = try {
    block()
} catch (_: Throwable) {
    null
}

enum class TextColor(val code: String) {
    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    DARK_PURPLE("§5"),
    GOLD("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLUE("§9"),
    GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    LIGHT_PURPLE("§d"),
    YELLOW("§e"),
    WHITE("§f");

    operator fun plus(other: String) = code + other
}

fun getBrightness(color: Int): Float {
    return Color(color).let {
        Color.RGBtoHSB(it.red, it.green, it.blue, null)[2]
    }
}

val colors = listOf(
    Color(0xFF0000, Translation("color.red", "Red")),
    Color(0x00FF00, Translation("color.green", "Green")),
    Color(0x0000FF, Translation("color.blue", "Blue")),
    Color(0xFFFF00, Translation("color.yellow", "Yellow")),
    Color(0x00FFFF, Translation("color.cyan", "Cyan")),
    Color(0xFF00FF, Translation("color.magenta", "Magenta")),
    Color(0xFFFFFF, Translation("color.white", "White")),
    Color(0x000000, Translation("color.black", "Black")),
    Color(0x808080, Translation("color.gray", "Gray")),
    Color(0x404040, Translation("color.dark_gray", "Dark Gray")),
    Color(0xC0C0C0, Translation("color.light_gray", "Light Gray")),
    Color(0xFFA500, Translation("color.orange", "Orange")),
    Color(0xFFC0CB, Translation("color.pink", "Pink")),
    Color(0x800080, Translation("color.purple", "Purple")),
    Color(0xA52A2A, Translation("color.brown", "Brown")),
    Color(0x00FF00, Translation("color.lime", "Lime")),
    Color(0x008080, Translation("color.teal", "Teal")),
    Color(0x000080, Translation("color.navy", "Navy")),
    Color(0x808000, Translation("color.olive", "Olive")),
    Color(0x800000, Translation("color.maroon", "Maroon")),
    Color(0xFFD700, Translation("color.gold", "Gold")),
    Color(0xC0C0C0, Translation("color.silver", "Silver")),
    Color(0x4B0082, Translation("color.indigo", "Indigo")),
    Color(0xEE82EE, Translation("color.violet", "Violet")),
    Color(0xFF7F50, Translation("color.coral", "Coral")),
    Color(0xFA8072, Translation("color.salmon", "Salmon")),
    Color(0xF0E68C, Translation("color.khaki", "Khaki")),
    Color(0xDDA0DD, Translation("color.plum", "Plum")),
    Color(0xDA70D6, Translation("color.orchid", "Orchid")),
    Color(0x40E0D0, Translation("color.turquoise", "Turquoise")),
    Color(0xDC143C, Translation("color.crimson", "Crimson")),
    Color(0xA0522D, Translation("color.sienna", "Sienna")),
    Color(0xD2691E, Translation("color.chocolate", "Chocolate")),
    Color(0xD2B48C, Translation("color.tan", "Tan")),
    Color(0xFFDAB9, Translation("color.peach", "Peach")),
    Color(0xE6E6FA, Translation("color.lavender", "Lavender")),
    Color(0x98FF98, Translation("color.mint", "Mint")),
    Color(0x87CEEB, Translation("color.sky_blue", "Sky Blue")),
    Color(0x00FFFF, Translation("color.aqua", "Aqua")),
    Color(0x228B22, Translation("color.forest_green", "Forest Green")),
    Color(0x4169E1, Translation("color.royal_blue", "Royal Blue")),
    Color(0xFF1493, Translation("color.deep_pink", "Deep Pink")),
    Color(0xB22222, Translation("color.firebrick", "Firebrick")),
    Color(0x4682B4, Translation("color.steel_blue", "Steel Blue")),
    Color(0x006400, Translation("color.dark_green", "Dark Green")),
    Color(0x8B0000, Translation("color.dark_red", "Dark Red")),
    Color(0x00008B, Translation("color.dark_blue", "Dark Blue")),
    Color(0xFF69B4, Translation("color.hot_pink", "Hot Pink")),
    Color(0x32CD32, Translation("color.lime_green", "Lime Green")),
    Color(0xFF4500, Translation("color.orange_red", "Orange Red")),
    Color(0x2E8B57, Translation("color.sea_green", "Sea Green")),
    Color(0x708090, Translation("color.slate_gray", "Slate Gray")),
    Color(0x9370DB, Translation("color.medium_purple", "Medium Purple")),
    Color(0xFF8C00, Translation("color.dark_orange", "Dark Orange")),
    Color(0xADD8E6, Translation("color.light_blue", "Light Blue")),
    Color(0x90EE90, Translation("color.light_green", "Light Green")),
    Color(0xFFB6C1, Translation("color.light_pink", "Light Pink")),
    Color(0xF5DEB3, Translation("color.wheat", "Wheat")),
)

class Color(val color: Int, val translation: Translation)

fun <T> T.staticFun(): () -> T = { this }

infix fun Float.within(@ArgbColor pair: Pair<Int, Int>): Int {
    val startRed = (pair.first shr 16) and 0xFF
    val startGreen = (pair.first shr 8) and 0xFF
    val startBlue = pair.first and 0xFF

    val endRed = (pair.second shr 16) and 0xFF
    val endGreen = (pair.second shr 8) and 0xFF
    val endBlue = pair.second and 0xFF

    val red = ((startRed + (endRed - startRed) * this).toInt() shl 16)
    val green = ((startGreen + (endGreen - startGreen) * this).toInt() shl 8)
    val blue = (startBlue + (endBlue - startBlue) * this).toInt()

    return red or green or blue
}

infix fun Int.multiplyColor(other: Int): Int {
    val a = ((this shr 24) and 0xFF) * ((other shr 24) and 0xFF) / 255
    val r = ((this shr 16) and 0xFF) * ((other shr 16) and 0xFF) / 255
    val g = ((this shr 8) and 0xFF) * ((other shr 8) and 0xFF) / 255
    val b = (this and 0xFF) * (other and 0xFF) / 255
    return (a shl 24) or (r shl 16) or (g shl 8) or b
}