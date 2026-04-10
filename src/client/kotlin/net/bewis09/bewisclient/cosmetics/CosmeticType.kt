package net.bewis09.bewisclient.cosmetics

enum class CosmeticType(val id: String) {
    HAT("hat"),
    CAPE("cape"),
    WING("wing");

    override fun toString() = id
}