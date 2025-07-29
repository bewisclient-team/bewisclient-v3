package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.DescriptionSettingCategory
import net.bewis09.bewisclient.drawable.renderables.ScrollGrid
import net.bewis09.bewisclient.drawable.renderables.ImageSettingCategory
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.WidgetLoader
import kotlin.collections.listOf

class SettingsStructure(val screen: OptionScreen) {
    val utilities = listOf(
        ImageSettingCategory("fullbright", Translation("menu.category.fullbright", "Fullbright"), arrayOf()),
        ImageSettingCategory("block_highlight", Translation("menu.category.block_highlight", "Block Highlight"), arrayOf()),
        ImageSettingCategory("held_item_info", Translation("menu.category.held_item_info", "Held Item Info"), arrayOf()),
        ImageSettingCategory("zoom", Translation("menu.category.zoom", "Zoom"), arrayOf()),
        ImageSettingCategory("pumpkin_overlay", Translation("menu.category.pumpkin_overlay", "Pumpkin Overlay"), arrayOf()),
        ImageSettingCategory("crosshair", Translation("menu.category.crosshair", "Crosshair"), arrayOf()),
        ImageSettingCategory("better_visibility", Translation("menu.category.better_visibility", "Better Visibility"), arrayOf()),
        ImageSettingCategory("scoreboard", Translation("menu.category.scoreboard", "Scoreboard"), arrayOf()),
        ImageSettingCategory("cleaner_debug_menu", Translation("menu.category.cleaner_debug_menu", "Cleaner Debug Menu"), arrayOf()),
        ImageSettingCategory("chat_enhancements", Translation("menu.category.chat_enhancements", "Chat Enhancements"), arrayOf()),
    )

    val widgets = WidgetLoader.widgets.map {
        DescriptionSettingCategory(it.getTranslation(), it.getDescription(), arrayListOf<Renderable>().also { list -> it.appendSettingsRenderables(list) }.toTypedArray())
    }

    val settings = listOf<ImageSettingCategory>(

    )

    val cosmetics = listOf<ImageSettingCategory>(

    )

    val extensions = listOf<ImageSettingCategory>(

    )

    val contact = listOf<ImageSettingCategory>(

    )

    val sidebarCategories = listOf(
        createSidebarCategory(Translation("menu.category.widgets", "Widgets"), widgets),
        createSidebarCategory(Translation("menu.category.utilities", "Utilities"), utilities),
        createSidebarCategory(Translation("menu.category.settings", "Settings"), settings),
        createSidebarCategory(Translation("menu.category.cosmetics", "Cosmetics"), cosmetics),
        createSidebarCategory(Translation("menu.category.extensions", "Extensions"), extensions),
        createSidebarCategory(Translation("menu.category.contact", "Contact"), contact)
    )

    fun createSidebarCategory(name: Translation, settings: List<Renderable>): Button {
        return Button(name.getTranslatedString()) {
            screen.transformInside(
                Text(name.getTranslatedString(), centered = true).setHeight(12),
                ScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80)
            )
        }.setHeight(14) as Button
    }
}