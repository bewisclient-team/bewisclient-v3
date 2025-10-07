package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.cosmetics.Cosmetic;
import net.bewis09.bewisclient.cosmetics.CosmeticLoader;
import net.bewis09.bewisclient.cosmetics.CosmeticType;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkinTextures.class)
public abstract class SkinTexturesMixin {
    @Shadow
    public abstract int hashCode();

    @Inject(method = "cape", at = @At("HEAD"), cancellable = true)
    private void bewisclient$cape(CallbackInfoReturnable<AssetInfo.TextureAsset> cir) {
        PlayerListEntry playerListEntry = CosmeticLoader.INSTANCE.getEntityBySkinTextures(this.hashCode());
        if (playerListEntry == null) return;
        Cosmetic cosmetic = CosmeticLoader.INSTANCE.getCosmeticForPlayer(playerListEntry, CosmeticType.CAPE);
        if (cosmetic != null) {
            cir.setReturnValue(new AssetInfo.TextureAsset() {
                @Override
                public Identifier texturePath() {
                    return cosmetic.getIdentifier();
                }

                @Override
                public Identifier id() {
                    return cosmetic.getIdentifier();
                }
            });
        }
    }
}
