package net.bewis09.bewisclient.cosmetics

enum class CosmeticType(val id: String) {
    HAT("hat"),
    CAPE("cape"),
    WING("wing");

    companion object {
        fun fromId(id: String): CosmeticType? {
            return entries.firstOrNull { it.id == id }
        }
    }
}