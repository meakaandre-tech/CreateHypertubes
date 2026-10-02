package com.pedrorok.hypertube.events;

import com.pedrorok.hypertube.config.ServerConfig;
import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.core.placement.TubePlacement;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import com.pedrorok.hypertube.core.travel.TravelManager;
import com.pedrorok.hypertube.utils.TubeUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author Rok, Pedro Lucas nmm. Created on 22/04/2025
 * @project Create Hypertube
 */
public class ModServerEvents {

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerConfig.get().init());

        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> !TravelManager.hasHyperTubeData(player));

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (TravelManager.hasHyperTubeData(entity)) {
                return false;
            }
            CompoundTag data = PersistentData.get(entity);
            if (!data.getBooleanOr(TravelConstants.IMMUNITY_TAG, false)) return true;
            data.putBoolean(TravelConstants.IMMUNITY_TAG, false);
            return data.getLongOr(TravelConstants.LAST_TRAVEL_TIME, 0L) < System.currentTimeMillis();
        });
    }

    /** Called from LivingEntityTickMixin at the start of every living entity tick, on both sides. */
    public static void onEntityTick(LivingEntity living) {
        if (!ServerConfig.canEntityTravel(living.getType())) return;
        TravelManager.entityTick(living);
        if (living.level().isClientSide()) {
            return;
        }
        if (TravelManager.hasHyperTubeData(living) && living.getPose() != Pose.CROUCHING) {
            living.setPose(Pose.CROUCHING);
        }
        if (!(living instanceof Player player)) return;
        TubePlacement.tickPlayerServer(player);
    }

    /** Called from BlockItemPlaceMixin before a block item is placed. Returns false to cancel. */
    public static boolean allowBlockPlace(Entity entity, Level level, BlockPos pos) {
        if (entity == null) return true;
        if (TravelManager.hasHyperTubeData(entity)) {
            return false;
        }
        if (level.isClientSide()) return true;
        if (!(entity instanceof Player player)) return true;
        return TubeUtils.checkPlayerPlacingBlock(player, level, pos);
    }
}
