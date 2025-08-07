package net.bewis09.bewisclient.logic.number

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Represents the precision of a fader, including its minimum and maximum values, step size, and precision.
 *
 * @property min The minimum value of the fader.
 * @property max The maximum value of the fader.
 * @property step The step size for the fader. 0 means no step, allowing any value between min and max.
 * @property precision The number of decimal places to which the fader value is rounded.
 */
data class Precision(val min: Float, val max: Float, val step: Float, val precision: Int) {
    init {
        require(min < max) { "Minimum value must be less than maximum value." }
    }

    fun round(value: Float): Float {
        val scale = (10.0).pow(precision)
        return (value * scale).roundToInt() / scale.toFloat()
    }

    override fun toString(): String {
        return "FaderPrecision(min=$min, max=$max, step=$step, precision=$precision)"
    }

    fun roundToString(value: Float): String {
        return round(value).toString().padEnd(precision + round(value).toString().split(".")[0].length + 1, '0')
    }

    fun coerce(value: Float): Float {
        return when {
            value < min -> min
            value > max -> max
            else -> round(value)
        }
    }

    fun getStepCount(): Int {
        return ((max - min) / step).toInt() + 1
    }

    fun getNearestStep(value: Float): Float {
        val steps = ((value - min) / step).roundToInt()
        return min + steps * step
    }

    fun normalize(value: Float): Float {
        return (value - min) / (max - min)
    }

    fun denormalize(value: Float): Float {
        return min + value * (max - min)
    }

    fun parse(value: Float): Float {
        return getNearestStep(coerce(value))
    }
}