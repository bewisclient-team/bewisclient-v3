package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.bewis09.bewisclient.util.UtilKt.createIdentifier;

@Mixin(PauseScreen.class)
public class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void bewisclient$init(CallbackInfo ci) {
        if (OptionsMenuSettings.INSTANCE.getButtonInGameScreen().get()) addRenderableWidget(new WorkingTexturedButtonWidget(this.width / 2 + 106, this.height / 4 + 56, 20, 20, createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"), createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png"), (_) -> this.minecraft.setScreen(new RenderableScreen(new OptionScreen(1f)))));
    }
}
