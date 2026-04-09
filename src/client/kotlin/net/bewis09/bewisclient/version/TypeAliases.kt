// @VersionReplacement

package net.bewis09.bewisclient.version

// @[26.1] Minecraft.GameLoadCookie @[] GameLoadCookie
typealias GameLoadCookie = net.minecraft.client./*[@]*/Minecraft.GameLoadCookie/*[!@]*/

// @[1.21.10] ResourceLocation @[] Identifier
typealias Identifier = net.minecraft.resources./*[@]*/ResourceLocation/*[!@]*/

// @[26.1] Gui @[] Hud
typealias Hud = net.minecraft.client.gui./*[@]*/Gui/*[!@]*/

// @[1.21.10] . @[] .util.
typealias Util = net.minecraft/*[@]*/./*[!@]*/Util

// @[1.21.11] ClientCommandManager @[] ClientCommands
typealias ClientCommandManager = net.fabricmc.fabric.api.client.command.v2./*[@]*/ClientCommandManager/*[!@]*/

// @[1.21.11] GuiGraphics @[] GuiGraphicsExtractor
typealias GuiGraphics = net.minecraft.client.gui./*[@]*/GuiGraphics/*[!@]*/

// @[1.21.11] FabricDataOutput @[] FabricPackOutput
typealias FabricDataOutput = net.fabricmc.fabric.api.datagen.v1./*[@]*/FabricDataOutput/*[!@]*/

// @[1.21.11] TooltipComponentCallback @[] ClientTooltipComponentCallback
typealias TooltipComponentCallback = net.fabricmc.fabric.api.client.rendering.v1./*[@]*/TooltipComponentCallback/*[!@]*/

// @[1.21.1] MetadataSectionSerializer @[] MetadataSectionType
typealias IndependentResourceMetadataSerializer<T> = net.minecraft.server.packs.metadata./*[@]*/MetadataSectionSerializer/*[!@]*/<T>