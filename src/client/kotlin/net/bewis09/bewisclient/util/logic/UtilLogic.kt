package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.core.Identifier
import net.bewis09.bewisclient.core.getScreen
import net.bewis09.bewisclient.screen.RenderableScreen
import net.minecraft.server.packs.resources.Resource
import java.util.function.Predicate

object UtilLogic: BewisclientInterface {
    val scaleFactor
        get() = client.window.guiScale.toFloat().toInt()

    fun isInWorld() = client.level != null

    fun getCurrentRenderableScreen() = (getScreen() as? RenderableScreen)

    fun findAllResources(path: String, filter: Predicate<Identifier>): Map<Identifier, List<Resource>> {
        return client.resourceManager.listResourceStacks(path) { filter.test(it) }.mapKeys { it.key }
    }
}