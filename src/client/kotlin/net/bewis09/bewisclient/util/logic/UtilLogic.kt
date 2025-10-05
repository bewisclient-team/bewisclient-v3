package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.screen.RenderableScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.Resource
import net.minecraft.util.Identifier
import java.util.function.Predicate

object UtilLogic {
    val width
        get() = MinecraftClient.getInstance().window.scaledWidth

    val height
        get() = MinecraftClient.getInstance().window.scaledHeight

    val scaleFactor
        get() = MinecraftClient.getInstance().window.scaleFactor.toFloat().toInt()

    fun isInWorld() = MinecraftClient.getInstance().world != null

    fun getCurrentRenderableScreen() = (MinecraftClient.getInstance().currentScreen as? RenderableScreen)

    fun findAllResources(path: String, filter: Predicate<Identifier>): Map<Identifier, List<Resource>> {
        return MinecraftClient.getInstance().resourceManager.findAllResources(path) { filter.test(it) }.mapKeys { it.key }
    }
}