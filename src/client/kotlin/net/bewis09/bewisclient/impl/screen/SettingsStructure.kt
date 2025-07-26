package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.ScrollGrid
import net.bewis09.bewisclient.drawable.renderables.SettingCategory
import net.bewis09.bewisclient.game.Translation
import kotlin.collections.listOf

class SettingsStructure(val screen: OptionScreen) {
    val utilities = listOf(
        SettingCategory("fullbright", Translation("menu.category.fullbright", "Fullbright"), arrayOf()),
        SettingCategory("block_highlight", Translation("menu.category.block_highlight", "Block Highlight"), arrayOf()),
        SettingCategory("held_item_info", Translation("menu.category.held_item_info", "Held Item Info"), arrayOf()),
        SettingCategory("zoom", Translation("menu.category.zoom", "Zoom"), arrayOf()),
        SettingCategory("pumpkin_overlay", Translation("menu.category.pumpkin_overlay", "Pumpkin Overlay"), arrayOf()),
        SettingCategory("crosshair", Translation("menu.category.crosshair", "Crosshair"), arrayOf()),
        SettingCategory("better_visibility", Translation("menu.category.better_visibility", "Better Visibility"), arrayOf()),
        SettingCategory("scoreboard", Translation("menu.category.scoreboard", "Scoreboard"), arrayOf()),
        SettingCategory("cleaner_debug_menu", Translation("menu.category.cleaner_debug_menu", "Cleaner Debug Menu"), arrayOf()),
        SettingCategory("chat_enhancements", Translation("menu.category.chat_enhancements", "Chat Enhancements"), arrayOf()),
    )

    val widgets = listOf<SettingCategory>(

    )

    val settings = listOf<SettingCategory>(

    )

    val cosmetics = listOf<SettingCategory>(

    )

    val extensions = listOf<SettingCategory>(

    )

    val contact = listOf<SettingCategory>(

    )

    val sidebarCategories = listOf(
        createSidebarCategory(Translation("menu.category.widgets", "Widgets"), widgets),
        createSidebarCategory(Translation("menu.category.utilities", "Utilities"), utilities),
        createSidebarCategory(Translation("menu.category.settings", "Settings"), settings),
        createSidebarCategory(Translation("menu.category.cosmetics", "Cosmetics"), cosmetics),
        createSidebarCategory(Translation("menu.category.extensions", "Extensions"), extensions),
        createSidebarCategory(Translation("menu.category.contact", "Contact"), contact)
    )

    fun createSidebarCategory(name: Translation, settings: List<SettingCategory>): Button {
        return Button(name.getTranslatableString()) {
            screen.optionsPane = ScrollGrid(
                { settings.map { it.setHeight(90) } },
                5,
                80
            )
            screen.resize()
        }.setHeight(14) as Button
    }
}