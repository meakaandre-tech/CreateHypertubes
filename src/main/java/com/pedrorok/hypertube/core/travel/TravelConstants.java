package com.pedrorok.hypertube.core.travel;

import com.pedrorok.hypertube.HypertubeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 15/06/2025
 * @project Create Hypertube
 */
public class TravelConstants {

    public static final String TRAVEL_TAG = "hypertube_travel";
    public static final String LAST_TRAVEL_TIME = "last_travel_time";

    public static final String LAST_TRAVEL_BLOCKPOS = "last_travel_blockpos";
    public static final String LAST_TRAVEL_SPEED = "last_travel_speed";

    public static final String IMMUNITY_TAG = "hypertube_immunity";

    public static final int DEFAULT_TRAVEL_TIME = 2000;
    public static final int DEFAULT_AFTER_TUBE_CAMERA = 1500; // 0.5 seconds (subtracting default travel time)

    public static final float DEFAULT_SPEED_MULTIPLIER = 1;

    public static final float NEEDED_SPEED = 16;


    public static final TagKey<EntityType<?>> TRAVELLER_ENTITIES =
            TagKey.create(Registries.ENTITY_TYPE, HypertubeMod.of("traveller_entities"));
    public static class Client {
        public static final Map<EntityType<?>, TravellerEntity> ENTITIES_RENDER = new HashMap<>() {{
            put(EntityTypes.PLAYER, TravellerEntity.ofBiped(-0.5f));
            put(EntityTypes.VILLAGER, TravellerEntity.ofBiped(-0.8f));
            put(EntityTypes.WANDERING_TRADER, TravellerEntity.ofBiped(-0.8f));
            put(EntityTypes.SALMON, TravellerEntity.ofFish(0.9f));
            put(EntityTypes.COD, TravellerEntity.ofFish(1f));
            put(EntityTypes.TROPICAL_FISH, TravellerEntity.ofFish(1f));
        }};

    }
}
