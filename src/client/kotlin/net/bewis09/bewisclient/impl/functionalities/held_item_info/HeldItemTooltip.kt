package net.bewis09.bewisclient.impl.functionalities.held_item_info

import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.interfaces.Settable
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.Registries
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Formatting
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.profiler.Profilers

object HeldItemTooltip : ImageSettingCategory(
    "held_item_tooltip", Translation("menu.category.held_item_tooltip", "Held Item Info"), arrayOf(
        HeldItemTooltipSettings.maxShownLines.createRenderable("held_item_tooltip.max_shown_lines", "Max Shown Lines", "Maximum number of lines to show in the held item tooltip"),
        MultipleBooleanSettingsRenderable.create(
            "held_item_tooltip.multiple_boolean_settings", "Data Component Tooltips:", "Select which information to show in the held item tooltip"
        ) { HeldItemTooltip.componentRenderableParts }
    ), HeldItemTooltipSettings.enabled
) {
    fun lookup() {
        isLookup = true

        ItemStack.EMPTY.appendTooltip(Item.TooltipContext.DEFAULT, TooltipDisplayComponent.DEFAULT, null, TooltipType.BASIC) {}

        isLookup = false
    }

    var isLookup = false
    var isRendering = false

    val componentSet = mutableSetOf<ComponentType<*>>(
        DataComponentTypes.ATTRIBUTE_MODIFIERS,
        DataComponentTypes.UNBREAKABLE,
        DataComponentTypes.BLOCK_ENTITY_DATA,
        DataComponentTypes.CAN_BREAK,
        DataComponentTypes.CAN_PLACE_ON,
        DataComponentTypes.DAMAGE
    )

    val componentRenderableParts by lazy {
        lookup()

        val parts = arrayListOf<MultipleBooleanSettingsRenderable.Part<*>>()

        for (componentType in componentSet.sortedWith { a, b ->
            val id1 = Registries.DATA_COMPONENT_TYPE.getEntry(a).idAsString
            val id2 = Registries.DATA_COMPONENT_TYPE.getEntry(b).idAsString

            if (id1.startsWith("minecraft:")) {
                return@sortedWith if (id2.startsWith("minecraft:")) id1.compareTo(id2) else -1
            } else if (id2.startsWith("minecraft:")) {
                return@sortedWith 1
            }

            id1.compareTo(id2)
        } ) {
            val id = toReadableString(Registries.DATA_COMPONENT_TYPE.getEntry(componentType).idAsString)
            parts.add(MultipleBooleanSettingsRenderable.Part(
                Translation.literal(id),
                null,
                object : Settable<Boolean?>, Gettable<Boolean> {
                    override fun get(): Boolean {
                        return HeldItemTooltipSettings.showMap[id, !defaultOff.contains(componentType)]
                    }

                    override fun set(value: Boolean?) {
                        HeldItemTooltipSettings.showMap[id] = value
                    }
                }
            ))
        }
        parts
    }

    val defaultOff = arrayOf(DataComponentTypes.DAMAGE, DataComponentTypes.ATTRIBUTE_MODIFIERS)

    fun render(drawContext: DrawContext, textRenderer: TextRenderer, heldItemTooltipFade: Int, currentStack: ItemStack) {
        Profilers.get().push("heldItemTooltip")
        if (heldItemTooltipFade > 0 && !currentStack.isEmpty) {
            isRendering = true

            val mutableText: MutableText = Text.empty().append(currentStack.name).formatted(currentStack.rarity.formatting)
            if (currentStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                mutableText.formatted(Formatting.ITALIC)
            }

            var texts: MutableList<MutableText> = mutableListOf(mutableText)

            if (currentStack.contains(DataComponentTypes.DAMAGE)) {
                if (TooltipDisplayComponent.DEFAULT.shouldDisplay(DataComponentTypes.DAMAGE)) {
                    texts.add(Text.translatable("item.durability", currentStack.maxDamage - currentStack.damage, currentStack.maxDamage))
                }
            }

            currentStack.appendTooltip(Item.TooltipContext.create(MinecraftClient.getInstance().world), TooltipDisplayComponent.DEFAULT, MinecraftClient.getInstance().player, TooltipType.BASIC) {
                texts.add(it.copy())
            }

            if (texts.size > 1) {
                for (it in texts.subList(1, texts.size)) {
                    if (it.style.color?.rgb == -1 || it.style == Style.EMPTY)
                        it.formatted(Formatting.GRAY)
                }
            }

            if (texts.size > HeldItemTooltipSettings.maxShownLines.get() + 1) {
                val beforeSize = texts.size
                texts = texts.subList(0, HeldItemTooltipSettings.maxShownLines.get())
                texts.add(Translations.MORE_LINES(beforeSize - texts.size))
            }

            var l = (heldItemTooltipFade * 256.0f / 10.0f).toInt()
            if (l > 255) {
                l = 255
            }

            var y: Int = drawContext.scaledWindowHeight - 59
            if (MinecraftClient.getInstance().interactionManager?.hasStatusBars() == false) {
                y += 14
            }

            if (l > 0) {
                for ((index, text) in texts.withIndex()) {
                    val width: Int = textRenderer.getWidth(text)
                    val x: Int = (drawContext.scaledWindowWidth - width) / 2

                    drawContext.drawTextWithBackground(textRenderer, text, x, y + (index - texts.size + 1) * 10, width, ColorHelper.withAlpha(l, Colors.WHITE))
                }
            }
            isRendering = false
        }

        Profilers.get().pop()
    }

    fun toReadableString(id: String): String {
        val without = id.split("^[a-z0-9_]+:".toRegex())[1]
        return without.replace('_', ' ').split(" ").joinToString(" ") { j -> j.replaceFirstChar { it.titlecase() } }.split("/").let { i -> i[0] + i.drop(1).joinToString { j -> " (${j.replaceFirstChar { it.titlecase() }})" } }
    }
}