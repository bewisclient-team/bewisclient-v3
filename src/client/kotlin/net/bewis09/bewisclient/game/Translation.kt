package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.util.addTranslation
import net.bewis09.bewisclient.util.toText
import net.minecraft.text.MutableText
import net.minecraft.text.Text

class Translation(private val namespace: String, private val key: String, @Suppress("PropertyName") val en_us: String) {
    constructor(key: String, @Suppress("LocalVariableName") en_us: String) : this("bewisclient", key, en_us)

    init {
        if (!key.isEmpty()) addTranslation(namespace, key, en_us)
    }

    fun getTranslatedText(): MutableText {
        if (key.isEmpty()) {
            return en_us.toText()
        }
        return Text.translatable("$namespace.$key")
    }

    fun getTranslatedText(vararg args: Any): MutableText {
        return if (key.isEmpty()) en_us.toText()
        else Text.translatable("$namespace.$key", *args)
    }

    fun getTranslatedString(): String {
        return if (key.isEmpty()) en_us
        else getTranslatedText().string
    }

    fun getKey(): String {
        return "$namespace.$key"
    }

    operator fun invoke(): MutableText {
        return getTranslatedText()
    }

    operator fun invoke(vararg args: Any): MutableText {
        return getTranslatedText(*args)
    }

    companion object {
        fun literal(text: String): Translation = Translation("", text)
    }
}

fun registerTranslation(key: String, @Suppress("LocalVariableName") en_us: String): String {
    return Translation(key, en_us).getKey()
}