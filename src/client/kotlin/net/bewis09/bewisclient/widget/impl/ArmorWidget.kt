package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.features.sidebar.Widgets
import net.bewis09.bewisclient.widget.impl.InventoryWidget.indicatorText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.TOP
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.SharedConstants
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object ArmorWidget : ScalableWidget(
    createIdentifier("bewisclient", "armor_widget"),
    "Armor Widget",
    "Displays your armor durability."
) {
    val showDurability = boolean("show_durability", true)
    val showPercentage = boolean("show_percentage", false)
    val showEmptySlots = boolean("show_empty_slots", true)
    val showEmptySlotIcon = boolean("show_empty_slot_icon", true)
    val colorCodeText = boolean("color_code_text", true)

    val showHead = boolean("show_head", true)
    val showChest = boolean("show_chest", true)
    val showLegs = boolean("show_legs", true)
    val showFeet = boolean("show_feet", true)
    val showOffHand = boolean("show_off_hand", false)

    val backgroundColor = create("background_color", Widgets.Default.backgroundColor.cloneWithDefault())
    val backgroundOpacity = create("background_opacity", Widgets.Default.backgroundOpacity.cloneWithDefault())
    val borderColor = create("border_color", Widgets.Default.borderColor.cloneWithDefault())
    val borderOpacity = create("border_opacity", Widgets.Default.borderOpacity.cloneWithDefault())
    val paddingSize = create("padding_size", Widgets.Default.paddingSize.cloneWithDefault())
    val shadow = create("shadow", Widgets.Default.shadow.cloneWithDefault())
    val textColor = create("text_color", Widgets.Default.textColor.cloneWithDefault())
    val borderRadius = create("border_radius", Widgets.Default.borderRadius.cloneWithDefault())

    val icons = mapOf(
        39 to createIdentifier("bewisclient", "textures/gui/sprites/helmet.png"),
        38 to createIdentifier("bewisclient", "textures/gui/sprites/chestplate.png"),
        37 to createIdentifier("bewisclient", "textures/gui/sprites/leggings.png"),
        36 to createIdentifier("bewisclient", "textures/gui/sprites/boots.png"),
        40 to createIdentifier("bewisclient", "textures/gui/sprites/shield.png")
    )

    var componentsLoaded = SharedConstants.getCurrentVersion().name.startsWith("1.")

    fun getStacks(): List<Int> {
        val stacks = mutableListOf<Int>()
        if (showHead.get()) stacks.add(39)
        if (showChest.get()) stacks.add(38)
        if (showLegs.get()) stacks.add(37)
        if (showFeet.get()) stacks.add(36)
        if (showOffHand.get()) stacks.add(40)

        return stacks.filter {
            val stack = getSlotForStack(it)
            showEmptySlots.get() || (stack != null && !stack.isEmpty)
        }
    }

    fun getSlotForStack(slot: Int): ItemStack? {
        return client.player?.inventory?.getItem(slot)?.apply { componentsLoaded = true } ?: if (componentsLoaded) getSampleStack(slot) else null
    }

    override fun isEnabledByDefault(): Boolean = false

    override fun defaultPosition(): WidgetPosition = RelativePosition(InventoryWidget, TOP)

    override fun render(screenDrawing: ScreenDrawing) {
        if (getHeight() == 0) return

        screenDrawing.fillWithBorderRounded(
            0, 0, getWidth(), getHeight(), borderRadius(), backgroundColor().getColor() alpha backgroundOpacity(), borderColor().getColor() alpha borderOpacity()
        )

        getStacks().forEachIndexed { i, slot ->
            val y = (i * 18) + paddingSize() + 1
            val stack = getSlotForStack(slot)

            if (stack != null && !stack.isEmpty) {
                screenDrawing.drawItemStackWithOverlay(stack, paddingSize() + 1, y)
            } else if (showEmptySlotIcon.get()) {
                icons[slot]?.let { screenDrawing.drawTexture(it, paddingSize() + 1, y, 16, 16, textColor().getColor() alpha 0.5f) }
            }

            screenDrawing.translate(0f, 0.5f) {
                screenDrawing.drawText(getTextForArmor(slot), paddingSize() + 24, y + 4, textColor().getColor(), shadow())
            }
        }
    }

    override fun getWidth(): Int = paddingSize.get() * 2 + 18 + if (showDurability.get()) 33 else 0

    override fun getHeight(): Int {
        val paddingSize = paddingSize.get()

        val lines = getStacks()
        if (lines.isEmpty()) return 0

        return lines.size * 18 + 2 * paddingSize
    }

    fun getTextForArmor(slot: Int): Component {
        if (!showDurability.get()) return Component.empty()

        val armorStack = getSlotForStack(slot)

        if (armorStack == null || armorStack.isEmpty || armorStack.maxDamage == 0) {
            return Component.empty()
        }

        val durability = armorStack.maxDamage - armorStack.damageValue
        val durabilityText = if (showPercentage.get()) {
            val percentage = (durability.toFloat() / armorStack.maxDamage.toFloat() * 100).toInt()
            "$percentage%"
        } else {
            "$durability"
        }

        return durabilityText.toText().apply { if (colorCodeText.get()) withColor(getColorForDurability(durability, armorStack.maxDamage)) }
    }

    fun getColorForDurability(durability: Int, maxDurability: Int): Int {
        return Color(255.coerceAtMost(511 - (durability * 511) / maxDurability), 255.coerceAtMost((durability * 511) / maxDurability), 0).argb
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            MultipleBooleanSettingsRenderable(
                createTranslation("armor_slots", "Armor Slots"), null,
                listOf(
                    showHead.createRenderablePart(this, "show_head", "Show Head"),
                    showChest.createRenderablePart(this, "show_chest", "Show Chest"),
                    showLegs.createRenderablePart(this, "show_legs", "Show Legs"),
                    showFeet.createRenderablePart(this, "show_feet", "Show Feet"),
                    showOffHand.createRenderablePart(this, "show_off_hand", "Show Off-Hand")
                ).staticFun()
            ).addToQuickSettings(this, "armor_slots")
        )

        list.menuQuick(showDurability, "Show Durability", "Toggle whether to show armor durability")
        list.menuQuick(showPercentage, "Show Percentage", "Toggle whether to show durability as a percentage")
        list.menuQuick(showEmptySlots, "Show Empty Slots", "Toggle whether to show empty armor slots")
        list.menuQuick(showEmptySlotIcon, "Show Empty Slot Icon", "Toggle whether to show an icon for empty armor slots")
        list.menuQuick(colorCodeText, "Color Code Text", "Toggle whether to color code the durability text")

        list.add(LineWidget.backgroundColorRenderable(backgroundColor, backgroundOpacity))
        list.add(LineWidget.borderColorRenderable(borderColor, borderOpacity))
        list.add(LineWidget.paddingSizeRenderable(paddingSize))
        list.add(LineWidget.textColorRenderable(textColor))
        list.add(LineWidget.borderRadiusRenderable(borderRadius))
        list.add(LineWidget.shadowRenderable(shadow))

        super.appendSettingsRenderables(list)
    }

    private fun getSampleStack(slot: Int): ItemStack? {
        return when (slot) {
            39 -> ItemStack(Items.NETHERITE_HELMET).apply {
                this.damageValue = 100
            }

            38 -> ItemStack(Items.ELYTRA).apply {
                this.damageValue = 240
                this.set(DataComponents.CUSTOM_NAME, indicatorText)
            }

            37 -> ItemStack(Items.CHAINMAIL_LEGGINGS)
            40 -> ItemStack(Items.SHIELD).apply {
                this.damageValue = 24
            }

            else -> null
        }
    }
}