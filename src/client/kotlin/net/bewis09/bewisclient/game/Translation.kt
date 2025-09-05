package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.logic.addTranslation
import net.minecraft.text.MutableText
import net.minecraft.text.Text

class Translation(private val key: String, @Suppress("PropertyName") val en_us: String) {
    init {
        if (!key.isEmpty()) addTranslation(key, en_us)
    }

    fun getTranslatedText(): MutableText {
        if (key.isEmpty()) {
            return Text.literal(en_us)
        }
        return Text.translatable("bewisclient.$key")
    }

    fun getTranslatedText(vararg args: Any): MutableText {
        return if (key.isEmpty()) Text.literal(en_us)
        else Text.translatable("bewisclient.$key", *args)
    }

    fun getTranslatedString(): String {
        return if (key.isEmpty()) en_us
        else getTranslatedText().string
    }

    fun getKey(): String {
        return "bewisclient.$key"
    }

    operator fun invoke(): MutableText {
        return getTranslatedText()
    }

    operator fun invoke(vararg args: Any): MutableText {
        return getTranslatedText(*args)
    }

    companion object {
        fun literal(tooltip: String): Translation = Translation("", tooltip)
    }
}

fun registerTranslation(key: String, @Suppress("LocalVariableName") en_us: String): String {
    return Translation(key, en_us).getKey()
}