package net.bewis09.bewisclient.mixin

import net.bewis09.bewisclient.core.Core
import net.bewis09.bewisclient.core.MixinFunctions
import net.bewis09.bewisclient.game.ShulkerBoxTooltipComponent
import net.bewis09.bewisclient.impl.functionalities.Perspective
import net.bewis09.bewisclient.impl.settings.functionalities.PerspectiveSettings
import net.bewis09.bewisclient.impl.settings.functionalities.ShulkerBoxTooltipSettings
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData

object MixinFunctionImpl: MixinFunctions, EventEntrypoint {
    override fun onInitializeClient() {
        Core.registerMixins(this)
    }

    override fun getShulkerBoxTooltipData(color: Int?, array: Array<ItemStack>): TooltipData? {
        if (!ShulkerBoxTooltipSettings.enabled.get()) return null

        return ShulkerBoxTooltipComponent.of(
            color, array
        )
    }

    override fun getYawAddition(): Float = if(PerspectiveSettings.enabled.get()) Perspective.cameraAddYaw else 0f

    override fun getPitchAddition(): Float = if(PerspectiveSettings.enabled.get()) Perspective.cameraAddPitch else 0f
}