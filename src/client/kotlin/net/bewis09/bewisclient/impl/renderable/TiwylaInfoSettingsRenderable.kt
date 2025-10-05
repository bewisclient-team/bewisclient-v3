package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.util.color.alpha
import net.bewis09.bewisclient.util.staticFun
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

class TiwylaInfoSettingsRenderable : Renderable() {
    val blockInfoList = MultipleBooleanSettingsRenderable(
        Translation("settings.tiwyla_info.title", "Special Block Information"),
        null,
        TiwylaWidget.blockStateInfoMap.map {
            MultipleBooleanSettingsRenderable.Part(
                Translation.literal(Registries.BLOCK.get(Identifier.of(it.key)).name.string + " -> " + it.value.name),
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
                Translation.literal(it.second.entityType.name.string + " §0(${it.first.namespace})"),
                null,
                object : SettingInterface<Boolean> {
                    override fun get(): Boolean {
                        return TiwylaWidget.entitySpecialInfoMap[Registries.ENTITY_TYPE.getEntry(it.second.entityType).key.getOrNull()?.value?.toString() ?: return true] != false
                    }

                    override fun set(value: Boolean?) {
                        TiwylaWidget.entitySpecialInfoMap[Registries.ENTITY_TYPE.getEntry(it.second.entityType).key.getOrNull()?.value?.toString() ?: return] = value
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