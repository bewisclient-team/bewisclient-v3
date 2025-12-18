package net.bewis09.bewisclient.util.logic

import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface BewisclientInterface : BewisclientLogger, FileLogic, InGameLogic, DrawingLogic, WebLogic {
    companion object {
        private val logger = LoggerFactory.getLogger("Bewisclient")
    }

    override val logger: Logger
        get() = BewisclientInterface.logger

    val util: UtilLogic
        get() = UtilLogic

    val client: MinecraftClient
        get() = MinecraftClient.getInstance()

    fun playClickSound() {
        client.soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }
}