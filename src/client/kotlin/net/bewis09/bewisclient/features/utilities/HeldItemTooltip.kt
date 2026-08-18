package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.bewisclient.settings.types.BooleanMapSetting
import net.bewis09.bewisclient.version.Profiler
import net.bewis09.bewisclient.version.appendTooltip
import net.bewis09.bewisclient.version.getItemFormattedName
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack

object HeldItemTooltip : ImageFeature("held_item_tooltip", "Held Item Info") {
    val maxShownLines by int("max_shown_lines", 5, 1 to 10) menuQuick ("Max Shown Lines" to "Maximum number of lines to show in the held item tooltip")
    val showMap = create("show_map", BooleanMapSetting())

    val moreLinesText = createTranslation("more_lines", "and %s more...")

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        super.appendSettingsRenderables(list)
        list.add(MultipleBooleanSettingsRenderable.create(
            this, "multiple_boolean_settings", "Data Component Tooltips:", "Select which information to show in the held item tooltip"
        ) { componentRenderableParts })
    }

    fun lookup() {
        isLookup = true

        ItemStack.EMPTY.appendTooltip {}

        isLookup = false
    }

    var isLookup = false
    var isRendering = false

    val componentSet = mutableSetOf(
        DataComponents.ATTRIBUTE_MODIFIERS, DataComponents.UNBREAKABLE, DataComponents.BLOCK_ENTITY_DATA, DataComponents.CAN_BREAK, DataComponents.CAN_PLACE_ON, DataComponents.DAMAGE
    )

    val componentRenderableParts by lazy {
        lookup()

        val parts = arrayListOf<MultipleBooleanSettingsRenderable.Part>()

        for (componentType in componentSet.sortedWith { a, b ->
            val id1 = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(a).toString()
            val id2 = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(b).toString()

            if (id1.startsWith("minecraft:")) {
                return@sortedWith if (id2.startsWith("minecraft:")) id1.compareTo(id2) else -1
            } else if (id2.startsWith("minecraft:")) {
                return@sortedWith 1
            }

            id1.compareTo(id2)
        }) {
            val id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType).toString()
            parts.add(
                MultipleBooleanSettingsRenderable.Part(
                    Component.literal(toReadableString(id)), null, object : SettingInterfaceWithDefault<Boolean> {
                        override fun get(): Boolean {
                            return showMap[id, !defaultOff.contains(componentType)]
                        }

                        override fun set(value: Boolean?) {
                            showMap[id] = value
                        }

                        override fun getDefault(): Boolean {
                            return !defaultOff.contains(componentType)
                        }
                    }
                )
            )
        }
        parts
    }

    val defaultOff = arrayOf(DataComponents.DAMAGE, DataComponents.ATTRIBUTE_MODIFIERS)

    fun render(screenDrawing: ScreenDrawing, heldItemTooltipFade: Int, stack: ItemStack) {
        Profiler.push("heldItemTooltip")
        if (heldItemTooltipFade > 0 && !stack.isEmpty) {
            isRendering = true

            val mutableText: Component = stack.getItemFormattedName()

            var texts: MutableList<Component> = mutableListOf(mutableText)

            if (stack.has(DataComponents.DAMAGE) && showMap["minecraft:damage", false]) {
                texts.add(Component.translatable("item.durability", stack.maxDamage - stack.damageValue, stack.maxDamage))
            }

            stack.appendTooltip {
                texts.add(it.copy())
            }

            if (texts.size > 1) {
                for (it in texts.subList(1, texts.size)) {
                    if (it.style.color?.value == -1 || it.style == Style.EMPTY) it.setColor(0xAAAAAA)
                }
            }

            if (texts.size > maxShownLines + 1) {
                val beforeSize = texts.size
                texts = texts.subList(0, maxShownLines)
                texts.add(moreLinesText(beforeSize - texts.size))
            }

            texts = texts.filter { !it.string.isEmpty() }.toMutableList()

            var l = (heldItemTooltipFade * 256.0f / 10.0f).toInt()
            if (l > 255) {
                l = 255
            }

            var y: Int = screenHeight - 59
            if (client.gameMode?.hasExperience() == false) {
                y += 14
            }

            if (l > 0) {
                for ((index, text) in texts.withIndex()) {
                    screenDrawing.drawCenteredTextWithShadow(text, screenWidth / 2, y + (index - texts.size + 1) * 10, Color.WHITE alpha (l / 255f))
                }
            }
            isRendering = false
        }

        Profiler.pop()
    }

    fun toReadableString(id: String): String {
        val without = id.split("^[a-z0-9_]+:".toRegex())[1]
        return without.replace('_', ' ').split(" ").joinToString(" ") { j -> j.replaceFirstChar { it.titlecase() } }.split("/").let { i -> i[0] + i.drop(1).joinToString { j -> " (${j.replaceFirstChar { it.titlecase() }})" } }
    }
}