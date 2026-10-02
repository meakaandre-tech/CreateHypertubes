package com.pedrorok.hypertube.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import com.pedrorok.hypertube.core.travel.TravellerEntity;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Carries the "this entity is inside a tube" pose from the entity to its render state, since renderers
 * no longer see the entity itself while drawing.
 */
public final class TravellerRenderData {

    public static final RenderStateDataKey<Consumer<PoseStack>> POSE = RenderStateDataKey.create(() -> "create_hypertube:traveller_pose");

    private TravellerRenderData() {
    }

    public static boolean isTravelling(LivingEntity entity) {
        return PersistentData.get(entity).getBooleanOr(TravelConstants.TRAVEL_TAG, false);
    }

    public static @Nullable Consumer<PoseStack> poseFor(LivingEntity entity) {
        if (!isTravelling(entity)) return null;
        TravellerEntity traveller = TravelConstants.Client.ENTITIES_RENDER.get(entity.getType());
        if (traveller == null) traveller = TravellerEntity.ofAny(0.5f);
        return traveller.renderEntityOnTube().apply(entity);
    }
}
