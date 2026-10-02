package com.pedrorok.hypertube.events;

import com.pedrorok.hypertube.core.escape.TubeEscapeHandler;
import com.pedrorok.hypertube.core.placement.TubePlacement;
import com.pedrorok.hypertube.core.sound.TubeSoundManager;
import com.pedrorok.hypertube.core.travel.client.ClientTravelPathMover;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/04/2025
 * @project Create Hypertube
 */
public class ClientEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> onTick(true));
        ClientTickEvents.END_CLIENT_TICK.register(client -> onTick(false));
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

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
