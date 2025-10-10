package net.bewis09.bewisclient.cosmetics

import net.bewis09.bewisclient.util.createIdentifier

data class CosmeticIdentifier(val type: CosmeticType, val id: String) {
    val identifier = createIdentifier("bewisclient", "cosmetics/${type.id}/$id")
}