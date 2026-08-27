package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.alpha
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component

class TiwylaInfoSettingsRenderable : PropedRenderable<TiwylaInfoSettingsRenderable>({
    minWidth = 22
}) {
    init { props() }

    val blockInfoList = MultipleBooleanSettingsRenderable {
        this.title = Translation("settings.tiwyla_info.title", "Special Block Information")
        settings = TiwylaWidget.blockStateInfoMap.map {
            MultipleBooleanSettingsRenderable.Part {
                this.title = Component.literal(BuiltInRegistries.BLOCK.getOrNull(createIdentifier(it.key))?.name?.string + " -> " + snake_toCamelCase(it.value.name))
                setting = object : SettingInterfaceWithDefault<Boolean> {
                    override fun get() = TiwylaWidget.blockSpecialInfoMap[it.key] != false

                    override fun set(value: Boolean?) {
                        TiwylaWidget.blockSpecialInfoMap[it.key ?: return] = value
                    }

                    override fun getDefault(): Boolean = true
                }
            }
        }
    }

    val entityInfoList = MultipleBooleanSettingsRenderable {
        this.title = Translation("settings.tiwyla_info.entity.title", "Special Entity Information")
        settings = TiwylaWidget.entityInfoProviders.map {
            MultipleBooleanSettingsRenderable.Part {
                this.title = Component.literal(it.second.entityType.description.string).append(Component.literal(" " + it.first.namespace).withColor(Color.LIGHT_GRAY.argb))
                setting = object : SettingInterfaceWithDefault<Boolean> {
                    override fun get() = TiwylaWidget.entitySpecialInfoMap[BuiltInRegistries.ENTITY_TYPE.getKey(it.second.entityType).toString()] != false

                    override fun set(value: Boolean?) {
                        TiwylaWidget.entitySpecialInfoMap[BuiltInRegistries.ENTITY_TYPE.getKey(it.second.entityType).toString()] = value
                    }

                    override fun getDefault(): Boolean = true
                }
            }
        }
    }

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        updateHeight(blockInfoList.height.coerceAtLeast(entityInfoList.height) + 5)
    }

    override fun Init.init() {
        Rectangle {
            color = 0xFFFFFF alpha 0.25f
        }(centerX, y + 5, 1, height)
        addRenderable(entityInfoList.updatePosition(x, y + 5).updateWidth((width - 11) / 2))
        addRenderable(blockInfoList.updatePosition(x2 - (width - 11) / 2, y + 5).updateWidth((width - 11) / 2))
    }
}