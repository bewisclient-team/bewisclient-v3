package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.interfaces.KeyBindingAccessor
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.logic.staticFun
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

object KeyWidget : ScalableWidget(Identifier.of("bewisclient", "key_widget")) {
    val backgroundColor = create("background_color", DefaultWidgetSettings.backgroundColor.cloneWithDefault())
    val backgroundOpacity = create(
        "background_opacity", DefaultWidgetSettings.backgroundOpacity.cloneWithDefault()
    )
    val borderColor = create("border_color", DefaultWidgetSettings.borderColor.cloneWithDefault())
    val borderOpacity = create("border_opacity", DefaultWidgetSettings.borderOpacity.cloneWithDefault())
    val textColor = create("text_color", DefaultWidgetSettings.textColor.cloneWithDefault())

    val pressedBackgroundColor = color(
        "pressed_background_color", StaticColorSaver(Color.LIGHT_GRAY), ColorSetting.CHANGING, ColorSetting.STATIC, ColorSetting.THEME
    )
    val pressedBackgroundOpacity = create(
        "pressed_background_opacity", DefaultWidgetSettings.backgroundOpacity.cloneWithDefault()
    )
    val pressedBorderColor = color(
        "pressed_border_color", StaticColorSaver(Color.LIGHT_GRAY), ColorSetting.CHANGING, ColorSetting.STATIC, ColorSetting.THEME
    )
    val pressedBorderOpacity = create(
        "pressed_border_opacity", DefaultWidgetSettings.borderOpacity.cloneWithDefault()
    )
    val pressedTextColor = color(
        "pressed_text_color", StaticColorSaver(Color.BLACK), ColorSetting.CHANGING, ColorSetting.STATIC, ColorSetting.THEME
    )

    val shadow = boolean("shadow", false)
    val paddingSize = int("padding_size", 5, 0, 10)
    val borderRadius = create("border_radius", DefaultWidgetSettings.borderRadius.cloneWithDefault())
    val gap = int("gap", 2, 0, 20)

    val showMovementKeys: BooleanSetting = boolean("show_movement_keys", true) { _, new ->
        if (!showAttackUseKeys.get() && !showJumpKey.get() && new == false) showAttackUseKeys.set(true)
    }
    val showAttackUseKeys: BooleanSetting = boolean("show_attack_use_keys", true) { _, new ->
        if (!showMovementKeys.get() && !showJumpKey.get() && new == false) showMovementKeys.set(true)
    }
    val showJumpKey: BooleanSetting = boolean("show_jump_key", true) { _, new ->
        if (!showAttackUseKeys.get() && !showMovementKeys.get() && new == false) showMovementKeys.set(true)
    }

    val showCPS = boolean("show_cps", false)

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:biome_widget", "top")

    override fun render(screenDrawing: ScreenDrawing) {
        val paddingSize = paddingSize.get()

        val topSize = 9 + (paddingSize + 2) * 2

        val bottomHeight = 9 + paddingSize * 2

        val totalWidth = topSize * 3 + gap.get() * 2
        val middleWidth = (totalWidth - gap.get()) / 2

        var y = 0

        if (showMovementKeys.get()) {
            renderKey(screenDrawing, topSize + gap.get(), 0, topSize, topSize, client.options.forwardKey)
            renderKey(screenDrawing, 0, topSize + gap.get(), topSize, topSize, client.options.leftKey)
            renderKey(screenDrawing, topSize + gap.get(), topSize + gap.get(), topSize, topSize, client.options.backKey)
            renderKey(screenDrawing, (topSize + gap.get()) * 2, topSize + gap.get(), topSize, topSize, client.options.rightKey)
            y += (topSize + gap.get()) * 2
        }

        if (showAttackUseKeys.get()) {
            renderKey(screenDrawing, 0, y, middleWidth, bottomHeight, client.options.attackKey)
            renderKey(screenDrawing, totalWidth - middleWidth, y, middleWidth, bottomHeight, client.options.useKey)
            y += bottomHeight + gap.get()
        }

        if (showJumpKey.get()) renderKey(screenDrawing, 0, y, totalWidth, bottomHeight, client.options.jumpKey)
    }

    fun renderKey(
        screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, keyBinding: KeyBinding
    ) {
        renderKey(
            screenDrawing, x, y, width, height, keyBinding.getKeyText(), isPressed(keyBinding)
        )
    }

    fun isPressed(keyBinding: KeyBinding): Boolean {
        val c = client.currentScreen as? RenderableScreen ?: return keyBinding.isPressed
        val d = c.renderable as? HudEditScreen ?: return keyBinding.isPressed

        val key = (keyBinding as KeyBindingAccessor).getBoundKey()

        if (key.category == InputUtil.Type.KEYSYM) return InputUtil.isKeyPressed(
            client.window, key.code
        )
        if (key.category == InputUtil.Type.MOUSE) return d.mouseMap[key.code] == true
        return keyBinding.isPressed
    }

    fun KeyBinding.getKeyText(): String = when ((this as KeyBindingAccessor).getBoundKey().code) {
        GLFW.GLFW_MOUSE_BUTTON_LEFT -> "LMB"
        GLFW.GLFW_MOUSE_BUTTON_RIGHT -> "RMB"
        else -> this.boundKeyLocalizedText.string
    }

    fun renderKey(
        screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, text: String, pressed: Boolean
    ) {
        val textColor = (if (pressed) pressedTextColor else textColor).get().getColor()
        val backgroundColor = (if (pressed) pressedBackgroundColor else backgroundColor).get().getColor()
        val backgroundOpacity = (if (pressed) pressedBackgroundOpacity else backgroundOpacity).get()
        val borderColor = (if (pressed) pressedBorderColor else borderColor).get().getColor()
        val borderOpacity = (if (pressed) pressedBorderOpacity else borderOpacity).get()
        val borderRadius = borderRadius.get()

        screenDrawing.fillWithBorderRounded(
            x, y, width, height, borderRadius, backgroundColor alpha backgroundOpacity, borderColor alpha borderOpacity
        )

        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f + 1f) {
            screenDrawing.drawCenteredText(text, x + width / 2 + 1, y, textColor, shadow.get())
        }
    }

    override fun getWidth(): Int {
        val paddingSize = paddingSize.get()

        val elementHeight = 9 + (paddingSize + 2) * 2

        return elementHeight * 3 + gap.get() * 2
    }

    override fun getHeight(): Int {
        val paddingSize = paddingSize.get()
        val topSize = 9 + (paddingSize + 2) * 2
        val bottomHeight = 9 + paddingSize * 2

        var y = 0

        if (showMovementKeys.get()) y += (topSize + gap.get()) * 2
        if (showAttackUseKeys.get()) y += bottomHeight + gap.get()
        if (showJumpKey.get()) y += bottomHeight

        return y
    }

    override val title = "Key Widget"
    override val description = "Displays your movement and jump keys."

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            MultipleBooleanSettingsRenderable(
                createTranslation("keys", "Select which keys should be shown"), null, listOf(
                    showMovementKeys.createRenderablePart("widget.key_widget.show_movement_keys", "Movement Keys"), showAttackUseKeys.createRenderablePart("widget.key_widget.show_attack_use_keys", "Attack/Use Keys"), showJumpKey.createRenderablePart("widget.key_widget.show_jump_key", "Jump Key")
                ).staticFun()
            )
        )

        list.add(
            showCPS.createRenderable("widget.key_widget.show_cps", "Show CPS", "Shows your clicks per second (CPS) for the attack/use keys")
        )

        list.add(
            backgroundColor.createRenderableWithFader(
                "widget.background", "Background", "Set the color and opacity of the widget", backgroundOpacity
            )
        )
        list.add(
            borderColor.createRenderableWithFader(
                "widget.border", "Border", "Set the color and opacity of the widget's border", borderOpacity
            )
        )
        list.add(
            textColor.createRenderable(
                "widget.text_color", "Text Color", "Set the color of the text in the widget"
            )
        )

        list.add(
            pressedBackgroundColor.createRenderableWithFader(
                "widget.pressed_background", "Pressed Background", "Set the color and opacity of the widget when a key is pressed", pressedBackgroundOpacity
            )
        )
        list.add(
            pressedBorderColor.createRenderableWithFader(
                "widget.pressed_border", "Pressed Border", "Set the color and opacity of the widget's border when a key is pressed", pressedBorderOpacity
            )
        )
        list.add(
            pressedTextColor.createRenderable(
                "widget.pressed_text_color", "Pressed Text Color", "Set the color of the text in the widget when a key is pressed"
            )
        )

        list.add(
            shadow.createRenderable(
                "widget.text_shadow", "Text Shadow", "Set whether text in the widget has a shadow"
            )
        )
        list.add(
            gap.createRenderable(
                "widget.gap", "Gap", "Set the gap between the keys in the widget"
            )
        )
        list.add(
            paddingSize.createRenderable(
                "widget.padding_size", "Padding Size", "Set the padding at the edge of the widget to the text"
            )
        )
        list.add(
            borderRadius.createRenderable(
                "widget.border_radius", "Border Radius", "Set the radius of the widget's border corners"
            )
        )

        super.appendSettingsRenderables(list)
    }
}