package net.bewis09.bewisclient.core

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.text.Style
import net.minecraft.text.StyleSpriteSource
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

fun ClickableWidget.isEnabled(): Boolean = this.isInteractable

var LivingEntity.beforeHeadYaw: Float
    get() = this.lastHeadYaw
    set(value) = run { this.lastHeadYaw = value }

fun LivingEntity.pos(): Vec3d = this.entityPos

fun MinecraftClient.registerTexture(identifier: Identifier, image: NativeImage) {
    this.textureManager.registerTexture(
        identifier, NativeImageBackedTexture({ identifier.toString() }, image)
    )
}

fun Identifier.toStyleFont(): Style {
    return Style.EMPTY.withFont(StyleSpriteSource.Font(this))
}

fun HorseEntity.getColor(): String = this.horseColor.asString().lowercase()