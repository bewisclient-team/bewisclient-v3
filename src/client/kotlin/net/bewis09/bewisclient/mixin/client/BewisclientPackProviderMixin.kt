package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.game.BewisclientResourcePack.metadata
import net.bewis09.bewisclient.game.BewisclientResourcePack.packInfo
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackSelectionConfig
import net.minecraft.server.packs.repository.FolderRepositorySource
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Consumer

@Mixin(FolderRepositorySource::class)
class BewisclientPackProviderMixin {
    @Inject(method = ["loadPacks"], at = [At("HEAD")])
    private fun loadPacks(profileAdder: Consumer<Pack?>, ci: CallbackInfo?) {
        profileAdder.accept(
            Pack(
                packInfo,
                object : ResourcesSupplier {
                    override fun openPrimary(info: PackLocationInfo): PackResources = BewisclientResourcePack

                    override fun openFull(info: PackLocationInfo, metadata: Pack.Metadata): PackResources = openPrimary(info)
                },
                metadata,
                PackSelectionConfig(true, Pack.Position.TOP, true)
            )
        )
    }
}