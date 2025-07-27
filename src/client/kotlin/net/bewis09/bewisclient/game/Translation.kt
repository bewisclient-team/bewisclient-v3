package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.logic.addTranslation
import net.minecraft.text.Text

class Translation(private val key: String, @Suppress("PropertyName") val en_us: String) {
    init {
        if (key.isEmpty()) {
            throw IllegalArgumentException("Translation key cannot be empty")
        }

        if (en_us.isEmpty()) {
            throw IllegalArgumentException("Translation en_us cannot be empty")
        }

        addTranslation(key, en_us)
    }

    fun getTranslatedText(): Text {
        return Text.translatable("bewisclient.$key")
    }

    fun getTranslatedText(vararg args: Any): Text {
        return Text.translatable("bewisclient.$key", *args)
    }

    fun getTranslatedString(): String {
        return getTranslatedText().string
    }

    fun getKey(): String {
        return "bewisclient.$key"
    }

    operator fun invoke(): Text {
        return getTranslatedText()
    }

    operator fun invoke(vararg args: Any): Text {
        return getTranslatedText(*args)
    }
}

fun registerTranslation(key: String, @Suppress("LocalVariableName") en_us: String): String {
    return Translation(key, en_us).getKey()
}