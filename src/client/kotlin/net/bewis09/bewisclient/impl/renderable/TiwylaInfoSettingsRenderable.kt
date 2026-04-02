package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.core.getOrNull
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.alpha
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.staticFun
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import kotlin.jvm.optionals.getOrNull

class TiwylaInfoSettingsRenderable : Renderable() {
    val blockInfoList = MultipleBooleanSettingsRenderable(
        Translation("settings.tiwyla_info.title", "Special Block Information"),
        null,
        TiwylaWidget.blockStateInfoMap.map {
            MultipleBooleanSettingsRenderable.Part(
                Component.literal(BuiltInRegistries.BLOCK.getOrNull(createIdentifier(it.key))?.name?.string + " -> " + TiwylaWidget.snake_toCamelCase(it.value.name)),
                null,
                object : SettingInterface<Boolean> {
                    override fun get(): Boolean {
                        return TiwylaWidget.blockSpecialInfoMap[it.key ?: return true] != false
                    }

                    override fun set(value: Boolean?) {
                        TiwylaWidget.blockSpecialInfoMap[it.key ?: return] = value
                    }
                }
            )
        }.staticFun()
    )

    val entityInfoList = MultipleBooleanSettingsRenderable(
        Translation("settings.tiwyla_info.entity.title", "Special Entity Information"),
        null,
        TiwylaWidget.entityInfoProviders.map {
            MultipleBooleanSettingsRenderable.Part(
                Component.literal(it.second.entityType.description.string).append(Component.literal(" "+it.first.namespace).withColor(Color.LIGHT_GRAY.argb)),
                null,
                object : SettingInterface<Boolean> {
                    override fun get(): Boolean {
                        return TiwylaWidget.entitySpecialInfoMap[BuiltInRegistries.ENTITY_TYPE.getKey(it.second.entityType).toString()] != false
                    }

                    override fun set(value: Boolean?) {
                        TiwylaWidget.entitySpecialInfoMap[BuiltInRegistries.ENTITY_TYPE.getKey(it.second.entityType).toString()] = value
                    }
                }
            )
        }.staticFun()
    )

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)

        setHeight(blockInfoList.height.coerceAtLeast(entityInfoList.height) + 5)
    }

    override fun init() {
        if (width < 12) return

        addRenderable(Rectangle(0xFFFFFF alpha 0.25f)(centerX, y + 5, 1, height))
        addRenderable(entityInfoList.setPosition(x, y + 5).setWidth((width - 11) / 2))
        addRenderable(blockInfoList.setPosition(x2 - (width - 11) / 2, y + 5).setWidth((width - 11) / 2))
    }
}