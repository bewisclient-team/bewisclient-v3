package net.bewis09.bewisclient.core.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(TransferableSelectionList.class)
public abstract class PackListWidgetMixin extends ObjectSelectionList<TransferableSelectionList.PackEntry> {
    @Shadow
    @Final
    private Component title;

    public PackListWidgetMixin(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @Override
    public void updateSize(int width, HeaderAndFooterLayout layout) {
        this.updateSizeAndPosition(width, (!PackAdderSettings.INSTANCE.isEnabled() || !Objects.equals(title.getString(), Component.translatable("pack.available.title").getString())) ? layout.getContentHeight() : layout.getContentHeight() - 20, layout.getHeaderHeight());
    }
}
