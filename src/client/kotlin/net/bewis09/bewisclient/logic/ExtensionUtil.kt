package net.bewis09.bewisclient.logic

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.registry.Registries
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

fun Entity.entityId(): Identifier = Registries.ENTITY_TYPE.getEntry(this.type).key.get().value

fun BlockState.blockId(): Identifier = Registries.BLOCK.getEntry(this.block).key.get().value

fun Text.setColor(color: Int): MutableText = (this as? MutableText ?: this.copy()).styled { it.withColor(color) }

fun String.toText(): MutableText = Text.literal(this)