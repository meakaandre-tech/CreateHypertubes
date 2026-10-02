package com.pedrorok.hypertube.blocks.blockentities;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.blocks.HyperEntranceBlock;
import com.pedrorok.hypertube.blocks.blockentities.parent.ActionTubeBlockEntity;
import com.pedrorok.hypertube.config.ServerConfig;
import com.pedrorok.hypertube.core.collision.TubeFiller;
import com.pedrorok.hypertube.core.connection.BezierConnection;
import com.pedrorok.hypertube.core.connection.TubeConnectionException;
import com.pedrorok.hypertube.core.connection.interfaces.IConnection;
import com.pedrorok.hypertube.core.sound.TubeSoundManager;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import com.pedrorok.hypertube.core.travel.TravelManager;
import com.pedrorok.hypertube.utils.TubeUtils;
import com.pedrorok.hypertube.core.TubeGoggleInfo;
import com.zurrtum.create.client.foundation.blockEntity.behaviour.tooltip.SpeedGaugeTooltipBehaviour;
import com.zurrtum.create.content.kinetics.base.IRotate;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import com.pedrorok.hypertube.utils.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 21/04/2025
 * @project Create Hypertube
 */
public class HyperEntranceBlockEntity extends ActionTubeBlockEntity implements TubeGoggleInfo {


    @Getter
    private IConnection connection;

    public HyperEntranceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // --------- Nbt Methods ---------
    @Override
    protected void read(ValueInput view, boolean clientPacket) {
        super.read(view, clientPacket);
        CompoundTag compound = view.read("HypertubeConnections", CompoundTag.CODEC).orElseGet(CompoundTag::new);
        connection = compound.contains("Connection")
                ? getConnectionRelative(compound, "Connection", worldPosition) : null;
    }

    @Override
    protected void write(ValueOutput view, boolean clientPacket) {
        super.write(view, clientPacket);
        CompoundTag compound = new CompoundTag();
        writeConnectionRelative(compound, worldPosition, new Tuple<>(connection, "Connection"));
        view.store("HypertubeConnections", CompoundTag.CODEC, compound);
    }
    // --------- Nbt Methods ---------

    // --------- Tube Segment Methods ---------
    public boolean wrenchClicked(Direction direction) {
        IConnection connectionInDirection = getConnectionInDirection(direction);
        if (connectionInDirection == null) return false;
        connectionInDirection.updateTubeSegments(level);
        return true;
    }
    // --------- Tube Segment Methods ---------

    @Override
    public void tick() {
        super.tick();
        Boolean isBlocked = getBlockState().getValue(HyperEntranceBlock.IN_FRONT);
        if (level.isClientSide()) {
            tickClient(isBlocked);
            return;
        }
        if (isBlocked) return;


        BlockState state = this.getBlockState();
        BlockPos pos = this.getBlockPos();

        float actualSpeed = Math.abs(this.getSpeed());
        Boolean isOpen = state.getValue(HyperEntranceBlock.OPEN);
        if (actualSpeed < TravelConstants.NEEDED_SPEED) {
            if (!isOpen) return;
            level.setBlock(pos, state.setValue(HyperEntranceBlock.OPEN, false), 3);
            playOpenCloseSound(false);
            return;
        }

        boolean isNotLocked = !getBlockState().getValue(HyperEntranceBlock.LOCKED);
        LivingEntity nearbyEntity = getNearbyLivingEntities((ServerLevel) level, Vec3.atCenterOf(pos));

        boolean canOpen = nearbyEntity != null && (isNotLocked || nearbyEntity.isShiftKeyDown() || PersistentData.get(nearbyEntity).getBooleanOr(TravelConstants.TRAVEL_TAG, false));

        if (isTubeClosed(canOpen, isOpen)) return;

        LivingEntity inRangeEntity = getInRangeLivingEntities((ServerLevel) level, Vec3.atCenterOf(pos), state.getValue(HyperEntranceBlock.FACING));
        if (inRangeEntity == null) return;

        if (isNotLocked && inRangeEntity.isShiftKeyDown() && !PersistentData.get(inRangeEntity).getBooleanOr(TravelConstants.TRAVEL_TAG, false)) {
            return;
        }

        boolean hasStartedTravel = TravelManager.tryStartTravel(inRangeEntity, this, state.getValue(HyperEntranceBlock.FACING), TubeUtils.calculateTravelSpeed(actualSpeed));
        if (!hasStartedTravel) return;
        TubeSoundManager.playTubeSuctionSound(inRangeEntity, Vec3.atCenterOf(getBlockPos()));
    }
    private void tickClient(boolean isBlocked) {
        float actualSpeed = Math.abs(this.getSpeed());
        TubeSoundManager.TubeAmbientSound sound = TubeSoundManager.getAmbientSound(tubeSoundId);
        if (actualSpeed < TravelConstants.NEEDED_SPEED || isBlocked) {
            sound.tickClientPlayerSounds();
            return;
        }
        playClientEffects(sound);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        float finalSpeed = Math.abs(this.getSpeed());
        SpeedGaugeTooltipBehaviour.getFormattedSpeedText(speed, finalSpeed < TravelConstants.NEEDED_SPEED).forGoggles(tooltip);

        if (getBlockState().getValue(HyperEntranceBlock.IN_FRONT)) {
            tooltip.add(Component.literal("     ").append(Component.translatable("tooltip.create_hypertube.entrance_blocked").withColor(0xFF0000)));
        } else if (finalSpeed < TravelConstants.NEEDED_SPEED) {
            tooltip.add(Component.literal("     ").append(Component.literal("▒ ")).append(Component.translatable("tooltip.create_hypertube.entrance_no_speed")).withColor(0xFF0000));
        }
        return true;
    }

    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (getBlockState().getValue(HyperEntranceBlock.LOCKED) && Math.abs(this.getSpeed()) >= TravelConstants.NEEDED_SPEED) {
            tooltip.add(Component.literal("     ").append(Component.translatable("block.hypertube.hyper_entrance.sneak_to_enter")).withColor(0xFFFFFF));
        }
        return true;
    }

    @Override
    public void setConnection(IConnection connection, Direction thisConnectionDir) {
        if (this.connection == null) {
            this.connection = connection;
        } else {
            HypertubeMod.LOGGER.error(new TubeConnectionException("Connection could not define connection", this.connection, connection).getMessage());
            return;
        }
        if (ServerConfig.get().TUBE_COLLISION.get() && connection instanceof BezierConnection bezier) {
            TubeFiller.place(level, bezier);
        }
        setChanged();
        sync();
    }

    @Override
    public void clearConnection(IConnection connection) {
        if (this.connection != null && this.connection.isSameConnection(connection)) {
            this.connection = null;
        } else {
            HypertubeMod.LOGGER.error(new TubeConnectionException("Connection could not be cleared", this.connection, connection).getMessage());
            return;
        }
        setChanged();
        sync();
    }

    @Override
    public float getConnectionOffsetOnDirection(Direction direction) {
        return 0.3f;
    }

    @Override
    public List<Direction> getFacesConnectable() {
        if (connection != null) return List.of();
        return List.of(getBlockState().getValue(HyperEntranceBlock.FACING));
    }

    @Override
    public List<IConnection> getConnections() {
        List<IConnection> connections = new ArrayList<>();
        if (connection != null) {
            connections.add(connection);
        }
        return connections;
    }

    @Override
    public Vec3 getExitDirection(@Nullable Direction connectionDirection) {
        if (getBlockState().hasProperty(HyperEntranceBlock.FACING)) {
            Direction facing = getBlockState().getValue(HyperEntranceBlock.FACING).getOpposite();
            return Vec3.atLowerCornerOf(facing.getUnitVec3i());
        }
        return null;
    }

    @Override
    protected int getConnectionCount() {
        return 1;
    }


    // --------- Stress Methods ---------
    public float calculateStressApplied() {
        float impact = ServerConfig.get().STRESS_IMPACT_ENTRANCE.get().floatValue();
        this.lastStressApplied = impact;
        return impact;
    }
}
