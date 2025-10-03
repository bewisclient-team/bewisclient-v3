package net.bewis09.bewisclient.core

object Core {
    @JvmField
    var mixinFunctions: MixinFunctions? = null

    fun registerMixins(mixinFunctions: MixinFunctions) {
        this.mixinFunctions = mixinFunctions
    }
}