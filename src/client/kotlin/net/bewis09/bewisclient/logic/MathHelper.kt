package net.bewis09.bewisclient.logic

object MathHelper {
    fun clamp(num: Float, min: Float, max: Float): Float {
        return if (num < min) min else if (num > max) max else num
    }

    fun clamp(num: Double, min: Double, max: Double): Double {
        return if (num < min) min else if (num > max) max else num
    }

    fun clamp(num: Int, min: Int, max: Int): Int {
        return if (num < min) min else if (num > max) max else num
    }
}