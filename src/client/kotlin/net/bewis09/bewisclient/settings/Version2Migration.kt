package net.bewis09.bewisclient.settings

import com.google.gson.Gson
import com.google.gson.JsonElement
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.settings.functionalities.*
import net.bewis09.bewisclient.impl.widget.*
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.*
import net.bewis09.bewisclient.util.color.ChangingColorSaver
import net.bewis09.bewisclient.util.color.ColorSaver
import net.bewis09.bewisclient.util.color.StaticColorSaver
import net.bewis09.bewisclient.util.color.color
import net.bewis09.bewisclient.util.logic.BewisclientInterface

object Version2Migration : BewisclientInterface {
    fun update(): Boolean {
        if (createBewisclientFile("bewisclient.json").exists()) return false

        updateFromFile("general.json") {
            mapBoolean("screenshot_folder_open", setting = ScreenshotSettings.redirect, default = false)
            mapBoolean("instant_zoom", setting = ZoomSettings.instant)
            mapBoolean("hard_zoom", setting = ZoomSettings.smooth) { it.not() }
            mapBoolean("zoom_enabled", setting = ZoomSettings.enabled)
            mapBoolean("perspective", setting = PerspectiveSettings.enabled)
        }

        updateFromFile("design.json") {
            mapBoolean("options_menu", "show_game_menu", setting = OptionsMenuSettings.buttonInGameScreen)
            mapBoolean("options_menu", "show_title_menu", setting = OptionsMenuSettings.buttonInTitleScreen)
            mapFloat("fire_height", setting = FireHeightSettings.height) { (it - 0.6f) * 2.5f }
            enableOnNotDefault(FireHeightSettings.enabled, FireHeightSettings.height)
            mapBoolean("fullbright", "enabled", setting = FullbrightSettings.enabled)
            mapBoolean("fullbright", "night_vision", setting = FullbrightSettings.nightVision)
            mapFloat("fullbright", "value", setting = FullbrightSettings.brightness, default = 1f)
            mapBoolean("better_visibility", "lava", setting = BetterVisibilitySettings.lava)
            mapBoolean("better_visibility", "water", setting = BetterVisibilitySettings.water)
            mapBoolean("better_visibility", "nether", setting = BetterVisibilitySettings.nether)
            mapBoolean("better_visibility", "powder_snow", setting = BetterVisibilitySettings.powder_snow)
            enableOnNotDefault(BetterVisibilitySettings.enabled, BetterVisibilitySettings.lava, BetterVisibilitySettings.water, BetterVisibilitySettings.nether, BetterVisibilitySettings.powder_snow)
            mapBoolean("blockhit", "enabled", setting = BlockHighlightSettings.enabled)
            mapFloat("blockhit", "alpha", setting = BlockHighlightSettings.thickness)
            mapColorSaver("blockhit", "color", setting = BlockHighlightSettings.color)
            mapBoolean("blockhit", "hit_overlay", "enabled", setting = EntityHighlightSettings.enabled)
            mapColorSaver("blockhit", "hit_overlay", "color", setting = EntityHighlightSettings.color)
            mapFloat("blockhit", "hit_overlay", "alpha", setting = EntityHighlightSettings.alpha)
            mapBoolean("disable_pumpkin_overlay", setting = PumpkinOverlaySettings.enabled)
            mapBoolean("held_item_info", "held_item_info", setting = HeldItemTooltipSettings.enabled)
            mapInt("held_item_info", "maxinfolength", setting = HeldItemTooltipSettings.maxShownLines)
            mapBoolean("shulker_box_tooltip", setting = ShulkerBoxTooltipSettings.enabled)
            mapFloat("scoreboard", "scale", setting = ScoreboardSettings.scale)
            enableOnNotDefault(ScoreboardSettings.enabled, ScoreboardSettings.scale)
        }

        updateFromFile("widgets.json") {
            mapBoolean("keys", "enabled", setting = KeyWidget.enabled, default = true)
            mapBoolean("keys", "select_parts", "show_space_bar", setting = KeyWidget.showJumpKey)
            mapBoolean("keys", "select_parts", "show_mouse_button", setting = KeyWidget.showAttackUseKeys)
            mapBoolean("keys", "select_parts", "show_movement_keys", setting = KeyWidget.showMovementKeys)
            mapBoolean("keys", "show_cps", setting = KeyWidget.showCPS)

            mapBoolean("coordinates", "enabled", setting = CoordinatesWidget.enabled, default = true)
            mapBoolean("coordinates", "show_biome", setting = CoordinatesWidget.showBiome)
            mapBoolean("coordinates", "show_direction", setting = CoordinatesWidget.showDirection)
            mapBoolean("coordinates", "show_direction", setting = CoordinatesWidget.showCoordinateChange)
            mapBoolean("coordinates", "colorcode_biome", setting = CoordinatesWidget.colorCodeBiome)

            mapBoolean("inventory", "enabled", setting = InventoryWidget.enabled, default = true)

            mapBoolean("daytime", "enabled", setting = DaytimeWidget.enabled, default = true)
            mapBoolean("daytime", "24Clock", setting = DaytimeWidget.format12Hours) { it.not() }

            mapBoolean("ping", "enabled", setting = PingWidget.enabled, default = true)

            mapBoolean("biome", "enabled", setting = BiomeWidget.enabled, default = true)
            mapBoolean("biome", "colorcode_biome", setting = BiomeWidget.colorCodeBiome)

            mapBoolean("speed", "enabled", setting = SpeedWidget.enabled, default = true)
            mapBoolean("speed", "vertical_speed", setting = SpeedWidget.verticalSpeed)

            mapBoolean("cps", "enabled", setting = CPSWidget.enabled, default = true)
            on("cps", "cps_elements") { obj ->
                when (obj.int()) {
                    0 -> { CPSWidget.leftEnabled.setWithoutSave(true); CPSWidget.rightEnabled.setWithoutSave(true) }
                    1 -> { CPSWidget.leftEnabled.setWithoutSave(true); CPSWidget.rightEnabled.setWithoutSave(false) }
                    2 -> { CPSWidget.leftEnabled.setWithoutSave(false); CPSWidget.rightEnabled.setWithoutSave(true) }
                }
            }

            mapBoolean("tiwyla", "enabled", setting = TiwylaWidget.enabled, default = true)
            on("tiwyla", "select_parts", "show_health_information") { if (it.boolean() == false) TiwylaWidget.entityLines.removeFirst() }
            TiwylaWidget.blockLines.setWithoutSave(mutableListOf(
                mapTiwylaBlockLines("first_line"),
                mapTiwylaBlockLines("second_line"),
                mapTiwylaBlockLines("third_line")
            ).filterNotNull().toMutableList())

            mapBoolean("fps", "enabled", setting = FPSWidget.enabled, default = true)

            mapBoolean("days", "enabled", setting = DayWidget.enabled, default = true)
        }

        return true
    }

    fun JsonElement.mapTiwylaBlockLines(name: String): TiwylaWidget.Information<TiwylaWidget.BlockData>? {
        return on("tiwyla", name) {
            return@on when (it.int() ?: return@on null) {
                0 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("tool"), null)
                1 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("mining_level"), null)
                2 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("break_time"), null)
                3 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("tool"), TiwylaWidget.findBlockInformation("progress"))
                4 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("mining_level"), TiwylaWidget.findBlockInformation("progress"))
                5 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("break_time"), TiwylaWidget.findBlockInformation("progress"))
                6 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("tool"), TiwylaWidget.findBlockInformation("block_property"))
                7 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("mining_level"), TiwylaWidget.findBlockInformation("block_property"))
                8 -> TiwylaWidget.Information(TiwylaWidget.findBlockInformation("break_time"), TiwylaWidget.findBlockInformation("block_property"))
                else -> null
            }
        }
    }

    fun enableOnNotDefault(enableSetting: Setting<Boolean>, vararg settings: Setting<*>) {
        fun <T> checkDefault(setting: Setting<T>) = setting.get() == setting.default()

        if (settings.any { !checkDefault(it) }) {
            enableSetting.setWithoutSave(true)
        }
    }

    fun JsonElement.mapColorSaver(vararg oldPath: String, setting: Setting<ColorSaver>, default: ColorSaver? = null, mapper: (ColorSaver) -> ColorSaver = { it }) {
        map(*oldPath, setting = setting, default = default, rec = {
            val value = this@map.string() ?: return@map null

            if (!value.startsWith("0x")) return@map null

            val int = value.substring(2).toIntOrNull(16) ?: return@map null
            if (int < 0) return@map ChangingColorSaver(-int)
            return@map StaticColorSaver(int.color)
        }, mapper = mapper)
    }

    fun JsonElement.mapInt(vararg oldPath: String, setting: Setting<Int>, default: Int? = null, mapper: (Int) -> Int = { it }) {
        map(*oldPath, setting = setting, default = default, rec = { float()?.toInt() }, mapper = mapper)
    }

    fun JsonElement.mapFloat(vararg oldPath: String, setting: Setting<Float>, default: Float? = null, mapper: (Float) -> Float = { it }) {
        map(*oldPath, setting = setting, default = default, rec = { float() }, mapper = mapper)
    }

    fun JsonElement.mapBoolean(vararg oldPath: String, setting: Setting<Boolean>, default: Boolean? = null, mapper: (Boolean) -> Boolean = { it }) {
        map(*oldPath, setting = setting, default = default, rec = { boolean() }, mapper = mapper)
    }

    fun <T, E> JsonElement.map(vararg oldPath: String, setting: Setting<E>, default: E? = null, rec: JsonElement.() -> T?, mapper: (T) -> E) {
        on(*oldPath) { obj ->
            setting.setWithoutSave(obj.rec()?.let { mapper(it) } ?: default)
        }
    }

    fun <T> JsonElement.on(vararg oldPath: String, then: (JsonElement) -> T?): T? {
        var obj = this

        for (path in oldPath) {
            if (!obj.isJsonObject) return null
            obj = obj.asJsonObject.get(path) ?: continue
        }

        return then(obj)
    }

    fun updateFromFile(fileName: String, updater: JsonElement.() -> Unit) {
        catchAndPrint {
            val file = createBewisclientFile(fileName)
            val jsonElement = Gson().fromJson(file.reader(), JsonElement::class.java)

            updater(jsonElement)
        }
    }
}