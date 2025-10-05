package net.bewis09.bewisclient.cosmetics

import net.bewis09.bewisclient.util.Bewisclient
import net.minecraft.util.Identifier

class StaticCosmetic(private val identifier: Identifier) : Cosmetic {
    override fun getIdentifier(): Identifier {
        return identifier
    }

    companion object {
        fun create(identifier: Identifier, byteArray: ByteArray): StaticCosmetic {
            Bewisclient.createTexture(identifier, byteArray)
            return StaticCosmetic(identifier)
        }
    }
}