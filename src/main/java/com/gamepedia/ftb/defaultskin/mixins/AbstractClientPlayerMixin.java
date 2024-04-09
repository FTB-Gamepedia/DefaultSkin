package com.gamepedia.ftb.defaultskin.mixins;

import com.gamepedia.ftb.defaultskin.DefaultSkinMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile profile) {
        super(level, pos, yRot, profile);
    }

    @Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
    private void defaultskin$getTextureLocation(CallbackInfoReturnable<ResourceLocation> info) {
        if (DefaultSkinMod.isEnabled()) {
            info.setReturnValue(DefaultPlayerSkin.getDefaultSkin(getUUID()));
        }
    }
}
