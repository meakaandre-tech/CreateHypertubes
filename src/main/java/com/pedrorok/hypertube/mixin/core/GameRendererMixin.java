package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.core.travel.client.ClientTravelPathMover;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Per-frame hook, run before anything is read for rendering. Replaces RenderFrameEvent.Pre.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "extract(Lnet/minecraft/client/DeltaTracker;Z)V", at = @At("HEAD"))
    private void createHypertube$onFrame(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        ClientTravelPathMover.onRenderTick(deltaTracker);
    }
}
