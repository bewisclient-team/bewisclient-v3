package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.common.UtilKt;
import net.bewis09.bewisclient.drawable.minecraft.TexturedButtonWidget;
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen;
import net.bewis09.bewisclient.features.sidebar.General;
import net.bewis09.bewisclient.util.Bewisclient;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PauseScreen.class)
public class NewPauseScreenButtonInjectorMixin extends Screen {
    protected NewPauseScreenButtonInjectorMixin(Component title) {
        super(title);
    }

    @Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;ILnet/minecraft/client/gui/layouts/LayoutSettings;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 1))
    private <T extends LayoutElement> T addNewButton(GridLayout.RowHelper instance, T widget, int columnWidth, LayoutSettings layoutSettings) {
        if (widget instanceof LinearLayout linearLayout && General.INSTANCE.getButtonInGameScreen().get()) {
            TexturedButtonWidget button = new TexturedButtonWidget(
                    this.width / 2 + 106,
                    this.height / 4 + 56,
                    20,
                    20,
                    UtilKt.createIdentifier("bewisclient", "textures/gui/sprites/options_button.png"),
                    UtilKt.createIdentifier("bewisclient", "textures/gui/sprites/options_button_pressed.png"),
                    (b) -> Bewisclient.INSTANCE.setRenderableScreen(OptionScreen.Companion.getOrCreateInstance(1f, 0f))
            );

            button.setTooltip(Tooltip.create(Component.literal("Bewisclient")));

            linearLayout.addChild(button);
        }
        return instance.addChild(widget, columnWidth, layoutSettings);
    }
}
