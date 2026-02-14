package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.impl.widget.InventoryWidget.indicatorText
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.staticFun
import net.bewis09.bewisclient.util.toText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text

object ArmorWidget : ScalableWidget(createIdentifier("bewisclient", "armor_widget")) {
    override val title = "Armor Widget"
    override val description = "Displays your armor durability."

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

    val backgroundColor = create("background_color", DefaultWidgetSettings.backgroundColor.cloneWithDefault())
    val backgroundOpacity = create("background_opacity", DefaultWidgetSettings.backgroundOpacity.cloneWithDefault())
    val borderColor = create("border_color", DefaultWidgetSettings.borderColor.cloneWithDefault())
    val borderOpacity = create("border_opacity", DefaultWidgetSettings.borderOpacity.cloneWithDefault())
    val paddingSize = create("padding_size", DefaultWidgetSettings.paddingSize.cloneWithDefault())
    val shadow = create("shadow", DefaultWidgetSettings.shadow.cloneWithDefault())
    val textColor = create("text_color", DefaultWidgetSettings.textColor.cloneWithDefault())
    val borderRadius = create("border_radius", DefaultWidgetSettings.borderRadius.cloneWithDefault())

    val icons = mapOf(
        39 to createIdentifier("bewisclient", "textures/gui/sprites/helmet.png"),
        38 to createIdentifier("bewisclient", "textures/gui/sprites/chestplate.png"),
        37 to createIdentifier("bewisclient", "textures/gui/sprites/leggings.png"),
        36 to createIdentifier("bewisclient", "textures/gui/sprites/boots.png"),
        40 to createIdentifier("bewisclient", "textures/gui/sprites/shield.png")
    )

    fun getStacks(): List<Int> {
        val stacks = mutableListOf<Int>()
        if (showHead.get()) stacks.add(39)
        if (showChest.get()) stacks.add(38)
        if (showLegs.get()) stacks.add(37)
        if (showFeet.get()) stacks.add(36)
        if (showOffHand.get()) stacks.add(40)

        return stacks.filter {
            val stack = client.player?.inventory?.getStack(it) ?: getSampleStack(it)
            showEmptySlots.get() || (stack != null && !stack.isEmpty)
        }
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:inventory_widget", "top")

    override fun render(screenDrawing: ScreenDrawing) {
        if (getHeight() == 0) return

        screenDrawing.fillWithBorderRounded(
            0, 0, getWidth(), getHeight(), borderRadius(), backgroundColor().getColor() alpha backgroundOpacity(), borderColor().getColor() alpha borderOpacity()
        )

        getStacks().forEachIndexed { i, slot ->
            val y = (i * 18) + paddingSize() + 1
            val stack = client.player?.inventory?.getStack(slot) ?: getSampleStack(slot)

            if (stack != null && !stack.isEmpty) {
                screenDrawing.drawItemStackWithOverlay(stack, paddingSize() + 1, y)
            } else if(showEmptySlotIcon.get()) {
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

    fun getTextForArmor(slot: Int): Text {
        val armorStack = client.player?.inventory?.getStack(slot) ?: getSampleStack(slot)

        if (armorStack == null || armorStack.isEmpty || armorStack.maxDamage == 0) {
            return Text.empty()
        }

        val durability = armorStack.maxDamage - armorStack.damage
        val durabilityText = if (showPercentage.get()) {
            val percentage = (durability.toFloat() / armorStack.maxDamage.toFloat() * 100).toInt()
            "$percentage%"
        } else {
            "$durability"
        }

        return if (showDurability.get()) {
            durabilityText.toText().apply { if (colorCodeText.get()) withColor(getColorForDurability(durability, armorStack.maxDamage)) }
        } else {
            Text.empty()
        }
    }

    fun getColorForDurability(durability: Int, maxDurability: Int): Int {
        return Color(255.coerceAtMost(511 - (durability * 511) / maxDurability), 255.coerceAtMost((durability * 511) / maxDurability), 0).argb
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            MultipleBooleanSettingsRenderable(
                createTranslation("armor_slots", "Armor Slots"), null,
                listOf(
                    showHead.createRenderablePart("widget.armor_widget.show_head", "Show Head"),
                    showChest.createRenderablePart("widget.armor_widget.show_chest", "Show Chest"),
                    showLegs.createRenderablePart("widget.armor_widget.show_legs", "Show Legs"),
                    showFeet.createRenderablePart("widget.armor_widget.show_feet", "Show Feet"),
                    showOffHand.createRenderablePart("widget.armor_widget.show_off_hand", "Show Off-Hand")
                ).staticFun()
            )
        )

        list.add(
            showDurability.createRenderable(
                "widget.show_durability", "Show Durability", "Toggle whether to show armor durability"
            )
        )
        list.add(
            showPercentage.createRenderable(
                "widget.show_percentage", "Show Percentage", "Toggle whether to show durability as a percentage"
            )
        )
        list.add(
            showEmptySlots.createRenderable(
                "widget.show_empty_slots", "Show Empty Slots", "Toggle whether to show empty armor slots"
            )
        )
        list.add(
            showEmptySlotIcon.createRenderable(
                "widget.show_empty_slot_icon", "Show Empty Slot Icon", "Toggle whether to show an icon for empty armor slots"
            )
        )
        list.add(
            colorCodeText.createRenderable(
                "widget.color_code_text", "Color Code Text", "Toggle whether to color code the durability text"
            )
        )

        list.add(
            backgroundColor.createRenderableWithFader(
                "widget.background", "Background", "Set the color and opacity of the widget", backgroundOpacity
            )
        )
        list.add(borderColor.createRenderableWithFader("widget.border", "Border", "Set the color and opacity of the widget's border", borderOpacity))
        list.add(
            paddingSize.createRenderable(
                "widget.padding_size", "Padding Size", "Set the padding at the edge of the widget to the text"
            )
        )
        list.add(
            textColor.createRenderable(
                "widget.text_color", "Text Color", "Set the color of the text in the widget"
            )
        )
        list.add(
            borderRadius.createRenderable(
                "widget.border_radius", "Border Radius", "Set the radius of the widget's border corners"
            )
        )
        list.add(
            shadow.createRenderable(
                "widget.text_shadow", "Text Shadow", "Set whether text in the widget has a shadow"
            )
        )
        super.appendSettingsRenderables(list)
    }

    private fun getSampleStack(slot: Int): ItemStack? {
        return when (slot) {
            39 -> ItemStack(Items.NETHERITE_HELMET).apply {
                this.damage = 100
            }

            38 -> ItemStack(Items.ELYTRA).apply {
                this.damage = 240
                this.set(DataComponentTypes.CUSTOM_NAME, indicatorText)
            }

            37 -> ItemStack(Items.CHAINMAIL_LEGGINGS)
            40 -> ItemStack(Items.SHIELD).apply {
                this.damage = 24
            }

            else -> null
        }
    }
}