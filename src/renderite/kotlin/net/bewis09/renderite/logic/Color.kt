package net.bewis09.renderite.logic

@JvmInline
value class Color(val argb: Int) {
    companion object {
        val WHITE = Color(255, 255, 255)
        val LIGHT_GRAY = Color(170, 170, 170)
        val GRAY = Color(128, 128, 128)
        val DARK_GRAY = Color(64, 64, 64)
        val BLACK = Color(0, 0, 0)
        val RED = Color(255, 0, 0)
        val PINK = Color(255, 175, 175)
        val ORANGE = Color(255, 200, 0)
        val YELLOW = Color(255, 255, 0)
        val GREEN = Color(0, 255, 0)
        val MAGENTA = Color(255, 0, 255)
        val CYAN = Color(0, 255, 255)
        val BLUE = Color(0, 0, 255)

        val TRANSPARENT = Color(0, 0, 0) alpha 0f

        fun rgb(rgb: Int): Color = Color(rgb or (255 shl 24))
    }

    constructor(rgb: Int, alpha: Float) : this(rgb or (((alpha * 255).toInt()) shl 24))

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(
        ((alpha and 0xFF) shl 24) + ((red and 0xFF) shl 16) + ((green and 0xFF) shl 8) + (blue and 0xFF)
    )

    constructor(red: Float, green: Float, blue: Float, alpha: Float = 1f) : this(
        (((alpha * 255).toInt() and 0xFF) shl 24) + (((red * 255).toInt() and 0xFF) shl 16) + (((green * 255).toInt() and 0xFF) shl 8) + ((blue * 255).toInt() and 0xFF)
    )

    constructor(hue: Float, sat: Float, bri: Float) : this(
        java.awt.Color.HSBtoRGB(hue, sat, bri)
    )

    val alpha: Int
        get() = this.argb shr 24 and 0xFF

    val red: Int
        get() = this.argb shr 16 and 0xFF

    val green: Int
        get() = this.argb shr 8 and 0xFF

    val blue: Int
        get() = this.argb and 0xFF

    val brightness: Float
        get() = hsb()[2]

    val saturation: Float
        get() = hsb()[1]

    val hue: Float
        get() = hsb()[0]

    fun withBrightness(brightness: Float): Color {
        val hsb = hsb()
        return Color(hsb[0], hsb[1], brightness)
    }

    fun withSaturation(saturation: Float): Color {
        val hsb = hsb()
        return Color(hsb[0], saturation, hsb[2])
    }

    fun withHue(hue: Float): Color {
        val hsb = hsb()
        return Color(hue, hsb[1], hsb[2])
    }

    fun withAlpha(alpha: Int): Color {
        return Color(this.red, this.green, this.blue, alpha)
    }

    fun withRed(red: Int): Color {
        return Color(red, this.green, this.blue, this.alpha)
    }

    fun withGreen(green: Int): Color {
        return Color(this.red, green, this.blue, this.alpha)
    }

    fun withBlue(blue: Int): Color {
        return Color(this.red, this.green, blue, this.alpha)
    }

    infix fun alpha(alpha: Float): Color {
        return Color(this.red, this.green, this.blue, (alpha * 255).toInt())
    }

    fun hsb(): FloatArray {
        return java.awt.Color.RGBtoHSB(this.red, this.green, this.blue, null)
    }

    operator fun times(other: Color): Color {
        return Color(
            (this.red * other.red / 255),
            (this.green * other.green / 255),
            (this.blue * other.blue / 255),
            (this.alpha * other.alpha / 255)
        )
    }

    operator fun times(other: Int): Color {
        val otherColor = rgb(other)

        return Color(
            (this.red * otherColor.red / 255),
            (this.green * otherColor.green / 255),
            (this.blue * otherColor.blue / 255),
            (this.alpha * otherColor.alpha / 255)
        )
    }

    operator fun times(fac: Float): Color {
        return Color(
            (this.red * fac).toInt(),
            (this.green * fac).toInt(),
            (this.blue * fac).toInt(),
            (this.alpha * fac).toInt()
        )
    }

    operator fun plus(other: Color): Color {
        return Color(
            (this.red + other.red),
            (this.green + other.green),
            (this.blue + other.blue),
            (this.alpha + other.alpha)
        )
    }
}

operator fun Int.not() = this.color

infix fun Float.within(pair: Pair<Color, Color>): Color {
    val startAlpha = pair.first.alpha
    val startRed = pair.first.red
    val startGreen = pair.first.green
    val startBlue = pair.first.blue

    val endAlpha = pair.second.alpha
    val endRed = pair.second.red
    val endGreen = pair.second.green
    val endBlue = pair.second.blue

    val alpha = (startAlpha + (endAlpha - startAlpha) * this).toInt()
    val red = (startRed + (endRed - startRed) * this).toInt()
    val green = (startGreen + (endGreen - startGreen) * this).toInt()
    val blue = (startBlue + (endBlue - startBlue) * this).toInt()

    return Color(red, green, blue, alpha)
}

fun color(t: Int): Color {
    return t.color
}

fun color(t: Int?): Color? {
    return t?.color
}

val Int.color: Color
    get() = Color.rgb(this)

val Long.color: Color
    get() = Color(this.toInt())

infix fun Int.alpha(alpha: Float): Color {
    return this.color alpha alpha
}