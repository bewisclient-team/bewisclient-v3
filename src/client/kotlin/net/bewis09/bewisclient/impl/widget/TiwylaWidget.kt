package net.bewis09.bewisclient.impl.widget

import com.google.gson.*
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.core.setFont
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.impl.renderable.TiwylaInfoSettingsRenderable
import net.bewis09.bewisclient.impl.renderable.TiwylaLinesSettingsRenderable
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.interfaces.BreakingProgressAccessor
import net.bewis09.bewisclient.logic.*
import net.bewis09.bewisclient.logic.color.color
import net.bewis09.bewisclient.settings.types.BooleanMapSetting
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.block.*
import net.minecraft.entity.*
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Property
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import kotlin.jvm.optionals.getOrNull
import kotlin.math.*

object TiwylaWidget : ScalableWidget(createIdentifier("bewisclient", "tiwyla_widget")), EventEntrypoint {
    private var lineWidth = 0

    var heartStyle: Identifier = createIdentifier("bewisclient", "extra")

    val topTextColor = create("top_text_color", DefaultWidgetSettings.textColor.cloneWithDefault())
    val bottomTextColor = create("bottom_text_color", DefaultWidgetSettings.textColor.cloneWithDefault())
    val backgroundColor = create("background_color", DefaultWidgetSettings.backgroundColor.cloneWithDefault())
    val backgroundOpacity = create("background_opacity", DefaultWidgetSettings.backgroundOpacity.cloneWithDefault())
    val borderColor = create("border_color", DefaultWidgetSettings.borderColor.cloneWithDefault())
    val borderOpacity = create("border_opacity", DefaultWidgetSettings.borderOpacity.cloneWithDefault())
    val borderRadius = create("border_radius", DefaultWidgetSettings.borderRadius.cloneWithDefault())
    val shadow = create("shadow", DefaultWidgetSettings.shadow.cloneWithDefault())
    val paddingSize = create("padding_size", DefaultWidgetSettings.paddingSize.cloneWithDefault())
    val lineSpacing = create("line_spacing", DefaultWidgetSettings.lineSpacing.cloneWithDefault())

    val blockSpecialInfoMap = create("block_special_info_map", BooleanMapSetting())
    val entitySpecialInfoMap = create("entity_special_info_map", BooleanMapSetting())

    val healthInfoText = createTranslation("information.health_information", "The Information of the health of the entity that you are looking at is not available on multiplayer servers due to cheating concerns. In singleplayer worlds it is still available.")

    val entityLines by lazy {
        create(
            "entity_lines", createListSetting(
                listOf(
                    loadEntityInformation("health"), loadEntityInformation("entity_id", "special_entity_info")
                ), ::loadEntityInformation
            )
        )
    }

    val blockLines by lazy {
        create(
            "block_lines", createListSetting(
                listOf(
                    loadBlockInformation("tool"), loadBlockInformation("mining_level", "block_property"), loadBlockInformation("break_time", "progress")
                ), ::loadBlockInformation
            )
        )
    }

    fun <T> createListSetting(default: List<Information<T>>, load: (first: String, second: String?) -> Information<T>) = ListSetting(default, {
        val arr = catch { it.asJsonArray } ?: return@ListSetting null
        val strings = arr.mapNotNull { a -> catch { a.asString } }

        if (strings.isEmpty()) return@ListSetting null

        load(strings[0], strings.getOrNull(1))
    }, {
        JsonArray().also { list ->
            it.first?.let { s -> list.add(s.id) }
            it.second?.let { s -> list.add(s.id) }
        }.let { l -> if (l.isEmpty) null else l }
    })

    val entityInfoProviders = APIEntrypointLoader.mapContainer { it.entrypoint.getTiwylaEntityExtraInfoProviders().map { provider -> Identifier.of(it.provider.metadata.id, Registries.ENTITY_TYPE.getId(provider.entityType).toString().replace(":", "/")) to provider } }.flatten()

    val progressText = createTranslation("progress", "Progress: %s%%")
    val toolText = createTranslation("tool", "Tool: %s")
    val miningLevel = createTranslation("mining_level", "Mining Level: %s")

    val axeToolText = createTranslation("tool.axe", "Axe")
    val pickaxeToolText = createTranslation("tool.pickaxe", "Pickaxe")
    val hoeToolText = createTranslation("tool.hoe", "Hoe")
    val shovelToolText = createTranslation("tool.shovel", "Shovel")
    val swordToolText = createTranslation("tool.sword", "Sword")
    val noneToolText = createTranslation("tool.none", "None")

    val noneLevelText = createTranslation("mining_level.none", "None")
    val woodLevelText = createTranslation("mining_level.wood", "Wood")
    val stoneLevelText = createTranslation("mining_level.stone", "Stone")
    val ironLevelText = createTranslation("mining_level.iron", "Iron")
    val diamondLevelText = createTranslation("mining_level.diamond", "Diamond")

    val instantText = createTranslation("instant", "Instant")
    val secondsText = createTranslation("seconds", "%s seconds")
    val minutesText = createTranslation("minutes", "%s minutes")
    val hoursText = createTranslation("hours", "%s hours")
    val daysText = createTranslation("days", "%s days")

    val blockStateInfoMap = sortedMapOf<String, Property<*>>()

    override fun defaultPosition(): WidgetPosition = SidedPosition(0, 5, SidedPosition.CENTER, SidedPosition.START)

    override fun render(screenDrawing: ScreenDrawing) {
        val title = getTiwylaTitle() ?: return

        lineWidth = screenDrawing.getTextWidth(title) + 2 * paddingSize()

        val lines = getSublines()

        screenDrawing.fillWithBorderRounded(
            0, 0, getWidth(), getHeight(), borderRadius(), backgroundColor().getColor() alpha backgroundOpacity(), borderColor().getColor() alpha borderOpacity()
        )

        screenDrawing.drawCenteredText(title, getWidth() / 2, paddingSize(), topTextColor().getColor(), shadow())

        lines.forEachIndexed { i, line ->
            screenDrawing.transform(getWidth() / 2f, paddingSize() + 9f + lineSpacing() + (i * (6 + lineSpacing())), 0.77f) {
                if (!shadow()) {
                    screenDrawing.drawCenteredText(line, 0, 0, bottomTextColor().getColor())
                } else {
                    screenDrawing.drawCenteredTextWithShadow(line, 0, 0, bottomTextColor().getColor())
                }
            }
        }
    }

    override fun isHidden(): Boolean = getTiwylaTitle() == null

    fun getTiwylaTitle(): Text? {
        if (!util.isInWorld()) return Blocks.GRASS_BLOCK.name

        return onHitResult({ data ->
            data.state.block.name
        }, { entity ->
            entity.name
        })
    }

    fun <T> onHitResult(block: (hitResult: BlockData) -> T, entity: (hitResult: Entity) -> T): T? {
        val world = client.world ?: return null

        return when (val hitResult = client.crosshairTarget) {
            is BlockHitResult -> if (world.getBlockState(hitResult.blockPos).isAir) null else block(BlockData(world.getBlockState(hitResult.blockPos), hitResult.blockPos))
            is EntityHitResult -> entity(hitResult.entity)
            else -> null
        }
    }

    fun getSublines(): List<Text> {
        if (!util.isInWorld()) return getBlockSublines(BlockData(Blocks.GRASS_BLOCK.defaultState, BlockPos.ORIGIN))

        return onHitResult(::getBlockSublines, ::getEntitySublines) ?: listOf()
    }

    fun getBlockSublines(data: BlockData): List<Text> {
        return blockLines.mapNotNull {
            arrayOf(it.second, it.first).filterNotNull().sortedBy { a -> -a.priority }.firstNotNullOfOrNull { a -> a(data) }
        }
    }

    fun getEntitySublines(entity: Entity): List<Text> {
        return entityLines.mapNotNull {
            arrayOf(it.second, it.first).filterNotNull().sortedBy { a -> -a.priority }.firstNotNullOfOrNull { a -> a(entity) }
        }
    }

    override fun getWidth(): Int = 150.coerceAtLeast(lineWidth).coerceAtMost(250)

    override fun getHeight(): Int = 9 + getSublines().size * 6 + lineSpacing.get() * (getSublines().size) + 2 * paddingSize.get()

    override val title = "Tiwyla Widget"
    override val description = "Show information about the block or entity you are looking at."

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            topTextColor.createRenderable(
                "widget.top_text_color", "Top Text Color", "Set the color of the top text in the widget"
            )
        )
        list.add(
            bottomTextColor.createRenderable(
                "widget.bottom_text_color", "Bottom Text Color", "Set the color of the bottom text in the widget"
            )
        )
        list.add(TiwylaLinesSettingsRenderable())
        list.add(InfoTextRenderable(healthInfoText.getTranslatedString(), 0xAAAAAA.color, true))
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
            lineSpacing.createRenderable(
                "widget.line_spacing", "Line Spacing", "Set the spacing between lines of text in the widget"
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

        list.add(TiwylaInfoSettingsRenderable())
    }

    fun loadBlockInformation(first: String, second: String? = null): Information<BlockData> {
        return Information(first = blockInformation.firstOrNull { it.id == first }, second = blockInformation.firstOrNull { it.id == second })
    }

    fun loadEntityInformation(first: String, second: String? = null): Information<Entity> {
        return Information(first = entityInformation.firstOrNull { it.id == first }, second = entityInformation.firstOrNull { it.id == second })
    }

    data class Information<T>(
        val first: Line<T>?, val second: Line<T>?
    ) {
        data class Line<T>(val fn: (data: T) -> Text?, val id: String, val priority: Int) {
            val translation = createTranslation("information.$id", `snake_toWord With Spaces`(id))

            operator fun invoke(data: T): Text? = fn(data)
        }
    }

    data class BlockData(val state: BlockState, val blockPos: BlockPos)

    val blockInformation = listOf<Information.Line<BlockData>>(
        Information.Line({ data ->
            if (data.state.isIn(BlockTags.AXE_MINEABLE)) return@Line toolText(axeToolText.getTranslatedString())
            if (data.state.isIn(BlockTags.PICKAXE_MINEABLE)) return@Line toolText(pickaxeToolText.getTranslatedString())
            if (data.state.isIn(BlockTags.HOE_MINEABLE)) return@Line toolText(hoeToolText.getTranslatedString())
            if (data.state.isIn(BlockTags.SHOVEL_MINEABLE)) return@Line toolText(shovelToolText.getTranslatedString())
            if (data.state.isIn(BlockTags.SWORD_EFFICIENT)) return@Line toolText(swordToolText.getTranslatedString())
            return@Line toolText(noneToolText.getTranslatedString())
        }, "tool", 0), Information.Line({ data ->
            if (data.state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) return@Line miningLevel(diamondLevelText.getTranslatedString())
            if (data.state.isIn(BlockTags.NEEDS_IRON_TOOL)) return@Line miningLevel(ironLevelText.getTranslatedString())
            if (data.state.isIn(BlockTags.NEEDS_STONE_TOOL)) return@Line miningLevel(stoneLevelText.getTranslatedString())

            if (data.state.isToolRequired) return@Line miningLevel(woodLevelText.getTranslatedString())
            return@Line miningLevel(noneLevelText.getTranslatedString())
        }, "mining_level", 0), Information.Line({ data ->
            if (!util.isInWorld()) return@Line secondsText(4.5)

            if (data.state.calcBlockBreakingDelta(client.player, client.world, data.blockPos) > 1) return@Line instantText()

            val secs = (1f / data.state.calcBlockBreakingDelta(client.player, client.world, data.blockPos) * 5F).roundToInt() / 100F

            if (secs > (3600 * 24)) return@Line daysText((secs / 36 / 24).roundToInt() / 100F)
            if (secs > 3600) return@Line hoursText((secs / 36).roundToInt() / 100F)
            if (secs > 60) return@Line minutesText((secs / 6 * 10).roundToInt() / 100F)
            return@Line secondsText((secs * 100).roundToInt() / 100F)
        }, "break_time", 0), Information.Line({ _ ->
            val s = ((client.interactionManager as BreakingProgressAccessor?)?.getCurrentBreakingProgress() ?: 0f) * 1000
            if (s == 0F) {
                return@Line null
            }
            return@Line progressText(round(s) / 10f)
        }, "progress", 2), Information.Line({ data ->
            val id = data.state.blockId().toString()
            if (blockSpecialInfoMap[id] == false) return@Line null
            val property = blockStateInfoMap[id] ?: return@Line null
            return@Line "${snake_toCamelCase(property.name)}: ${data.state.get(property)}".toText()
        }, "block_property", 1)
    )

    val entityInformation = listOf<Information.Line<Entity>>(
        Information.Line({ entity ->
            return@Line entity.entityId().toString().toText()
        }, "entity_id", 0), Information.Line({ entity ->
            return@Line if (client.isInSingleplayer) (entity as? LivingEntity)?.let {
                convertToHearths(
                    it.health.toDouble(), it.maxHealth.toDouble(), it.absorptionAmount.toDouble()
                )
            } else null
        }, "health", 1), Information.Line({ entity ->
            if (entity.entityId().let { entitySpecialInfoMap[it.toString()] } == false) return@Line null
            return@Line provideEntityInfo(entity)?.toText()
        }, "special_entity_info", 2)
    )

    fun convertToHearths(h: Double, mH: Double, a: Double): Text {
        var health = h
        var maxHealth = mH
        var absorption = a
        try {
            maxHealth = roundUpAndHalf(maxHealth)
            health = ((health * 10).toInt().toDouble()) / 10f
            absorption = ((absorption * 10).toInt().toDouble()) / 10f
            if (maxHealth > 13.0) {
                return (health.toString() + " / " + maxHealth * 2 + " HP").toText()
            }
            health = roundUpAndHalf(health)
            absorption = roundUpAndHalf(absorption)
            val isHalf = health != health.toInt().toDouble()
            val isAbsorptionHalf = absorption != absorption.toInt().toDouble()
            val isMaxHalf = maxHealth != (((maxHealth * 2).toInt().toDouble()) / 2).toInt().toDouble()
            val maxHealthLeft = (maxHealth - ((health.toInt()) + (if (isHalf) 1 else 0)) + (if (isMaxHalf) 1 else 0)).toInt()
            return ("❤".repeat(health.toInt())).toText()
                .setColor(0xFF0000)
                .append((if (isHalf) "\uE0aa" else "").toText().setFont(heartStyle).setColor(0xFFFFFF))
                .append(("❤".repeat(maxHealthLeft)).toText().setColor(0xFFFFFF))
                .append(("❤".repeat(absorption.toInt())).toText().setColor(0xFFFF00))
                .append((if (isAbsorptionHalf) "\uE0ab" else "").toText().setFont(heartStyle).setColor(0xFFFF00))
        } catch (_: Exception) {
            return "".toText()
        }
    }

    fun roundUpAndHalf(i: Double): Double {
        return ceil(i) / 2.0
    }

    fun <T : Entity> provideEntityInfo(entity: T): String? {
        @Suppress("UNCHECKED_CAST") val provider = entityInfoProviders.firstOrNull { entity.type == it.second.entityType }?.second as? EntityInfoProvider<T> ?: return null
        return provider.fn(entity)
    }

    @Suppress("FunctionName")
    private fun `snake_toWord With Spaces`(str: String): String {
        return str.split("_".toRegex()).filter { it.isNotEmpty() }.joinToString(" ") {
            it.replaceFirstChar(Char::uppercaseChar)
        }
    }

    @Suppress("FunctionName")
    private fun snake_toCamelCase(str: String): String {
        return str.split("_".toRegex()).filter { it.isNotEmpty() }.mapIndexed { i, it ->
            if (i == 0) it.lowercase() else it.replaceFirstChar(Char::uppercaseChar)
        }.joinToString("")
    }

    override fun onResourcesReloaded() {
        blockStateInfoMap.clear()

        val resources = util.findAllResources(
            "bewisclient/block_information"
        ) { it.path.endsWith(".json") }

        resources.entries.forEach {
            it.value.forEach { resource ->
                val jsonElement = Gson().fromJson(resource.reader, JsonElement::class.java)

                if (jsonElement?.isJsonObject == true) {
                    val jsonObject = jsonElement.asJsonObject

                    jsonObject.keySet().forEach { block ->
                        val property = jsonObject.get(block)

                        if (property.isJsonPrimitive) {
                            val propertyId = property.asString
                            val b: Block = Registries.BLOCK.get(Identifier.of(block))
                            if (b == Registries.BLOCK.defaultEntry.getOrNull()) return@forEach
                            blockStateInfoMap[block] = b.stateManager.properties.firstOrNull { a -> a.name == propertyId } ?: run {
                                warn("Unknown block property: $propertyId for block $block in pack ${resource.packId}")
                                return@forEach
                            }
                        } else {
                            warn("Invalid block property format for $block in ${it.key}")
                        }
                    }
                } else {
                    warn("Invalid block information JSON format in ${it.key}")
                }
            }
        }
    }

    override fun getDefaultScale(): Float = 1f

    data class EntityInfoProvider<T : Entity>(val entityType: EntityType<T>, val fn: (entity: T) -> String?)
}