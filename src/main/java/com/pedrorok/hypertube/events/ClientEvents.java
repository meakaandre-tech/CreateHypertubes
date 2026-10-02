package com.pedrorok.hypertube.events;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.client.TubePathOutline;
import com.pedrorok.hypertube.core.escape.TubeEscapeHandler;
import com.pedrorok.hypertube.core.placement.TubePlacement;
import com.pedrorok.hypertube.core.sound.TubeSoundManager;
import com.pedrorok.hypertube.core.travel.client.ClientTravelPathMover;
import com.pedrorok.hypertube.core.travel.client.ClientTravelPathRender;
import com.pedrorok.hypertube.utils.TubePulseRenderer;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/04/2025
 * @project Create Hypertube
 */
public class ClientEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> onTick(true));
        ClientTickEvents.END_CLIENT_TICK.register(client -> onTick(false));

        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(TubePathOutline::renderTubeOutline);
        LevelRenderEvents.COLLECT_SUBMITS.register(ClientEvents::onRenderWorld);

        HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, HypertubeMod.of("travel_direction"),
                (graphics, deltaTracker) -> ClientTravelPathRender.renderOverlay(graphics, AnimationTickHolder.getPartialTicksUI(deltaTracker)));
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, HypertubeMod.of("escape_progress"),
                TubeEscapeHandler::onRenderGuiOverlay);
    }

    private static void onTick(boolean isPreEvent) {
        if (!isGameActive()) return;
        if (isPreEvent) {
            TubeSoundManager.tickClientPlayerSounds();
            TubeEscapeHandler.onClientTick();
            return;
        }
        TubePlacement.clientTick();
        ClientTravelPathMover.onClientTick();
    }

    private static void onRenderWorld(LevelRenderContext context) {
        if (!isGameActive()) return;
        Vec3 camera = context.levelState().cameraRenderState.pos;
        float lineWidth = context.gameRenderer().gameRenderState().windowRenderState.appropriateLineWidth;

        TubePlacement.drawCustomBlockSelection(context.poseStack(), context.submitNodeCollector(), camera, lineWidth);
        TubePulseRenderer.onRenderLevelStage(context.poseStack(), context.submitNodeCollector(), camera, lineWidth);
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
