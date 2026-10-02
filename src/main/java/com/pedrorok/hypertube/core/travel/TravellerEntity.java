package com.pedrorok.hypertube.core.travel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pedrorok.hypertube.core.travel.client.ClientTravelPathMover;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Rok, Pedro Lucas nmm. Created on 20/06/2025
 * @project Create Hypertube
 */
public record TravellerEntity(Function<LivingEntity, Consumer<PoseStack>> renderEntityOnTube) {

    public static TravellerEntity ofBiped(float translateY) {
        Function<LivingEntity, Consumer<PoseStack>> renderBiped = entity -> {
            float yaw = entity.getYRot();
            ClientTravelPathMover.PathData data = ClientTravelPathMover.getData(entity.getId());
            float pitch = data != null ? data.getPitch() : entity.getXRot();
            return poseStack -> {
            poseStack.translate(0, 0.2, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch + 90));
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.translate(0, translateY, 0);
            poseStack.scale(0.8f, 0.8f, 0.8f);
            };
        };
        return new TravellerEntity(renderBiped);
    }

    public static TravellerEntity ofFish(float size) {
        Function<LivingEntity, Consumer<PoseStack>> renderAnimal = entity -> {
            entity.setPose(Pose.SWIMMING);
            float yRot = entity.getYRot();
            float xRot = entity.getXRot();
            return poseStack -> {
            poseStack.translate(0, 0.1, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yRot + 90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90));
            poseStack.translate(0, -0.1, 0);
            poseStack.scale(size, size, size);
            };
        };
        return new TravellerEntity(renderAnimal);
    }

    public static TravellerEntity ofAny(float maxSize) {
        Function<LivingEntity, Consumer<PoseStack>> renderAny = entity -> {
            float size = Math.min(maxSize, entity.getBbWidth());
            float yRot = entity.getYRot();
            float xRot = entity.getXRot();
            return poseStack -> {
            poseStack.translate(0, 0.1, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.translate(0, -0.1, 0);
            poseStack.scale(size, size, size);
            };
        };
        return new TravellerEntity(renderAny);

    }
}
