package net.bewis09.bewisclient.core.features

import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.util.MathHelper
import net.minecraft.client.DeltaTracker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.fog.FogData
import net.minecraft.client.renderer.fog.environment.DimensionOrBossFogEnvironment
import net.minecraft.client.renderer.fog.environment.FogEnvironment
import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment
import net.minecraft.client.renderer.fog.environment.PowderedSnowFogEnvironment
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import kotlin.jvm.java

object BetterVisibility {
    class FogModifierConfig(val setting: BooleanSetting, val clazz: Class<out FogEnvironment>, val start: (Float) -> Float, val end: (Float) -> Float)

    val fogModifiers = listOf(
        FogModifierConfig(BetterVisibilitySettings.nether, DimensionOrBossFogEnvironment::class.java, { it * 2 - MathHelper.clamp(it / 10.0f, 4.0f, 64.0f) }, { it * 2 }),
        FogModifierConfig(BetterVisibilitySettings.water, WaterFogEnvironment::class.java, { -8f }, { it }),
        FogModifierConfig(BetterVisibilitySettings.lava, LavaFogEnvironment::class.java, { -8f }, { 16f }),
        FogModifierConfig(BetterVisibilitySettings.powder_snow, PowderedSnowFogEnvironment::class.java, { -8f }, { 8f })
    )

    fun applyFogModifier(instance: FogEnvironment, fogData: FogData, entity: Entity, blockPos: BlockPos, clientWorld: ClientLevel, viewDistance: Float, renderTickCounter: DeltaTracker) {
        instance.setupFog(fogData, entity, blockPos, clientWorld, viewDistance, renderTickCounter)

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