package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.drawable.Translations;
import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.impl.pack.Modrinth;
import net.bewis09.bewisclient.impl.pack.PackListScreen;
import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

import static net.bewis09.bewisclient.util.UtilKt.createIdentifier;

@Mixin(PackSelectionScreen.class)
public class PackScreenMixin extends Screen {
    protected PackScreenMixin(Component title) {
        super(title);
    }

    @ModifyArg(method = "repositionElements", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/packs/TransferableSelectionList;updateSizeAndPosition(IIII)V", ordinal = 0), index = 1)
    public int bewisclient$modifyPackListWidgetPosition(int par1) {
        return !PackAdderSettings.INSTANCE.isEnabled() ? par1 : par1 - 20;
    }
}
