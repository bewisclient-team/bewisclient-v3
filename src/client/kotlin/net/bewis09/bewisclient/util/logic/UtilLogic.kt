package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.screen.RenderableScreen
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import java.util.function.Predicate

object UtilLogic: BewisclientInterface {
    val width
        get() = client.window.guiScaledWidth

    val height
        get() = client.window.guiScaledHeight

    val scaleFactor
        get() = client.window.guiScale.toFloat().toInt()

    fun isInWorld() = client.level != null

    fun getCurrentRenderableScreen() = (client.screen as? RenderableScreen)

    fun findAllResources(path: String, filter: Predicate<Identifier>): Map<Identifier, List<Resource>> {
        return client.resourceManager.listResourceStacks(path) { filter.test(it) }.mapKeys { it.key }
    }
}