// @VersionReplacement

package net.bewis09.bewisclient.version

// @[26.1] Minecraft.GameLoadCookie @[] GameLoadCookie
typealias GameLoadCookie = net.minecraft.client./*[@]*/GameLoadCookie/*[!@]*/

// @[1.21.10] ResourceLocation @[] Identifier
typealias Identifier = net.minecraft.resources./*[@]*/Identifier/*[!@]*/

// @[26.1] Gui @[] Hud
typealias Hud = net.minecraft.client.gui./*[@]*/Hud/*[!@]*/

// @[1.21.10] . @[] .util.
typealias Util = net.minecraft/*[@]*/.util./*[!@]*/Util

// @[1.21.11] ClientCommandManager @[] ClientCommands
typealias ClientCommandManager = net.fabricmc.fabric.api.client.command.v2./*[@]*/ClientCommands/*[!@]*/

// @[1.21.11] GuiGraphics @[] GuiGraphicsExtractor
typealias GuiGraphics = net.minecraft.client.gui./*[@]*/GuiGraphicsExtractor/*[!@]*/

// @[1.21.11] FabricDataOutput @[] FabricPackOutput
typealias FabricDataOutput = net.fabricmc.fabric.api.datagen.v1./*[@]*/FabricPackOutput/*[!@]*/

// @[1.21.11] TooltipComponentCallback @[] ClientTooltipComponentCallback
typealias TooltipComponentCallback = net.fabricmc.fabric.api.client.rendering.v1./*[@]*/ClientTooltipComponentCallback/*[!@]*/

// @[1.21.1] MetadataSectionSerializer @[] MetadataSectionType
typealias IndependentResourceMetadataSerializer<T> = net.minecraft.server.packs.metadata./*[@]*/MetadataSectionType/*[!@]*/<T>