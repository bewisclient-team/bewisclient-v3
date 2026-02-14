package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.game.BewisclientResourcePack;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(FileResourcePackProvider.class)
public class FileResourcePackProviderMixin {
    @Inject(method = "register", at = @At("HEAD"))
    private void register(Consumer<ResourcePackProfile> profileAdder, CallbackInfo ci) {
        profileAdder.accept(new ResourcePackProfile(BewisclientResourcePack.INSTANCE.getInfo(),
                new ResourcePackProfile.PackFactory() {
                    @Override
                    public ResourcePack open(ResourcePackInfo info) {
                        return BewisclientResourcePack.INSTANCE;
                    }

                    @Override
                    public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                        return open(info);
                    }
                },
                BewisclientResourcePack.INSTANCE.getMetadata(),
                new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.TOP, true)
        ));
    }
}
