package net.bewis09.bewisclient.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.state.BlockState

fun Entity.entityId(): Identifier = BuiltInRegistries.ENTITY_TYPE.getKey(this.type)

fun BlockState.blockId(): Identifier = BuiltInRegistries.BLOCK.getKey(this.block)

fun Component.setColor(color: Int): MutableComponent = (this as? MutableComponent ?: this.copy()).withStyle { it.withColor(color) }

fun String.toText(): MutableComponent = Component.literal(this)