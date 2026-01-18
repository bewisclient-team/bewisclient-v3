package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(PackListWidget.class)
public abstract class PackListWidgetMixin extends AlwaysSelectedEntryListWidget<PackListWidget.ResourcePackEntry> {
    @Shadow
    @Final
    private Text title;

    public PackListWidgetMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @Override
    public void position(int width, ThreePartsLayoutWidget layout) {
        this.position(width, (!PackAdderSettings.INSTANCE.isEnabled() || !Objects.equals(title.getString(), Text.translatable("pack.available.title").getString())) ? layout.getContentHeight() : layout.getContentHeight() - 20, layout.getHeaderHeight());
    }
}
