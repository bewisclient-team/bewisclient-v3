package net.bewis09.bewisclient.core

import net.minecraft.util.Identifier

data class BewisclientID(val namespace: String, val path: String) {
    constructor(id: String) : this(
        id.substringBefore(":","bewisclient"), id.substringAfter(":")
    )

    internal fun toIdentifier(): Identifier {
        return Identifier.of(namespace, path)
    }

    override fun toString(): String {
        return "$namespace:$path"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BewisclientID) return false

        if (namespace != other.namespace) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }
}