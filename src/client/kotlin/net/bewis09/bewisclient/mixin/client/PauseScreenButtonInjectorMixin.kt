package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.drawable.TexturedButtonWidget
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings.buttonInGameScreen
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.version.setScreen
import net.minecraft.client.gui.screens.PauseScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PauseScreen::class)
class PauseScreenButtonInjectorMixin(title: Component) : Screen(title) {
    @Inject(method = ["init"], at = [At("HEAD")])
    private fun bewisclientInit(ci: CallbackInfo?) {
        if (buttonInGameScreen.get()) addRenderableWidget(
            TexturedButtonWidget(
                this.width / 2 + 106,
                this.height / 4 + 56,
                20,
                20,
                createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"),
                createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png")
            ) { setScreen(RenderableScreen(OptionScreen(1f))) }
        )
    }
}