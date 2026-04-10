package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.util.addTranslation
import net.bewis09.bewisclient.util.toText
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

class Translation(private val namespace: String, private val key: String, @Suppress("PropertyName") val en_us: String) {
    constructor(key: String, @Suppress("LocalVariableName") en_us: String) : this("bewisclient", key, en_us)

    init {
        if (!key.isEmpty()) addTranslation(namespace, key, en_us)
    }

    fun getTranslatedText(): MutableComponent = Component.translatable("$namespace.$key")

    fun getTranslatedText(vararg args: Any): MutableComponent = Component.translatable("$namespace.$key", *args)

    fun getTranslatedString(): String = getTranslatedText().string

    fun getKey(): String = "$namespace.$key"

    fun getKeyWithoutNamespace() = key

    operator fun invoke(): MutableComponent = getTranslatedText()

    operator fun invoke(vararg args: Any): MutableComponent = getTranslatedText(*args)
}