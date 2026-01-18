package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 1))
    private ButtonWidget.Builder bewisclient$init(ButtonWidget.Builder instance, int x, int y, int width, int height) {
        if (OptionsMenuSettings.INSTANCE.getButtonInTitleScreen().get()) this.addDrawableChild(new WorkingTexturedButtonWidget(x + width + 4, y, 20, 20, Identifier.of("bewisclient", "textures/gui/sprites/options_button.png"), Identifier.of("bewisclient", "textures/gui/sprites/options_button_pressed.png"), (b) -> {
            assert this.client != null;
            this.client.setScreen(new RenderableScreen(new OptionScreen()));
        }));
        return instance.dimensions(x, y, width, height);
    }
}
