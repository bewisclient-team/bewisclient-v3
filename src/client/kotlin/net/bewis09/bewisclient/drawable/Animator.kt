package net.bewis09.bewisclient.drawable

/**
 * Animator class to handle animations of drawable properties.
 * It allows for smooth transitions between values over a specified duration.
 * It supports different interpolation types to control the animation curve.
 * @param duration The total duration of the animation in milliseconds.
 * @param interpolationType A function that takes a delta value (0 to 1) and returns an interpolated value.
 * @param value The starting value of the animation.
 */
class Animator(val duration: () -> Long, val interpolationType: (delta: Float) -> Float = LINEAR, private var value: Float) {
    constructor(
        duration: Long, interpolationType: (delta: Float) -> Float = LINEAR, value: Float
    ) : this({ duration }, interpolationType, value)

    private var startTime: Long = 0
    private var beforeValue: Float = value
    private var onFinish: (Animator.() -> Unit)? = null
    private var paused = false

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
        paused = true
    }

    fun getWithoutInterpolation(): Float {
        return value
    }

    /**
     * Returns the current animated value for a given key.
     */
    fun get(): Float {
        unpause()

        val delta = (System.currentTimeMillis() - startTime) / duration().toFloat()

        if (delta >= 1) return executeFinishAction()
        if (delta <= 0) return beforeValue

        return beforeValue + (value - beforeValue) * interpolationType(delta)
    }

    /**
     * Unpauses the animation if it was paused and returns whether it was paused before.
     */
    fun unpause(): Boolean {
        val wasPaused = paused
        paused = false
        return wasPaused
    }

    fun executeFinishAction(): Float {
        val value = value
        val finishAction = onFinish
        onFinish = null
        finishAction?.invoke(this)
        return value
    }

    /**
     * Sets a value in the animation map.
     * @param value The value to set.
     */
    fun set(value: Float) {
        val paused = unpause()

        if (this.value == value) return

        this.beforeValue = get()
        this.value = value
        this.startTime = if (paused) 0 else System.currentTimeMillis()

        executeFinishAction()
    }

    fun set(value: Float, onFinish: Animator.() -> Unit) {
        set(value)
        this.onFinish = onFinish
    }
}