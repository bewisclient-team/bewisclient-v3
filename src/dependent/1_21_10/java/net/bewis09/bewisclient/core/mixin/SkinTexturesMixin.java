package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.cosmetics.Cosmetic;
import net.bewis09.bewisclient.cosmetics.CosmeticLoader;
import net.bewis09.bewisclient.cosmetics.CosmeticType;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class SkinTexturesMixin {
    @Shadow
    public abstract int hashCode();

    @Inject(method = "cape", at = @At("HEAD"), cancellable = true)
    private void bewisclient$cape(CallbackInfoReturnable<ClientAsset.Texture> cir) {
        PlayerInfo playerListEntry = CosmeticLoader.INSTANCE.getEntityBySkinTextures(this.hashCode());
        if (playerListEntry == null) return;
        Cosmetic cosmetic = CosmeticLoader.INSTANCE.getCosmeticForPlayer(playerListEntry.getProfile(), CosmeticType.CAPE);
        if (cosmetic != null) {
            cir.setReturnValue(new ClientAsset.Texture() {
                @Override
                public @NotNull ResourceLocation texturePath() {
                    return cosmetic.getIdentifier();
                }

                @Override
                public @NotNull ResourceLocation id() {
                    return cosmetic.getIdentifier();
                }
            });
        }
    }
}
