package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.client.TravellerRenderData;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the cape while travelling (it used to be hidden on the player model directly).
 */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void createHypertube$hideCape(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        if (TravellerRenderData.isTravelling(entity)) {
            state.showCape = false;
        }
    }
}
