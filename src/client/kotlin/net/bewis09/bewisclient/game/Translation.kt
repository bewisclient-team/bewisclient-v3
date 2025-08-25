package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.logic.addTranslation
import net.minecraft.text.MutableText
import net.minecraft.text.Text

class Translation(private val key: String, @Suppress("PropertyName") val en_us: String) {
    init {
        if (!key.isEmpty())
            addTranslation(key, en_us)
    }

    fun getTranslatedText(): MutableText {
        if (key.isEmpty()) {
            return Text.literal(en_us)
        }
        return Text.translatable("bewisclient.$key")
    }

    fun getTranslatedText(vararg args: Any): MutableText {
        if (key.isEmpty()) {
            return Text.literal(en_us)
        }
        return Text.translatable("bewisclient.$key", *args)
    }

    fun getTranslatedString(): String {
        if (key.isEmpty()) {
            return en_us
        }
        return getTranslatedText().string
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