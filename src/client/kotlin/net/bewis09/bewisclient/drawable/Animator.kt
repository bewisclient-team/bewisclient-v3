package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.exception.ProgramCodeException

/**
 * Animator class to handle animations of drawable properties.
 * It allows for smooth transitions between values over a specified duration.
 * It supports different interpolation types to control the animation curve.
 * @param duration The total duration of the animation in milliseconds.
 * @param interpolationType A function that takes a delta value (0 to 1) and returns an interpolated value.
 * @param initial An optional initial map of values to start the animation from.
 */
class Animator(val duration: Long, val interpolationType: (delta: Float) -> Float = LINEAR, initial: HashMap<String, Float>? = null) {
    companion object {
        val LINEAR = { delta: Float -> delta }
        val EASE_IN = { delta: Float -> delta * delta }
        val EASE_OUT = { delta: Float -> delta * (2 - delta) }
        val EASE_IN_OUT = { delta: Float ->
            if (delta < 0.5f) {
                2 * delta * delta
            } else {
                -1 + (4 - 2 * delta) * delta
            }
        }
    }

    private val values: HashMap<String, Float> = (initial ?: hashMapOf())
    private val animationStartMap: HashMap<String, Long> = hashMapOf()
    private val beforeAnimationMap: HashMap<String, Float> = hashMapOf()
    private val beforeChangeMap: HashMap<String, Float> = hashMapOf()
    private val animatedMap: HashMap<String, Float> = hashMapOf()

    /**
     * This function should be called when applying the animation.
     * If the value for a key has changed, it will start the animation for that key.
     */
    fun invoke(applyCallback: (HashMap<String, Float>) -> Unit, execute: (Map<String, Float>) -> Unit) {
        beforeChangeMap.putAll(values)
        applyCallback(beforeChangeMap)

        beforeChangeMap.forEach {
            values[it.key]?.let { value ->
                if (it.value != value) {
                    beforeAnimationMap[it.key] = getAnimatedValue(it.key)
                    animationStartMap[it.key] = System.currentTimeMillis()
                }
            }
        }

        values.putAll(beforeChangeMap)

        values.forEach {
            animatedMap[it.key] = getAnimatedValue(it.key)
        }

        execute(animatedMap)
    }

    /**
     * Returns the current animated value for a given key.
     */
    fun getAnimatedValue(key: String): Float {
        val delta = (System.currentTimeMillis() - (animationStartMap[key] ?: 0))/duration.toFloat()

        val value = values[key] ?: throw ProgramCodeException("Animation for key '$key' has not been initialized")

        if (delta >= 1) return value

        val beforeValue = beforeAnimationMap[key]

        if (beforeValue == null) {
            return value
        }

        if (delta <= 0) {
            return beforeValue
        }

        return beforeValue + (value - beforeValue) * interpolationType(delta)
    }
}

/**
 * Creates a new Animator instance with the specified duration and interpolation type.
 * @param duration The total duration of the animation in milliseconds.
 * @param interpolationType A function that takes a delta value (0 to 1) and returns an interpolated value.
 * @param initial An optional initial map of values to start the animation from.
 * @return A new Animator instance.
 */
fun animate(
    duration: Long,
    interpolationType: (delta: Float) -> Float = Animator.LINEAR,
    initial: HashMap<String, Float>? = null
): Animator {
    return Animator(duration, interpolationType, initial)
}