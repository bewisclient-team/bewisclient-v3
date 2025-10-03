package net.bewis09.bewisclient.core.wrapper

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.core.toStyleFont
import net.minecraft.text.MutableText

@JvmInline
value class TextWrapper(val text: MutableText) {
    val string: String
        get() = text.string

    fun append(other: TextWrapper): TextWrapper {
        text.append(other.text)
        return this
    }

    override fun toString(): String {
        return text.string
    }

    fun copy(): TextWrapper {
        return TextWrapper(text.copy())
    }

    fun setColor(color: Int): TextWrapper {
        text.styled { it.withColor(color) }
        return this
    }

    fun setBold(bold: Boolean): TextWrapper {
        text.styled { it.withBold(bold) }
        return this
    }

    fun setItalic(italic: Boolean): TextWrapper {
        text.styled { it.withItalic(italic) }
        return this
    }

    fun setUnderlined(underlined: Boolean): TextWrapper {
        text.styled { it.withUnderline(underlined) }
        return this
    }

    fun setStrikethrough(strikethrough: Boolean): TextWrapper {
        text.styled { it.withStrikethrough(strikethrough) }
        return this
    }

    fun setFont(id: BewisclientID?): TextWrapper {
        if (id != null)
            text.styled { id.toStyleFont() }
        return this
    }
}

internal fun MutableText.wrap() = TextWrapper(this)