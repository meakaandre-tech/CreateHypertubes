package com.pedrorok.hypertube.mixin.core;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.hypertube.client.TravellerRenderData;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Lays entities down along the tube while they travel. Replaces the RenderLivingEvent hooks.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void createHypertube$extractTravelPose(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        FabricRenderState data = (FabricRenderState) state;
        Consumer<PoseStack> pose = TravellerRenderData.poseFor(entity);
        if (pose != null || data.getData(TravellerRenderData.POSE) != null) {
            data.setData(TravellerRenderData.POSE, pose);
        }
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"))
    private void createHypertube$applyTravelPose(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        Consumer<PoseStack> pose = ((FabricRenderState) state).getData(TravellerRenderData.POSE);
        if (pose == null) return;
        poseStack.pushPose();
        pose.accept(poseStack);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("RETURN"))
    private void createHypertube$popTravelPose(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        if (((FabricRenderState) state).getData(TravellerRenderData.POSE) == null) return;
        poseStack.popPose();
    }
}
