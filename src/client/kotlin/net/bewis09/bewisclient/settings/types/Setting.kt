package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.interfaces.Settable
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.settings.SettingsLoader

/**
 * Base class for settings that can be stored in the settings file.
 * It provides methods to get and set the value, as well as to convert the value to and from a JSON element.
 *
 * @param T The type of the setting value.
 * @param default The default value of the setting.
 * @param onChangeListener An optional listener that is called when the setting value changes.
 */
abstract class Setting<T>(val default: () -> T, val onChangeListener: (Setting<T>.(oldValue: T?, newValue: T?) -> Unit)?) : BewisclientInterface, Gettable<T>, Settable<T?> {
    constructor(default: () -> T) : this(default, null)

    constructor(default: T, onChangeListener: (Setting<T>.(oldValue: T?, newValue: T?) -> Unit)? = null) : this({ default }, onChangeListener)

    constructor(default: T) : this({ default }, null)

    /**
     * The current value of the setting.
     * It is initialized to null and can be set to a value using the `set` method.
     * The default value is returned when `get` is called and the current value is null.
     */
    private var value: T? = null

    override fun get(): T {
        return value ?: default()
    }

    /**
     * Returns the current value of the setting without using the default value.
     * If the value is null, it returns null instead of the default value.
     */
    fun getWithoutDefault(): T? {
        return value
    }

    /**
     * Sets the value of the setting and calls the onChange method.
     * It also calls the onChangeListener if it is set.
     * After setting the value, it saves the settings.
     *
     * @param value The new value to set for the setting.
     */
    override fun set(value: T?) {
        val oldValue = this.value
        if (value == this.value) {
            return // No change, do nothing
        }
        this.value = value
        onChange(oldValue, value)
        onChangeListener?.invoke(this, oldValue, value)
        save()
    }

    operator fun invoke(value: T) {
        set(value as T?)
    }

    operator fun invoke() {
        get()
    }

    /**
     * Saves the settings to the file.
     * This method should be called after setting a value to ensure that the changes are persisted.
     */
    fun save() {
        SettingsLoader.getAllSettings().forEach { it.save() }
    }

    /**
     * Sets the value of the setting without saving the settings.
     * It calls the onChange method and the onChangeListener if it is set.
     *
     * @param value The new value to set for the setting.
     */
    fun setWithoutSave(value: T?) {
        val oldValue = this.value
        if (value == this.value) {
            return // No change, do nothing
        }
        this.value = processChange(value)
        onChange(oldValue, this.value)
        onChangeListener?.invoke(this, oldValue, this.value)
    }

    /**
     * Converts the setting value to a JSON element.
     * This method should be implemented by subclasses to provide the specific conversion logic.
     *
     * @return A JsonElement representing the setting value.
     */
    abstract fun convertToElement(): JsonElement?

    /**
     * Sets the value of the setting from a JSON element.
     * This method should be implemented by subclasses to provide the specific deserialization logic.
     *
     * @param data The JsonElement containing the value to set.
     */
    abstract fun setFromElement(data: JsonElement?)

    /**
     * Called when the value of the setting changes.
     * This method can be overridden by subclasses to provide custom behavior when the value changes.
     *
     * @param oldValue The previous value of the setting.
     * @param newValue The new value of the setting.
     */
    open fun onChange(oldValue: T?, newValue: T?) {

    }

    open fun processChange(value: T?) = value
}