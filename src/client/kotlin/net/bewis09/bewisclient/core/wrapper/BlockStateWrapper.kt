package net.bewis09.bewisclient.core.wrapper

import net.minecraft.block.BlockState

@JvmInline
value class BlockStateWrapper(val blockState: BlockState) {
    val isAir: Boolean
        get() = blockState.isAir

    fun getBlockTitle(): TextWrapper = blockState.block.name.wrap()
}

internal fun BlockState.wrap() = BlockStateWrapper(this)