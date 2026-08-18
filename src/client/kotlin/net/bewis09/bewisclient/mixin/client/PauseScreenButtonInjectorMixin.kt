package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.getModrinthVersion
import net.bewis09.bewisclient.drawable.minecraft.TexturedButtonWidget
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.features.sidebar.General.buttonInGameScreen
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.minecraft.client.gui.screens.PauseScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PauseScreen::class)
class PauseScreenButtonInjectorMixin(title: Component) : Screen(title), ClientInterface {
    @Inject(method = ["init"], at = [At("HEAD")])
    private fun bewisclientInit(ci: CallbackInfo?) {
        if (buttonInGameScreen.get() && getModrinthVersion() < "26.2") addRenderableWidget(
            TexturedButtonWidget(
                this.width / 2 + 106,
                this.height / 4 + 57,
                20,
                20,
                createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"),
                createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png")
            ) { setRenderableScreen(OptionScreen.getOrCreateInstance(1f)) }
        )
    }
}