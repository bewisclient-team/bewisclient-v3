package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.logic.Settings
import net.bewis09.bewisclient.util.logic.ClientInterface
import kotlin.reflect.KProperty

/**
 * Base class for settings that can be stored in the settings file.
 * It provides methods to get and set the value, as well as to convert the value to and from a JSON element.
 *
 * @param T The type of the setting value.
 * @param default The default value of the setting.
 */
abstract class Setting<T>(val default: () -> T) : ClientInterface, SettingInterfaceWithDefault<T> {
    constructor(default: T) : this({ default })

    /**
     * The current value of the setting.
     * It is initialized to null and can be set to a value using the `set` method.
     * The default value is returned when `get` is called and the current value is null.
     */
    private var value: T? = null

    var onChangeListener: (Setting<T>.(oldValue: T?, newValue: T?) -> Unit)? = null

    fun <T: Setting<A>, A> T.withOnChangeListener(listener: (Setting<A>.(oldValue: A?, newValue: A?) -> Unit)?): T {
        this.onChangeListener = listener
        return this
    }

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

    override fun getDefault() = default()

    fun isDefault() = get() == getDefault()

    /**
     * Sets the value of the setting and calls the onChange method.
     * It also calls the onChangeListener if it is set.
     * After setting the value, it saves the settings.
     *
     * @param value The new value to set for the setting.
     */
    override fun set(value: T?) {
        val oldValue = this.value
        if (processChange(value) == this.value) {
            return // No change, do nothing
        }
        this.value = processChange(value)
        onChange(oldValue, this.value)
        onChangeListener?.invoke(this, oldValue, this.value)
        save()
    }

    operator fun invoke(value: T) {
        set(value as T?)
    }

    operator fun invoke() = get()

    /**
     * Saves the settings to the file.
     * This method should be called after setting a value to ensure that the changes are persisted.
     */
    fun save() = Settings.setDirty()

    /**
     * Sets the value of the setting without saving the settings.
     * It calls the onChange method and the onChangeListener if it is set.
     *
     * @param value The new value to set for the setting.
     */
    fun setWithoutSave(value: T?) {
        val oldValue = this.value
        if (processChange(value) == this.value) {
            return // No change, do nothing
        }
        if (processChange(value) == this.default()) {
            this.value = null
        } else {
            this.value = processChange(value)
        }
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
    fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(convertFromElement(data))
        } catch (e: Throwable) {
            info("Failed to deserialize ${this.javaClass.simpleName}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }

    abstract fun convertFromElement(data: JsonElement?): T?

    /**
     * Called when the value of the setting changes.
     * This method can be overridden by subclasses to provide custom behavior when the value changes.
     *
     * @param oldValue The previous value of the setting.
     * @param newValue The new value of the setting.
     */
    open fun onChange(oldValue: T?, newValue: T?) = Unit

    open fun processChange(value: T?) = value

    operator fun getValue(obj: Any, prop: KProperty<*>) = get()

    operator fun setValue(obj: Any, prop: KProperty<*>, value: T?) = set(value)
}