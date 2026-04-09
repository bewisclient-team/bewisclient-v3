package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
