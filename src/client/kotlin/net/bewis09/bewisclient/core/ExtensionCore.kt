package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.core.wrapper.TextWrapper
import net.bewis09.bewisclient.core.wrapper.wrap
import net.minecraft.block.Block
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.text.*
import net.minecraft.util.math.Vec3d

fun ClickableWidget.isEnabled(): Boolean = this.isInteractable

fun LivingEntity.pos(): Vec3d = this.entityPos

fun MinecraftClient.registerTexture(identifier: BewisclientID, image: NativeImage) {
    this.textureManager.registerTexture(
        identifier.toIdentifier(), NativeImageBackedTexture({ identifier.toString() }, image)
    )
}

fun BewisclientID.toStyleFont(): Style {
    return Style.EMPTY.withFont(StyleSpriteSource.Font(this.toIdentifier()))
}

fun HorseEntity.getColor(): String = this.horseColor.asString().lowercase()

fun Block.getText(): TextWrapper = this.name.wrap()

fun Entity.getText(): TextWrapper = this.name.copy().wrap()

fun String.toText(): TextWrapper = Text.literal(this).wrap()