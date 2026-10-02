package com.pedrorok.hypertube.network;

import com.pedrorok.hypertube.network.packets.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * @author Rok, Pedro Lucas nmm. Created on 10/06/2025
 * @project Create Hypertube
 */
public class NetworkHandler {

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(SyncPersistentDataPacket.TYPE, SyncPersistentDataPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(EntityTravelDirDataPacket.TYPE, EntityTravelDirDataPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MovePathPacket.TYPE, MovePathPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncEntityPosPacket.TYPE, SyncEntityPosPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SpeedChangePacket.TYPE, SpeedChangePacket.STREAM_CODEC);

        PayloadTypeRegistry.serverboundPlay().register(FinishPathPacket.TYPE, FinishPathPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ActionPointReachPacket.TYPE, ActionPointReachPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MoveDirectionPacket.TYPE, MoveDirectionPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(EscapeTubePacket.TYPE, EscapeTubePacket.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(FinishPathPacket.TYPE, (packet, ctx) -> FinishPathPacket.handle(packet, server(ctx)));
        ServerPlayNetworking.registerGlobalReceiver(ActionPointReachPacket.TYPE, (packet, ctx) -> ActionPointReachPacket.handle(packet, server(ctx)));
        ServerPlayNetworking.registerGlobalReceiver(MoveDirectionPacket.TYPE, (packet, ctx) -> MoveDirectionPacket.handle(packet, server(ctx)));
        ServerPlayNetworking.registerGlobalReceiver(EscapeTubePacket.TYPE, (packet, ctx) -> EscapeTubePacket.handle(packet, server(ctx)));
    }

    private static PayloadContext server(ServerPlayNetworking.Context ctx) {
        return new PayloadContext(ctx.player(), ctx.server()::execute);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload) {
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload) {
        sendToPlayersTrackingEntity(entity, payload);
        if (entity instanceof ServerPlayer self && !PlayerLookup.tracking(entity).contains(self)) {
            ServerPlayNetworking.send(self, payload);
        }
    }
}
