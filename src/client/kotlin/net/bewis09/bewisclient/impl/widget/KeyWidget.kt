package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.interfaces.KeyBindingAccessor
import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

object KeyWidget: ScalableWidget() {
    val backgroundColor = DefaultWidgetSettings.backgroundColor.cloneWithDefault()
    val backgroundOpacity = DefaultWidgetSettings.backgroundOpacity.cloneWithDefault()
    val borderColor = DefaultWidgetSettings.borderColor.cloneWithDefault()
    val borderOpacity = DefaultWidgetSettings.borderOpacity.cloneWithDefault()
    val textColor = DefaultWidgetSettings.textColor.cloneWithDefault()

    val pressedBackgroundColor = ColorSetting(StaticColorSaver(0xAAAAAA), ColorSetting.CHANGING, ColorSetting.STATIC)
    val pressedBackgroundOpacity = DefaultWidgetSettings.backgroundOpacity.cloneWithDefault()
    val pressedBorderColor = ColorSetting(StaticColorSaver(0xAAAAAA), ColorSetting.CHANGING, ColorSetting.STATIC)
    val pressedBorderOpacity = DefaultWidgetSettings.borderOpacity.cloneWithDefault()
    val pressedTextColor = ColorSetting(StaticColorSaver(0), ColorSetting.CHANGING, ColorSetting.STATIC)

    val paddingSize = IntegerSetting(5, 0, 10)
    val borderRadius = DefaultWidgetSettings.borderRadius.cloneWithDefault()
    val gap = IntegerSetting(2, 0, 20)

    init {
        create("background_color", backgroundColor)
        create("background_opacity", backgroundOpacity)
        create("border_color", borderColor)
        create("border_opacity", borderOpacity)
        create("text_color", textColor)

        create("pressed_background_color", pressedBackgroundColor)
        create("pressed_background_opacity", pressedBackgroundOpacity)
        create("pressed_border_color", pressedBorderColor)
        create("pressed_border_opacity", pressedBorderOpacity)
        create("pressed_text_color", pressedTextColor)

        create("padding_size", paddingSize)
        create("border_radius", borderRadius)
        create("gap", gap)
    }

    val keyWidgetTranslation = Translation("widget.key_widget.name", "Key Widget")
    val keyWidgetDescription = Translation("widget.key_widget.description", "Displays your movement and jump keys.")

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:biome_widget", "top")

    override fun getId(): Identifier = Identifier.of("bewisclient", "key_widget")

    override fun render(screenDrawing: ScreenDrawing) {
        val paddingSize = paddingSize.get()

        val topElementHeight = 9 + (paddingSize + 2) * 2
        val topElementWidth = topElementHeight

        val bottomElementHeight = 9 + paddingSize * 2

        val totalWidth = topElementWidth * 3 + gap.get() * 2
        val middleElementWidth = (totalWidth - gap.get()) / 2

        renderKey(screenDrawing, topElementWidth + gap.get(), 0, topElementWidth, topElementHeight, MinecraftClient.getInstance().options.forwardKey)
        renderKey(screenDrawing, 0, topElementHeight + gap.get(), topElementWidth, topElementHeight, MinecraftClient.getInstance().options.leftKey)
        renderKey(screenDrawing, topElementWidth + gap.get(), topElementHeight + gap.get(), topElementWidth, topElementHeight, MinecraftClient.getInstance().options.backKey)
        renderKey(screenDrawing, (topElementWidth + gap.get()) * 2, topElementHeight + gap.get(), topElementWidth, topElementHeight, MinecraftClient.getInstance().options.rightKey)

        renderKey(screenDrawing, 0, (topElementHeight + gap.get()) * 2, middleElementWidth, bottomElementHeight, MinecraftClient.getInstance().options.attackKey)
        renderKey(screenDrawing, totalWidth - middleElementWidth, (topElementHeight + gap.get()) * 2, middleElementWidth, bottomElementHeight, MinecraftClient.getInstance().options.useKey)

        renderKey(screenDrawing, 0, topElementHeight * 2 + gap.get() * 3 + bottomElementHeight, totalWidth, bottomElementHeight, MinecraftClient.getInstance().options.jumpKey)
    }

    fun renderKey(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, keyBinding: KeyBinding) {
        renderKey(screenDrawing, x, y, width, height, keyBinding.getKeyText(), keyBinding.isPressed)
    }

    fun KeyBinding.getKeyText(): String {
        if ((this as KeyBindingAccessor).getBoundKey().code == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return "LMB"
        }

        if ((this as KeyBindingAccessor).getBoundKey().code == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            return "RMB"
        }

        return this.boundKeyLocalizedText.string
    }

    fun renderKey(screenDrawing: ScreenDrawing, x: Int, y: Int, width: Int, height: Int, text: String, pressed: Boolean) {
        val textColor = (if (pressed) pressedTextColor else textColor).get().getColor()
        val backgroundColor = (if (pressed) pressedBackgroundColor else backgroundColor).get().getColor()
        val backgroundOpacity = (if (pressed) pressedBackgroundOpacity else backgroundOpacity).get()
        val borderColor = (if (pressed) pressedBorderColor else borderColor).get().getColor()
        val borderOpacity = (if (pressed) pressedBorderOpacity else borderOpacity).get()
        val borderRadius = borderRadius.get()

        screenDrawing.fillWithBorderRounded(x, y, width, height, borderRadius, backgroundColor, backgroundOpacity, borderColor, borderOpacity)

        screenDrawing.push()
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f + 1f)
        screenDrawing.drawCenteredText(text, x + width / 2 + 1, y, textColor, 1f)
        screenDrawing.pop()
    }

    override fun getWidth(): Int {
        val paddingSize = paddingSize.get()

        val elementHeight = 9 + (paddingSize + 2) * 2

        return elementHeight * 3 + gap.get() * 2
    }

    override fun getHeight(): Int {
        val paddingSize = paddingSize.get()

        val elementHeight = 9 + (paddingSize + 2) * 2
        val bottomElementHeight = 9 + paddingSize * 2

        return elementHeight * 2 + gap.get() * 3 + bottomElementHeight * 2
    }

    override fun getTranslation(): Translation = keyWidgetTranslation

    override fun getDescription(): Translation = keyWidgetDescription

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(backgroundColor.createRenderableWithFader("widget.background", "Background", "Set the color and opacity of the widget", backgroundOpacity))
        list.add(borderColor.createRenderableWithFader("widget.border", "Border", "Set the color and opacity of the widget's border", borderOpacity))
        list.add(textColor.createRenderable("widget.text_color", "Text Color", "Set the color of the text in the widget"))

        list.add(pressedBackgroundColor.createRenderableWithFader("widget.pressed_background", "Pressed Background", "Set the color and opacity of the widget when a key is pressed", pressedBackgroundOpacity))
        list.add(pressedBorderColor.createRenderableWithFader("widget.pressed_border", "Pressed Border", "Set the color and opacity of the widget's border when a key is pressed", pressedBorderOpacity))
        list.add(pressedTextColor.createRenderable("widget.pressed_text_color", "Pressed Text Color", "Set the color of the text in the widget when a key is pressed"))

        list.add(gap.createRenderable("widget.gap", "Gap", "Set the gap between the keys in the widget"))
        list.add(paddingSize.createRenderable("widget.padding_size", "Padding Size", "Set the padding at the edge of the widget to the text"))
        list.add(borderRadius.createRenderable("widget.border_radius", "Border Radius", "Set the radius of the widget's border corners"))

        super.appendSettingsRenderables(list)
    }
}