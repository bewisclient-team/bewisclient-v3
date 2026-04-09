package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.core.WorkingTexturedButtonWidget
import net.bewis09.bewisclient.core.setScreen
import net.bewis09.bewisclient.drawable.Translations.ADD_DATA_PACK
import net.bewis09.bewisclient.drawable.Translations.ADD_RESOURCE_PACK
import net.bewis09.bewisclient.impl.pack.Modrinth
import net.bewis09.bewisclient.impl.pack.PackListScreen
import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.version.Identifier
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Button.OnPress
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.packs.PackSelectionScreen
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.ModifyArg
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.nio.file.Path

@Mixin(PackSelectionScreen::class)
class PackSelectionScreenMixin(title: Component) : Screen(title) {
    @Shadow
    @Final
    private val packDir: Path? = null

    @Unique
    var addResourcePackButton: Button? = null

    @Unique
    var buttonTexture: Identifier = createIdentifier("bewisclient", "textures/gui/sprites/pack_screen_button_texture.png")

    @Inject(method = ["init"], at = [At("RETURN")])
    fun bewisclientInit(ci: CallbackInfo) {
        if (!PackAdderSettings.isEnabled()) return

        addResourcePackButton = addRenderableWidget(WorkingTexturedButtonWidget(width / 2 - 215, height - 49, 200, 18, buttonTexture, buttonTexture, OnPress { b: Button? ->
            setScreen(
                RenderableScreen(
                    PackListScreen(
                        if (packDir!!.endsWith(Path.of("resourcepacks"))) Modrinth.Type.RESOURCE_PACK else Modrinth.Type.DATA_PACK, this, this.packDir
                    )
                )
            )
        }, (if (packDir!!.endsWith(Path.of("resourcepacks"))) ADD_RESOURCE_PACK.getTranslatedText() else ADD_DATA_PACK.getTranslatedText()).append("...")))
    }

    @Inject(method = ["repositionElements"], at = [At("HEAD")])
    fun bewisclientRefreshWidgetPositions(ci: CallbackInfo) {
        if (addResourcePackButton == null) return
        addResourcePackButton!!.setPosition(width / 2 - 215, height - 49)
    }

    @ModifyArg(method = ["init"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/packs/TransferableSelectionList;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/packs/PackSelectionScreen;IILnet/minecraft/network/chat/Component;)V", ordinal = 0), index = 3)
    fun bewisclientModifyPackListWidgetTitle(par3: Int): Int {
        return if (!PackAdderSettings.isEnabled()) par3 else par3 - 20
    }
}