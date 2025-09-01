package net.bewis09.bewisclient.impl.widget

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.renderable.TiwylaLinesSettingsRenderable
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.interfaces.BreakingProgressAccessor
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Property
import net.minecraft.text.Style
import net.minecraft.text.StyleSpriteSource
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.roundToInt

object TiwylaWidget : ScalableWidget(), EventEntrypoint {
    var heartsFont: StyleSpriteSource = StyleSpriteSource.Font(Identifier.of("bewisclient", "extra"))

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

    val healthInfoText = Translation("widget.tiwyla_widget.information.health_information", "The Information of the health of the entity that you are looking at is not available on multiplayer servers due to cheating concerns. In singleplayer worlds it is still available.")

    val entityLines by lazy {
        create(
            "entity_lines", ListSetting<Information<Entity>>(
                listOf(
                    loadEntityInformation("health"),
                    loadEntityInformation("entity_id", "special_entity_info")
                ), {
                    val arr = catch { it.asJsonArray } ?: return@ListSetting null
                    val strings = arr.mapNotNull { a -> catch { a.asString } }

                    if (strings.isEmpty()) return@ListSetting null

                    loadEntityInformation(strings[0], strings.getOrNull(1))
                }, {
                    JsonArray().also { list ->
                        it.first?.let { s -> list.add(s.id) }
                        it.second?.let { s -> list.add(s.id) }
                    }.let { l -> if (l.isEmpty) null else l }
                }
            )
        )
    }

    val blockLines by lazy {
        create(
            "block_lines", ListSetting(
                listOf(
                    loadBlockInformation("tool"),
                    loadBlockInformation("mining_level", "block_property"),
                    loadBlockInformation("break_time", "progress")
                ), {
                    val arr = catch { it.asJsonArray } ?: return@ListSetting null
                    val strings = arr.mapNotNull { a -> catch { a.asString } }

                    if (strings.isEmpty()) return@ListSetting null

                    loadBlockInformation(strings[0], strings.getOrNull(1))
                }, {
                    JsonArray().also { list ->
                        it.first?.let { s -> list.add(s.id) }
                        it.second?.let { s -> list.add(s.id) }
                    }.let { l -> if (l.isEmpty) null else l }
                }
            )
        )
    }

    val entityInfoProviders = APIEntrypointLoader.mapEntrypoint { it.getTiwylaEntityExtraInfoProviders() }.flatten()

    val tiwylaWidgetTranslation = Translation("widget.tiwyla_widget.name", "Tiwyla Widget")
    val tiwylaWidgetDescription = Translation(
        "widget.tiwyla_widget.description", "A widget for displaying Tiwyla information."
    )

    val progressText = Translation("widget.tiwyla_widget.progress", "Progress: %s%%")
    val toolText = Translation("widget.tiwyla_widget.tool", "Tool: %s")
    val miningLevel = Translation("widget.tiwyla_widget.mining_level", "Mining Level: %s")

    val axeToolText = Translation("widget.tiwyla_widget.tool.axe", "Axe")
    val pickaxeToolText = Translation("widget.tiwyla_widget.tool.pickaxe", "Pickaxe")
    val hoeToolText = Translation("widget.tiwyla_widget.tool.hoe", "Hoe")
    val shovelToolText = Translation("widget.tiwyla_widget.tool.shovel", "Shovel")
    val swordToolText = Translation("widget.tiwyla_widget.tool.sword", "Sword")
    val noneToolText = Translation("widget.tiwyla_widget.tool.none", "None")

    val noneLevelText = Translation("widget.tiwyla_widget.mining_level.none", "None")
    val woodLevelText = Translation("widget.tiwyla_widget.mining_level.wood", "Wood")
    val stoneLevelText = Translation("widget.tiwyla_widget.mining_level.stone", "Stone")
    val ironLevelText = Translation("widget.tiwyla_widget.mining_level.iron", "Iron")
    val diamondLevelText = Translation("widget.tiwyla_widget.mining_level.diamond", "Diamond")

    val instantText = Translation("widget.tiwyla_widget.instant", "Instant")
    val secondsText = Translation("widget.tiwyla_widget.seconds", "%s seconds")
    val minutesText = Translation("widget.tiwyla_widget.minutes", "%s minutes")
    val hoursText = Translation("widget.tiwyla_widget.hours", "%s hours")
    val daysText = Translation("widget.tiwyla_widget.days", "%s days")

    val blockBlockStateMap = hashMapOf<Block, Property<*>>()

    override fun defaultPosition(): WidgetPosition = SidedPosition(0, 5, SidedPosition.TransformerType.CENTER, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "tiwyla_widget")

    override fun render(screenDrawing: ScreenDrawing) {
        val backgroundColor = backgroundColor.get().getColor()
        val backgroundOpacity = backgroundOpacity.get()
        val borderColor = borderColor.get().getColor()
        val borderOpacity = borderOpacity.get()
        val borderRadius = borderRadius.get()
        val shadow = shadow.get()
        val paddingSize = paddingSize.get()
        val lineSpacing = lineSpacing.get()
        val topTextColor = topTextColor.get().getColor()
        val bottomTextColor = bottomTextColor.get().getColor()

        val title = getTitle() ?: return

        val lines = getSublines()

        screenDrawing.fillWithBorderRounded(
            0, 0, getWidth(), getHeight(), borderRadius, backgroundColor, backgroundOpacity, borderColor, borderOpacity
        )

        if (!shadow) {
            screenDrawing.drawCenteredText(title, getWidth() / 2, paddingSize, topTextColor, 1f)
        } else {
            screenDrawing.drawCenteredTextWithShadow(title, getWidth() / 2, paddingSize, topTextColor, 1f)
        }

        lines.forEachIndexed { i, line ->
            screenDrawing.push()
            screenDrawing.translate(getWidth() / 2f, paddingSize + 9f + lineSpacing + (i * (6 + lineSpacing)))
            screenDrawing.scale(0.77f, 0.77f)
            if (!shadow) {
                screenDrawing.drawCenteredText(line, 0, 0, bottomTextColor, 1f)
            } else {
                screenDrawing.drawCenteredTextWithShadow(line, 0, 0, bottomTextColor, 1f)
            }
            screenDrawing.pop()
        }
    }

    override fun isHidden(): Boolean = getTitle() == null

    fun getTitle(): String? {
        if (MinecraftClient.getInstance().world == null) return Blocks.GRASS_BLOCK.name.string

        return onHitResult({ data ->
            data.state.block.name.string
        }, { entity ->
            entity.name.string
        })
    }

    fun <T> onHitResult(block: (hitResult: BlockData) -> T, entity: (hitResult: Entity) -> T): T? {
        val world = MinecraftClient.getInstance().world ?: return null

        return when (val hitResult = MinecraftClient.getInstance().crosshairTarget) {
            is BlockHitResult -> if (world.getBlockState(hitResult.blockPos).isAir) null else block(BlockData(world.getBlockState(hitResult.blockPos), hitResult.blockPos))
            is EntityHitResult -> entity(hitResult.entity)
            else -> null
        }
    }

    fun getSublines(): List<Text> {
        if (MinecraftClient.getInstance().world == null) return getBlockSublines(BlockData(Blocks.GRASS_BLOCK.defaultState, BlockPos.ORIGIN))

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

    override fun getWidth(): Int = 150

    override fun getHeight(): Int = 9 + getSublines().size * 6 + lineSpacing.get() * (getSublines().size) + 2 * paddingSize.get()

    override fun getTranslation(): Translation = tiwylaWidgetTranslation

    override fun getDescription(): Translation = tiwylaWidgetDescription

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
        list.add(InfoTextRenderable(healthInfoText.getTranslatedString(), 0xFFAAAAAA.toInt(), true))
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
    }

    fun loadBlockInformation(first: String, second: String? = null): Information<BlockData> {
        return Information(
            first = blockInformation.firstOrNull { it.id == first },
            second = blockInformation.firstOrNull { it.id == second }
        )
    }

    fun loadEntityInformation(first: String, second: String? = null): Information<Entity> {
        return Information(
            first = entityInformation.firstOrNull { it.id == first },
            second = entityInformation.firstOrNull { it.id == second }
        )
    }

    data class Information<T>(
        val first: Line<T>?,
        val second: Line<T>?
    ) {
        data class Line<T>(val fn: (data: T) -> Text?, val id: String, val priority: Int) {
            val translation = Translation("widget.tiwyla_widget.information.$id", `snake_toWord With Spaces`(id))

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
        }, "tool", 0),
        Information.Line({ data ->
            if (data.state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) return@Line miningLevel(diamondLevelText.getTranslatedString())
            if (data.state.isIn(BlockTags.NEEDS_IRON_TOOL)) return@Line miningLevel(ironLevelText.getTranslatedString())
            if (data.state.isIn(BlockTags.NEEDS_STONE_TOOL)) return@Line miningLevel(stoneLevelText.getTranslatedString())

            if (data.state.isToolRequired) return@Line miningLevel(woodLevelText.getTranslatedString())
            return@Line miningLevel(noneLevelText.getTranslatedString())
        }, "mining_level", 0),
        Information.Line({ data ->
            if (MinecraftClient.getInstance().world == null) return@Line secondsText(4.5)

            val player = MinecraftClient.getInstance().player ?: return@Line null

            if (data.state.calcBlockBreakingDelta(player, MinecraftClient.getInstance().world, data.blockPos) > 1) return@Line instantText()

            val secs = (1f / data.state.calcBlockBreakingDelta(player, MinecraftClient.getInstance().world, data.blockPos) * 5F).roundToInt() / 100F

            if (secs > (3600 * 24)) return@Line daysText((secs / 36 / 24).roundToInt() / 100F)
            if (secs > 3600) return@Line hoursText((secs / 36).roundToInt() / 100F)
            if (secs > 60) return@Line minutesText((secs / 6 * 10).roundToInt() / 100F)
            return@Line secondsText((secs * 100).roundToInt() / 100F)
        }, "break_time", 0),
        Information.Line({ _ ->
            val s = (((MinecraftClient.getInstance().interactionManager as BreakingProgressAccessor?)?.getCurrentBreakingProgress() ?: 0f) * 1000)
            if (s == 0F) {
                return@Line null
            }
            return@Line progressText(round(s) / 10f)
        }, "progress", 2),
        Information.Line({ data ->
            val property = blockBlockStateMap[data.state.block] ?: return@Line null
            return@Line Text.literal("${snake_toCamelCase(property.name)}: ${data.state.get(property)}")
        }, "block_property", 1)
    )

    val entityInformation = listOf<Information.Line<Entity>>(
        Information.Line({ entity ->
            return@Line Text.literal(Registries.ENTITY_TYPE.getEntry(entity.type).key.get().value.toString())
        }, "entity_id", 0),
        Information.Line({ entity ->
            return@Line if (MinecraftClient.getInstance().isInSingleplayer) entity.entity?.let {
                convertToHearths(
                    it.health.toDouble(),
                    it.maxHealth.toDouble(),
                    it.absorptionAmount.toDouble()
                )
            } else null
        }, "health", 1),
        Information.Line({ entity ->
            return@Line provideEntityInfo(entity)?.let { Text.literal(it) }
        }, "special_entity_info", 2)
    )

    fun convertToHearths(_health: Double, _maxHealth: Double, _absorption: Double): Text {
        var health = _health
        var maxHealth = _maxHealth
        var absorbtion = _absorption
        try {
            maxHealth = roundUpAndHalf(maxHealth)
            health = ((health * 10).toInt().toDouble()) / 10f
            absorbtion = ((absorbtion * 10).toInt().toDouble()) / 10f
            if (maxHealth > 13.0) {
                return Text.literal((health.toString() + " / " + maxHealth * 2 + " HP"))
            }
            health = roundUpAndHalf(health)
            absorbtion = roundUpAndHalf(absorbtion)
            val isHalf = health != health.toInt().toDouble()
            val isAbso = absorbtion != absorbtion.toInt().toDouble()
            val isMaxHalf = maxHealth != (((maxHealth * 2).toInt().toDouble()) / 2).toInt().toDouble()
            val maxhealthleft = (maxHealth - ((health.toInt()) + (if (isHalf) 1 else 0)) + (if (isMaxHalf) 1 else 0)).toInt()
            return Text.literal("❤".repeat(health.toInt())).setStyle(Style.EMPTY.withColor(0xFF0000))
                .append(Text.literal(if (isHalf) "\uE0aa" else "").setStyle(Style.EMPTY.withFont(heartsFont).withColor(0xFFFFFF)))
                .append(Text.literal("❤".repeat(maxhealthleft)).setStyle(Style.EMPTY.withColor(0xFFFFFF)))
                .append(Text.literal("❤".repeat(absorbtion.toInt())).setStyle(Style.EMPTY.withColor(0xFFFF00)))
                .append(Text.literal(if (isAbso) "\uE0ab" else "").setStyle(Style.EMPTY.withFont(heartsFont).withColor(0xFFFFFF)))
        } catch (_: Exception) {
            return Text.of("")
        }
    }

    fun roundUpAndHalf(i: Double): Double {
        return ceil(i) / 2.0
    }

    fun <T : Entity> provideEntityInfo(entity: T): String? {
        @Suppress("UNCHECKED_CAST")
        val provider = entityInfoProviders.firstOrNull { it.clazz.isInstance(entity) } as? EntityInfoProvider<T> ?: return null
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
        return str.split("_".toRegex()).filter { it.isNotEmpty() }.joinToString("") {
            it.replaceFirstChar(Char::uppercaseChar)
        }
    }

    override fun onMinecraftClientInitFinished() {
        val resources = MinecraftClient.getInstance().resourceManager.findAllResources(
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
                            blockBlockStateMap[b] = b.stateManager.properties.firstOrNull { a -> a.name == propertyId } ?: run {
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

    data class EntityInfoProvider<T : Entity>(val clazz: Class<T>, val fn: (entity: T) -> String?)
}