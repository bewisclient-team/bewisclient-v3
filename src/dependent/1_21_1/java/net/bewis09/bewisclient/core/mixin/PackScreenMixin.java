package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.drawable.Translations;
import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget;
import net.bewis09.bewisclient.impl.pack.Modrinth;
import net.bewis09.bewisclient.impl.pack.PackListScreen;
import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings;
import net.bewis09.bewisclient.screen.RenderableScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(PackScreen.class)
public class PackScreenMixin extends Screen {
    @Shadow
    @Final
    private Path file;
    @Unique
    ButtonWidget addResourcePackButton;

    @Unique
    Identifier BUTTON_TEXTURE = Identifier.of("bewisclient", "textures/gui/sprites/pack_screen_button_texture.png");

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void bewisclient$init(CallbackInfo ci) {
        if (!PackAdderSettings.INSTANCE.isEnabled()) return;

        addResourcePackButton = addDrawableChild(new WorkingTexturedButtonWidget(width / 2 - 215, height - 49, 200, 18, BUTTON_TEXTURE, BUTTON_TEXTURE, (b) -> MinecraftClient.getInstance().setScreen(new RenderableScreen(new PackListScreen(
                file.endsWith(Path.of("resourcepacks")) ? Modrinth.Type.RESOURCE_PACK : Modrinth.Type.DATA_PACK, this, this.file
        ))),(file.endsWith(Path.of("resourcepacks")) ? Translations.INSTANCE.getADD_RESOURCE_PACK().getTranslatedText() : Translations.INSTANCE.getADD_DATA_PACK().getTranslatedText()).append("...")));
    }

    @Inject(method = "initTabNavigation", at = @At("HEAD"))
    public void bewisclient$refreshWidgetPositions(CallbackInfo ci) {
        if (addResourcePackButton == null) return;
        addResourcePackButton.setPosition(width / 2 - 215, height - 49);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/pack/PackListWidget;<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/pack/PackScreen;IILnet/minecraft/text/Text;)V", ordinal = 0), index = 3)
    public int bewisclient$modifyPackListWidgetTitle(int par3) {
        return !PackAdderSettings.INSTANCE.isEnabled() ? par3 : par3 - 20;
    }
}
