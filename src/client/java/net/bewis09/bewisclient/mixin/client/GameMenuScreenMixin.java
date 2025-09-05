package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.drawable.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {
    @Unique
    ButtonTextures buttonTextures = new ButtonTextures(Identifier.of("bewisclient", "textures/gui/sprites/options_button.png"), Identifier.of("bewisclient", "textures/gui/sprites/options_button_pressed.png"));

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void bewisclient$init(CallbackInfo ci) {
        if (OptionsMenuSettings.INSTANCE.getButtonInGameScreen().get()) addDrawableChild(new WorkingTexturedButtonWidget(this.width / 2 + 106, this.height / 4 + 56, 20, 20, buttonTextures, (b) -> {
            assert this.client != null;
            this.client.setScreen(new RenderableScreen(new OptionScreen(1f)));
        }));
    }
}
