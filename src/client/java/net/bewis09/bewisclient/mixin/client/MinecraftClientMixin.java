package net.bewis09.bewisclient.mixin.client;

import kotlin.Unit;
import net.bewis09.bewisclient.logic.EventEntrypoint;
import net.bewis09.bewisclient.mixin.EventEntrypointMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Inject(at = @At("HEAD"), method = "onInitFinished")
    private void onInitFinished(MinecraftClient.LoadingContext loadingContext, CallbackInfoReturnable<Runnable> cir) {
        EventEntrypointMixin.INSTANCE.onMinecraftClientInitFinished();
    }

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;registerReloader(Lnet/minecraft/resource/ResourceReloader;)V"))
    public void registerResourceReloaders(RunArgs args, CallbackInfo ci) {
        (this.resourceManager).registerReloader((SynchronousResourceReloader) manager -> EventEntrypoint.Companion.onAllEventEntrypoints((a) -> {
            a.onResourcesReloaded();
            return Unit.INSTANCE;
        }));
    }
}