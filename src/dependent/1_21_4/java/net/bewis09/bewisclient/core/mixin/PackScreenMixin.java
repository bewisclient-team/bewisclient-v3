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
import net.minecraft.resources.ResourceLocation;
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
    @Shadow
    @Final
    private Path packDir;
    @Unique
    Button addResourcePackButton;

    @Unique
    ResourceLocation BUTTON_TEXTURE = createIdentifier("bewisclient", "textures/gui/sprites/pack_screen_button_texture.png");

    protected PackScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void bewisclient$init(CallbackInfo ci) {
        if (!PackAdderSettings.INSTANCE.isEnabled()) return;

        addResourcePackButton = addRenderableWidget(new WorkingTexturedButtonWidget(width / 2 - 215, height - 49, 200, 18, BUTTON_TEXTURE, BUTTON_TEXTURE, (_) -> Minecraft.getInstance().setScreen(new RenderableScreen(new PackListScreen(
                packDir.endsWith(Path.of("resourcepacks")) ? Modrinth.Type.RESOURCE_PACK : Modrinth.Type.DATA_PACK, this, this.packDir
        ))),(packDir.endsWith(Path.of("resourcepacks")) ? Translations.INSTANCE.getADD_RESOURCE_PACK().getTranslatedText() : Translations.INSTANCE.getADD_DATA_PACK().getTranslatedText()).append("...")));
    }

    @Inject(method = "repositionElements", at = @At("HEAD"))
    public void bewisclient$refreshWidgetPositions(CallbackInfo ci) {
        if (addResourcePackButton == null) return;
        addResourcePackButton.setPosition(width / 2 - 215, height - 49);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/packs/TransferableSelectionList;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/packs/PackSelectionScreen;IILnet/minecraft/network/chat/Component;)V", ordinal = 0), index = 3)
    public int bewisclient$modifyPackListWidgetTitle(int par3) {
        return !PackAdderSettings.INSTANCE.isEnabled() ? par3 : par3 - 20;
    }
}
