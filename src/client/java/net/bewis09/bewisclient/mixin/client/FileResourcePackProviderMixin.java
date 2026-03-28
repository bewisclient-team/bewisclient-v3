package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.game.BewisclientResourcePack;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(FolderRepositorySource.class)
public class FileResourcePackProviderMixin {
    @Inject(method = "loadPacks", at = @At("HEAD"))
    private void loadPacks(Consumer<Pack> profileAdder, CallbackInfo ci) {
        profileAdder.accept(new Pack(BewisclientResourcePack.INSTANCE.getPackInfo(),
                new Pack.ResourcesSupplier() {
                    @Override
                    public @NotNull PackResources openPrimary(@NotNull PackLocationInfo info) {
                        return BewisclientResourcePack.INSTANCE;
                    }

                    @Override
                    public @NotNull PackResources openFull(@NotNull PackLocationInfo info, Pack.@NotNull Metadata metadata) {
                        return openPrimary(info);
                    }
                },
                BewisclientResourcePack.INSTANCE.getMetadata(),
                new PackSelectionConfig(true, Pack.Position.TOP, true)
        ));
    }
}
