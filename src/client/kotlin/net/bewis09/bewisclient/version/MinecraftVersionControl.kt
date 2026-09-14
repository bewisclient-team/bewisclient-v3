package net.bewis09.bewisclient.version

import net.bewis09.bewisclient.common.getMinecraftVersion

fun <T: Any, A> T.getField(vararg pair: Pair<Boolean, String>): A = getField(pair.first { it.first }.second)

@Suppress("UNCHECKED_CAST")
fun <T: Any, A> T.getField(name: String): A = this.javaClass.getDeclaredField(name).get(this) as A

fun <T: Any, A> Class<T>.getStaticField(vararg pair: Pair<Boolean, String>): A = getStaticField(pair.first { it.first }.second)

@Suppress("UNCHECKED_CAST")
fun <T: Any, A> Class<T>.getStaticField(name: String): A = this.getField(name).get(this) as A

fun until(version: String): Boolean {
    val mcVersion = MinecraftVersion(version)
    val crVersion = MinecraftVersion(getMinecraftVersion())

    return mcVersion <= crVersion
}

class MinecraftVersion(val version: String): Comparable<MinecraftVersion> {
    override fun compareTo(other: MinecraftVersion): Int {
        val thisParts = this.version.split(".")
        val otherParts = other.version.split(".")

        for (i in 0 until maxOf(thisParts.size, otherParts.size)) {
            val thisPart = thisParts.getOrNull(i)?.toIntOrNull() ?: 0
            val otherPart = otherParts.getOrNull(i)?.toIntOrNull() ?: 0

            if (thisPart != otherPart) {
                return thisPart - otherPart
            }
        }

        return 0
    }
}