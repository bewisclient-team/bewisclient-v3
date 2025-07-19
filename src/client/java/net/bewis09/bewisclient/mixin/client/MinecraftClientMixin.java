package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.mixin.EntrypointMixin;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"), method = "run")
	private void init(CallbackInfo info) {
		EntrypointMixin.INSTANCE.onMinecraftClientInitStarted();
	}

	@Inject(at = @At("HEAD"), method = "onInitFinished")
	private void onInitFinished(MinecraftClient.LoadingContext loadingContext, CallbackInfoReturnable<Runnable> cir) {
		EntrypointMixin.INSTANCE.onMinecraftClientInitFinished();
	}
}