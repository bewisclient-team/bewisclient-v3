package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import net.bewis09.bewisclient.util.catch
import net.bewis09.bewisclient.util.jsonArray
import net.bewis09.bewisclient.settings.Settings

class ListSetting<T>(default: List<T>, val from: (JsonElement) -> T?, val to: (T) -> JsonElement?) : Setting<MutableList<T>>(default.toMutableList()), MutableList<T> {
    override fun convertToElement(): JsonElement? {
        return Settings.gson.toJsonTree(get().mapNotNull { to(it) })
    }

    override fun convertFromElement(data: JsonElement?): MutableList<T>? = data?.jsonArray()?.mapNotNull { catch { from(it) } }?.toMutableList()

    override fun containsAll(elements: Collection<T>): Boolean = get().containsAll(elements)

    override fun get(index: Int): T = get()[index]

    override fun indexOf(element: T): Int = get().indexOf(element)

    override fun lastIndexOf(element: T): Int = get().lastIndexOf(element)

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

    override fun set(index: Int, element: T): T = get().set(index, element).also { save() }

    override fun add(index: Int, element: T) = get().add(index, element).also { save() }

    override fun removeAt(index: Int): T = get().removeAt(index).also { save() }

    override fun listIterator(): MutableListIterator<T> = get().listIterator()

    override fun listIterator(index: Int): MutableListIterator<T> = get().listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = get().subList(fromIndex, toIndex)

    override fun addAll(elements: Collection<T>): Boolean = get().addAll(elements).also { save() }

    override fun addAll(index: Int, elements: Collection<T>): Boolean = get().addAll(index, elements).also { save() }

    override fun add(element: T): Boolean = get().add(element).also { save() }
}