package net.bewis09.bewisclient.core.features

import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.util.MathHelper
import net.minecraft.client.Camera
import net.minecraft.client.DeltaTracker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.fog.FogData
import net.minecraft.client.renderer.fog.environment.*

object BetterVisibility {
    class FogModifierConfig(val setting: BooleanSetting, val clazz: Class<out FogEnvironment>, val start: (Float) -> Float, val end: (Float) -> Float)

    val fogModifiers = listOf(
        FogModifierConfig(BetterVisibilitySettings.nether, AtmosphericFogEnvironment::class.java, { it * 2 - MathHelper.clamp(it / 10.0f, 4.0f, 64.0f) }, { it * 2 }),
        FogModifierConfig(BetterVisibilitySettings.water, WaterFogEnvironment::class.java, { -8f }, { it }),
        FogModifierConfig(BetterVisibilitySettings.lava, LavaFogEnvironment::class.java, { -8f }, { 16f }),
        FogModifierConfig(BetterVisibilitySettings.powder_snow, PowderedSnowFogEnvironment::class.java, { -8f }, { 8f })
    )

    fun applyFogModifier(instance: FogEnvironment, fogData: FogData, camera: Camera, clientWorld: ClientLevel, viewDistance: Float, renderTickCounter: DeltaTracker) {
        instance.setupFog(fogData, camera, clientWorld, viewDistance, renderTickCounter)

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