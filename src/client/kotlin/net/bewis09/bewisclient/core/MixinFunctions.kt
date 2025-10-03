package net.bewis09.bewisclient.core

import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData

interface MixinFunctions {
    fun getShulkerBoxTooltipData(color: Int?, array: Array<ItemStack>): TooltipData?

    fun getYawAddition(): Float
    fun getPitchAddition(): Float
}