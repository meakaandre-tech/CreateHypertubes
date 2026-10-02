package com.pedrorok.hypertube.network;

import com.pedrorok.hypertube.network.packets.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Client half of the packet registration.
 */
public class ClientNetworkHandler {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SyncPersistentDataPacket.TYPE, (packet, ctx) -> SyncPersistentDataPacket.handle(packet, client(ctx)));
        ClientPlayNetworking.registerGlobalReceiver(EntityTravelDirDataPacket.TYPE, (packet, ctx) -> EntityTravelDirDataPacket.handle(packet, client(ctx)));
        ClientPlayNetworking.registerGlobalReceiver(MovePathPacket.TYPE, (packet, ctx) -> MovePathPacket.handle(packet, client(ctx)));
        ClientPlayNetworking.registerGlobalReceiver(SyncEntityPosPacket.TYPE, (packet, ctx) -> SyncEntityPosPacket.handle(packet, client(ctx)));
        ClientPlayNetworking.registerGlobalReceiver(SpeedChangePacket.TYPE, (packet, ctx) -> SpeedChangePacket.handle(packet, client(ctx)));
    }

    private static PayloadContext client(ClientPlayNetworking.Context ctx) {
        return new PayloadContext(ctx.player(), ctx.client()::execute);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
