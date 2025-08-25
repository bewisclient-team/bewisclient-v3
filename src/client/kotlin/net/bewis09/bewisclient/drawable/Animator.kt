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
class Animator(val duration: () -> Long, val interpolationType: (delta: Float) -> Float = LINEAR, vararg initial: Pair<String, Float>) {
    constructor(
        duration: Long, interpolationType: (delta: Float) -> Float = LINEAR, vararg initial: Pair<String, Float>
    ) : this({ duration }, interpolationType, *initial)

    private val map: HashMap<String, Float> = hashMapOf()
    private val animationStartMap: HashMap<String, Long> = hashMapOf()
    private val beforeAnimationMap: HashMap<String, Float> = hashMapOf()
    private val finishMap: HashMap<String, () -> Unit> = hashMapOf()

    private var pauseAnimation = arrayListOf<String>()

    init {
        initial.forEach { (key, value) -> map[key] = value }
    }

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

    fun pauseForOnce() {
        pauseAnimation = arrayListOf(*map.keys.toTypedArray())
    }

    fun getWithoutInterpolation(key: String): Float {
        return map[key] ?: throw ProgramCodeException("Animation for key '$key' has not been initialized")
    }

    /**
     * Returns the current animated value for a given key.
     */
    operator fun get(key: String): Float {
        if (key in pauseAnimation) pauseAnimation.remove(key)

        val delta = (System.currentTimeMillis() - (animationStartMap[key] ?: 0)) / duration().toFloat()

        val value = map[key] ?: throw ProgramCodeException("Animation for key '$key' has not been initialized")

        if (delta >= 1) {
            val finishAction = finishMap[key]
            finishMap.remove(key)
            finishAction?.invoke()

            return value
        }

        val beforeValue = beforeAnimationMap[key]

        if (beforeValue == null) {
            return value
        }

        if (delta <= 0) {
            return beforeValue
        }

        return beforeValue + (value - beforeValue) * interpolationType(delta)
    }

    /**
     * Sets a value in the animation map.
     * @param key The key for the value.
     * @param value The value to set.
     */
    operator fun set(key: String, value: Float) {
        val paused = pauseAnimation.contains(key)

        if (paused) {
            pauseAnimation.remove(key)
        }

        if (map[key] == value) return

        val old = if (map[key] != null) this[key] else value
        map[key] = value

        animationStartMap[key] = if (paused) 0 else System.currentTimeMillis()
        beforeAnimationMap[key] = old

        val finishAction = finishMap[key]
        finishMap.remove(key)
        finishAction?.invoke()
    }

    fun set(key: String, value: Float, onFinish: () -> Unit) {
        set(key, value)

        finishMap[key] = onFinish
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
    duration: Long, interpolationType: (delta: Float) -> Float = Animator.LINEAR, vararg initial: Pair<String, Float>
): Animator {
    return Animator(duration, interpolationType, *initial)
}