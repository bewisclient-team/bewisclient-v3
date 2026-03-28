package net.bewis09.bewisclient.mixin.client;

import kotlin.Unit;
import net.bewis09.bewisclient.util.EventEntrypoint;
import net.bewis09.bewisclient.mixin.EventEntrypointMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Inject(at = @At("HEAD"), method = "onGameLoadFinished")
    private void onInitFinished(Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
        EventEntrypointMixin.INSTANCE.onMinecraftClientInitFinished();
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V"))
    public void registerResourceReloaders(GameConfig args, CallbackInfo ci) {
        (this.resourceManager).registerReloadListener((ResourceManagerReloadListener) _ -> EventEntrypoint.Companion.onAllEventEntrypoints((a) -> {
            a.onResourcesReloaded();
            return Unit.INSTANCE;
        }));
    }
}