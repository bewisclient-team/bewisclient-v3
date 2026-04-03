package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.bewis09.bewisclient.util.UtilKt.createIdentifier;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Redirect(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1))
    private Button.Builder bewisclient$init(Button.Builder instance, int x, int y, int width, int height) {
        if (OptionsMenuSettings.INSTANCE.getButtonInTitleScreen().get()) this.addRenderableWidget(new WorkingTexturedButtonWidget(x + width + 4, y, 20, 20, createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"), createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png"), (b) -> this.minecraft.setScreen(new RenderableScreen(new OptionScreen()))));
        return instance.bounds(x, y, width, height);
    }
}
