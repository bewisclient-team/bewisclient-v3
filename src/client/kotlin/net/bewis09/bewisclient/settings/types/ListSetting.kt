package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.settings.Settings

class ListSetting<T>(default: List<T>, val from: (JsonElement) -> T?, val to: (T) -> JsonElement?) : Setting<MutableList<T>>(default.toMutableList()), MutableCollection<T> {
    override fun convertToElement(): JsonElement? {
        return Settings.gson.toJsonTree(get().mapNotNull { to(it) })
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asJsonArray?.mapNotNull { catch { from(it) } }?.toMutableList())
        } catch (e: Exception) {
            error("Failed to deserialize ListSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }

    override fun containsAll(elements: Collection<T>): Boolean = get().containsAll(elements)

    override fun contains(element: T): Boolean = get().contains(element)

    override fun isEmpty(): Boolean = get().isEmpty()

    override val size: Int
        get() = get().size

    override fun retainAll(elements: Collection<T>): Boolean = get().retainAll(elements).also { save() }

    override fun removeAll(elements: Collection<T>): Boolean = get().removeAll(elements).also { save() }

    override fun remove(element: T): Boolean = get().remove(element).also { save() }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val iterator = get().iterator()

        override fun remove() = iterator.remove().also { save() }

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T = iterator.next()
    }

    override fun clear() = get().clear().also { save() }

    override fun addAll(elements: Collection<T>): Boolean = get().addAll(elements).also { save() }

    override fun add(element: T): Boolean = get().add(element).also { save() }

    operator fun set(index: Int, element: T) = get().set(index, element).also { save() }

    operator fun get(index: Int): T = get()[index]
}