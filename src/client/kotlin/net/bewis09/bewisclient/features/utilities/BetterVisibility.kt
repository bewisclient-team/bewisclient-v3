// @VersionReplacement

package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.settings.structure.ImageFeature

object BetterVisibility : ImageFeature(createIdentifier("bewisclient", "better_visibility"), "Better Visibility") {
    var nether by boolean("nether", false) menuQuick ("Nether" to "Improve visibility in the Nether dimension")
    var water by boolean("water", false) menuQuick ("Water" to "Enhance visibility underwater")
    var lava by boolean("lava", false) menuQuick ("Lava" to "Boost visibility in lava")
    var powder_snow by boolean("powder_snow", false) menuQuick ("Powder Snow" to "Increase visibility in powder snow")

    class FogModifierConfig(val setting: () -> Boolean, val start: (Float) -> Float, val end: (Float) -> Float)

    val fogModifiers = mapOf(
        "atmospheric" to FogModifierConfig({ nether }, { it * 2 - (it / 10.0f).coerceIn(4.0f, 64.0f) }, { it * 2 }),
        "water" to FogModifierConfig({ water }, { -8f }, { it }),
        "lava" to FogModifierConfig({ lava }, { -8f }, { 16f }),
        "powder_snow" to FogModifierConfig({ powder_snow }, { -8f }, { 8f })
    )

    fun applyFogModifier(instance: String, fogData: FogData, viewDistance: Float) {
        if (enabled) fogModifiers[instance]?.let {
            if (!it.setting()) return

            val start = it.start(viewDistance)
            val end = it.end(viewDistance)

            if (end > fogData.environmentalEnd) {
                fogData.environmentalEnd = end
                fogData.environmentalStart = start
            }
        }
    }
}

// @[1.21.5] class FogData(var environmentalStart: Float, var environmentalEnd: Float) @[] typealias FogData = net.minecraft.client.renderer.fog.FogData
/*[@]*/typealias FogData = net.minecraft.client.renderer.fog.FogData/*[!@]*/