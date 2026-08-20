package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.logic.BewisclientLogger
import net.bewis09.bewisclient.common.logic.FileLogic
import net.bewis09.bewisclient.common.logic.WebLogic
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.minecraft.RenderableScreen
import net.bewis09.bewisclient.version.getScreen
import net.bewis09.bewisclient.version.setScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.server.packs.resources.Resource
import net.minecraft.sounds.SoundEvents
import java.util.function.Predicate

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface ClientInterface : BewisclientLogger, FileLogic, InGameLogic, DrawingLogic, WebLogic {
    val screenWidth
        get() = client.window?.guiScaledWidth ?: 1000

    val screenHeight
        get() = client.window?.guiScaledHeight ?: 1000

    val client: Minecraft
        get() = Minecraft.getInstance()

    fun playClickSound() {
        client.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }

    val scaleFactor
        get() = client.window.guiScale.toFloat().toInt()

    fun isInWorld() = client.level != null

    fun getCurrentRenderableScreen() = (getScreen() as? RenderableScreen)

    fun setRenderableScreen(screen: Renderable) = setScreen(RenderableScreen(screen))

    fun findAllResources(path: String, filter: Predicate<Identifier>): Map<Identifier, List<Resource>> {
        return client.resourceManager.listResourceStacks(path) { filter.test(it) }.mapKeys { it.key }
    }
}