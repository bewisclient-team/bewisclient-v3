package net.bewis09.bewisclient.core.wrapper

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

@JvmInline
value class WorldWrapper(val world: World) {
    fun getBlockState(blockPos: BlockPos): BlockStateWrapper {
        return world.getBlockState(blockPos).wrap()
    }
}

internal fun World.wrap() = WorldWrapper(this)