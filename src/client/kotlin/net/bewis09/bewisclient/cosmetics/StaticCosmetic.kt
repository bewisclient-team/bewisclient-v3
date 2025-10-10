package net.bewis09.bewisclient.cosmetics

import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.util.createIdentifier
import net.minecraft.util.Identifier

class StaticCosmetic(private val identifier: Identifier) : Cosmetic {
    override fun getIdentifier(): Identifier {
        return identifier
    }

    companion object {
        fun create(identifier: CosmeticIdentifier, byteArray: ByteArray): StaticCosmetic {
            Bewisclient.createTexture(identifier.identifier, byteArray)
            return StaticCosmetic(identifier.identifier)
        }
    }
}