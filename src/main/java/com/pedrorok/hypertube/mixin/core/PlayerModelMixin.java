package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.client.TravellerRenderData;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/05/2025
 * @project Create Hypertube
 */
@Mixin(value = HumanoidModel.class, priority = 1001)
public abstract class PlayerModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("RETURN"))
    private void createHypertube$onSetupAnim(HumanoidRenderState state, CallbackInfo ci) {
        if (((FabricRenderState) state).getData(TravellerRenderData.POSE) == null) return;

        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        // Sleeves, pants and jacket are children of these parts now, so they follow along.
        createHypertube$straighten(model.rightArm);
        createHypertube$straighten(model.leftArm);
        createHypertube$straighten(model.rightLeg);
        createHypertube$straighten(model.leftLeg);
        createHypertube$straighten(model.body);

        model.head.xRot = -1.2F;
        model.head.yRot = 0;
        model.head.zRot = 0;

        // Fixing issue #6
        model.hat.xRot = -1.2F;
        model.hat.yRot = 0;
        model.hat.zRot = 0;
    }

    private static void createHypertube$straighten(ModelPart part) {
        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;
    }
}
