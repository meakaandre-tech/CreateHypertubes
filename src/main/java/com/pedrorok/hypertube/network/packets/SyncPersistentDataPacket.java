package com.pedrorok.hypertube.network.packets;

import com.pedrorok.hypertube.client.ClientHooks;
import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import com.pedrorok.hypertube.core.travel.client.ClientTravelPathMover;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import com.pedrorok.hypertube.network.PayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/06/2025
 * @project Create Hypertube
 */
public record SyncPersistentDataPacket(int entityId, CompoundTag readData) implements CustomPacketPayload {

    public static final Type<SyncPersistentDataPacket> TYPE = new Type<>(
           HypertubeMod.of("travel_index")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPersistentDataPacket> STREAM_CODEC =
            StreamCodec.of(SyncPersistentDataPacket::encode, SyncPersistentDataPacket::decode);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(RegistryFriendlyByteBuf buf, SyncPersistentDataPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeNbt(packet.readData);
    }

    public static SyncPersistentDataPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncPersistentDataPacket(
                buf.readInt(),
                buf.readNbt()
        );
    }

    public static SyncPersistentDataPacket create(Entity entity) {
        return new SyncPersistentDataPacket(
                entity.getId(),
                PersistentData.get(entity)
        );
    }

    public static void handle(SyncPersistentDataPacket packet, PayloadContext context) {
        context.enqueueWork(() -> {
            handleClient(packet);
        });
    }

    private static void handleClient(SyncPersistentDataPacket packet) {
        try {
            Entity entityByID = ClientHooks.entity(packet.entityId);
            CompoundTag data = PersistentData.get(entityByID);
            new HashSet<>(data.keySet()).forEach(data::remove);
            data.merge(packet.readData);
            if (!data.getBooleanOr(TravelConstants.TRAVEL_TAG, false)) {
                ClientTravelPathMover.stopMoving(packet.entityId);
            }
        } catch (Exception e) {
            HypertubeMod.LOGGER.error("Failed to handle SyncPersistentDataPacket for entity ID: {}", packet.entityId, e);
        }
    }
}
