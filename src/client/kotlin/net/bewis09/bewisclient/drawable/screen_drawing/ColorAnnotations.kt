package net.bewis09.bewisclient.drawable.screen_drawing

/**
 * The color should be in ARGB format (Alpha, Red, Green, Blue).
 * When the property is of type [Number] it will be converted to [Int] using [Number.toInt].
 */
annotation class ArgbColor

/**
 * The color should be in RGB format (Red, Green, Blue) and the alpha channel should be applied separately.
 * When the property is of type [Number] it will be converted to [Int] using [Number.toInt].
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RgbColor
