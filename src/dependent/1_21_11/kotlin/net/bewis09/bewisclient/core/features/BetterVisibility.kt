package net.bewis09.bewisclient.core.features

import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.minecraft.client.render.Camera
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.fog.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import kotlin.jvm.java

object BetterVisibility {
    class FogModifierConfig(val setting: BooleanSetting, val clazz: Class<out FogModifier>, val start: (Float) -> Float, val end: (Float) -> Float)

    val fogModifiers = listOf(
        FogModifierConfig(BetterVisibilitySettings.nether, AtmosphericFogModifier::class.java, { it * 2 - MathHelper.clamp(it / 10.0f, 4.0f, 64.0f) }, { it * 2 }),
        FogModifierConfig(BetterVisibilitySettings.water, WaterFogModifier::class.java, { -8f }, { it }),
        FogModifierConfig(BetterVisibilitySettings.lava, LavaFogModifier::class.java, { -8f }, { 16f }),
        FogModifierConfig(BetterVisibilitySettings.powder_snow, PowderSnowFogModifier::class.java, { -8f }, { 8f })
    )

    fun applyFogModifier(instance: FogModifier, fogData: FogData, camera: Camera, clientWorld: ClientWorld, viewDistance: Float, renderTickCounter: RenderTickCounter) {
        instance.applyStartEndModifier(fogData, camera, clientWorld, viewDistance, renderTickCounter)

        fogModifiers.find { instance.javaClass == it.clazz && it.setting.get() }?.let {
            val start = it.start(viewDistance)
            val end = it.end(viewDistance)

            if (end > fogData.environmentalEnd) {
                fogData.environmentalEnd = end
                fogData.environmentalStart = start
            }
        }
    }
}