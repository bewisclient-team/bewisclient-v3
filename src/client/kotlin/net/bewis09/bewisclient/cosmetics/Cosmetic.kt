package net.bewis09.bewisclient.cosmetics

import net.bewis09.bewisclient.version.Identifier

interface Cosmetic {
    fun getIdentifier(): Identifier
}