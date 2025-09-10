package net.bewis09.bewisclient.logic.color

import java.awt.Color

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

    return createColor(red, green, blue, alpha)
}

fun Color.withBrightness(brightness: Float): Color {
    val hsb = Color.RGBtoHSB(this.red, this.green, this.blue, null)
    return createHSBColor(hsb[0], hsb[1], brightness)
}

fun Color.withSaturation(saturation: Float): Color {
    val hsb = Color.RGBtoHSB(this.red, this.green, this.blue, null)
    return createHSBColor(hsb[0], saturation, hsb[2])
}

fun Color.withHue(hue: Float): Color {
    val hsb = Color.RGBtoHSB(this.red, this.green, this.blue, null)
    return createHSBColor(hue, hsb[1], hsb[2])
}

fun Color.withRed(red: Int): Color {
    return createColor(red, this.green, this.blue, this.alpha)
}

fun Color.withGreen(green: Int): Color {
    return createColor(this.red, green, this.blue, this.alpha)
}

fun Color.withBlue(blue: Int): Color {
    return createColor(this.red, this.green, blue, this.alpha)
}

fun createColor(r: Float, g: Float, b: Float, a: Float = 1f): Color {
    return createColor((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), (a * 255).toInt())
}

fun createColor(r: Int, g: Int, b: Int, a: Int = 255): Color {
    return createColorWithAlpha((((a.toLong() and 0xFF) shl 24) + ((r.toLong() and 0xFF) shl 16) + ((g.toLong() and 0xFF) shl 8) + (b.toLong() and 0xFF)).toInt())
}

fun createColor(rgb: Int): Color {
    return createColorWithAlpha(rgb + (0xFF shl 24))
}

fun createColorWithAlpha(argb: Int): Color {
    return Color(argb shr 16 and 0xFF, argb shr 8 and 0xFF, argb and 0xFF, argb shr 24 and 0xFF)
}

fun createHSBColor(h: Float, s: Float, b: Float): Color {
    return createColor(Color.HSBtoRGB(h, s, b))
}

val Int.color: Color
    get() = createColor(this)

val Long.color: Color
    get() = createColorWithAlpha(this.toInt())

operator fun Color.invoke(): Int {
    return this.rgb
}

operator fun Color.times(other: Color): Color {
    return createColor(
        (this.red * other.red / 255),
        (this.green * other.green / 255),
        (this.blue * other.blue / 255),
        (this.alpha * other.alpha / 255)
    )
}

operator fun Color.times(other: Int): Color {
    return createColor(
        (this.red * other.color.red / 255),
        (this.green * other.color.green / 255),
        (this.blue * other.color.blue / 255),
        (this.alpha * other.color.alpha / 255)
    )
}

operator fun Color.times(fac: Float): Color {
    return createColor(
        (this.red * fac).toInt(),
        (this.green * fac).toInt(),
        (this.blue * fac).toInt(),
        (this.alpha * fac).toInt()
    )
}

operator fun Color.plus(other: Color): Color {
    return createColor(
        (this.red + other.red),
        (this.green + other.green),
        (this.blue + other.blue),
        (this.alpha + other.alpha)
    )
}

val Color.brightness: Float
    get() = Color.RGBtoHSB(this.red, this.green, this.blue, null)[2]

val Color.saturation: Float
    get() = Color.RGBtoHSB(this.red, this.green, this.blue, null)[1]

val Color.hue: Float
    get() = Color.RGBtoHSB(this.red, this.green, this.blue, null)[0]

val Int.brightness: Float get() = this.color.brightness

val Int.saturation: Float get() = this.color.saturation

val Int.hue: Float get() = this.color.hue

infix fun Color.alpha(alpha: Float): Color {
    return createColor(this.red, this.green, this.blue, (alpha * 255).toInt())
}

infix fun Int.alpha(alpha: Float): Color {
    return createColor(
        this.color.red,
        this.color.green,
        this.color.blue,
        (alpha * 255).toInt()
    )
}