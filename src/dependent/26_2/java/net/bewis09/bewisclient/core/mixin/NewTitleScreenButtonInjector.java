package net.bewis09.bewisclient.core.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.bewis09.bewisclient.common.UtilKt;
import net.bewis09.bewisclient.drawable.minecraft.TexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.features.sidebar.General;
import net.bewis09.bewisclient.util.Bewisclient;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class NewTitleScreenButtonInjector extends Screen {
    @Shadow
    protected abstract int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth);

    protected NewTitleScreenButtonInjector(Component title) {
        super(title);
    }

    @Definition(id = "numberOfButtons", local = @Local(type = int.class, name = "numberOfButtons"))
    @Expression("numberOfButtons = ?")
    @Inject(method = "init", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    private void adjustAmountOfIconButtons(CallbackInfo ci, @Local(name = "numberOfButtons") LocalIntRef numberOfButtons) {
        if (General.INSTANCE.getButtonInTitleScreen().get()) {
            numberOfButtons.set(numberOfButtons.get() + 1);
        }
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;getHorizontalPosition(III)I"), index = 1)
    private int buttonNumber(int original) {
        return original + 1;
    }


    @Definition(id = "width", field = "Lnet/minecraft/client/gui/screens/TitleScreen;width:I")
    @Expression("this.width / 2 - 100")
    @Inject(method = "init", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private void addModMenuIconWidget(CallbackInfo ci, @Local(name = "currentButton") LocalIntRef currentButton, @Local(name = "topPos") int topPos, @Local(name = "numberOfButtons") int numberOfButtons, @Share("addModMenuIconWidget") LocalBooleanRef addModMenuIconWidget) {
        if (!General.INSTANCE.getButtonInTitleScreen().get()) return;
        currentButton.set(currentButton.get()+1);
        Screen screen = this;
        var button = new TexturedButtonWidget(
                this.getHorizontalPosition(currentButton.get(), numberOfButtons, 20),
                topPos,
                20,
                20,
                UtilKt.createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"),
                UtilKt.createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png"),
                (b) -> Bewisclient.INSTANCE.setRenderableScreen(OptionScreen.Companion.getOrCreateInstance(1f, 0f))
        );

        Screens.getWidgets(screen).add(button);

        button.setTooltip(Tooltip.create(Component.literal("Bewisclient")));
    }
}
