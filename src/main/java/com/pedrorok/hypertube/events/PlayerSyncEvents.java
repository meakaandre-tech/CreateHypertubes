package com.pedrorok.hypertube.events;

import com.pedrorok.hypertube.core.travel.TravelManager;
import com.pedrorok.hypertube.network.NetworkHandler;
import com.pedrorok.hypertube.network.packets.SyncPersistentDataPacket;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author Rok, Pedro Lucas nmm. Created on 11/06/2025
 * @project Create Hypertube
 */
public class PlayerSyncEvents {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            syncAllStatesToPlayer(player);
            syncPlayerStateToAll(player, false);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> TravelManager.finishTravel(handler.getPlayer()));
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> {
            syncAllStatesToPlayer(player);
            syncPlayerStateToAll(player, false);
        });
    }

    private static void syncAllStatesToPlayer(ServerPlayer targetPlayer) {
        for (ServerPlayer otherPlayer : targetPlayer.level().getServer().getPlayerList().getPlayers()) {
            if (otherPlayer == targetPlayer || !TravelManager.hasHyperTubeData(otherPlayer)) continue;
            NetworkHandler.sendToPlayer(targetPlayer, SyncPersistentDataPacket.create(otherPlayer));
        }
    }

    public static void syncPlayerStateToAll(LivingEntity sourcePlayer, boolean force) {
        if (!TravelManager.hasHyperTubeData(sourcePlayer) && !force) return;
        NetworkHandler.sendToPlayersTrackingEntity(sourcePlayer, SyncPersistentDataPacket.create(sourcePlayer));
    }
}
