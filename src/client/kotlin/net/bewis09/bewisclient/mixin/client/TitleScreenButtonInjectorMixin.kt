package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.drawable.TexturedButtonWidget
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings.buttonInTitleScreen
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.version.setScreen
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Button.OnPress
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(TitleScreen::class)
class TitleScreenButtonInjectorMixin(title: Component) : Screen(title) {
    @Redirect(method = ["createNormalMenuOptions"], at = At(value = "INVOKE", target = $$"Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1))
    private fun bewisclientInit(instance: Button.Builder, x: Int, y: Int, width: Int, height: Int): Button.Builder {
        if (buttonInTitleScreen.get()) this.addRenderableWidget(TexturedButtonWidget(x + width + 4, y, 20, 20, createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"), createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png"), OnPress { b: Button? -> setScreen(RenderableScreen(OptionScreen())) }))
        return instance.bounds(x, y, width, height)
    }
}