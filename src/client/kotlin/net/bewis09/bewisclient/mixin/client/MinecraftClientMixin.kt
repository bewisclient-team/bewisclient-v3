package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.version.GameLoadCookie
import net.bewis09.bewisclient.mixin.EventEntrypointMixin.onMinecraftClientInitFinished
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.EventEntrypoint.Companion.onAllEventEntrypoints
import net.minecraft.client.Minecraft
import net.minecraft.client.main.GameConfig
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
class MinecraftClientMixin {
    @Shadow
    @Final
    private val resourceManager: ReloadableResourceManager? = null

    @Inject(at = [At("HEAD")], method = ["onGameLoadFinished"])
    private fun onInitFinished(gameLoadCookie: GameLoadCookie?, ci: CallbackInfo?) {
        onMinecraftClientInitFinished()
    }

    @Inject(method = ["<init>"], at = [At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V")])
    fun registerResourceReloaders(args: GameConfig?, ci: CallbackInfo?) {
        (this.resourceManager)!!.registerReloadListener(ResourceManagerReloadListener { _ ->
            onAllEventEntrypoints(EventEntrypoint::onResourcesReloaded)
        })
    }
}