package com.pedrorok.hypertube.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import com.pedrorok.hypertube.utils.TubeUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Rok, Pedro Lucas nmm. Created on 03/06/2025
 * @project Create Hypertube
 */
public class ServerConfig {
    private static final String FILE = HypertubeMod.MOD_ID + "-server.json";
    private static final ServerConfig INSTANCE = new ServerConfig();

    /**
     * How to handle entity travel permissions:
     * TAG_ONLY - use only the 'create_hypertube:traveller_entities' tag from datapacks;
     * WHITELIST - only entities in the whitelist can travel (ignores tag);
     * BLACKLIST - all entities can travel except those in the blacklist;
     * TAG_WITH_BLACKLIST - use the tag but exclude entities in the blacklist.
     */
    public final ConfigValue<EntityListMode> ENTITY_LIST_MODE = new ConfigValue<>(EntityListMode.BLACKLIST);
    /** Entities that CAN travel (only used when mode is WHITELIST). */
    public final ConfigValue<List<String>> ENTITY_WHITELIST = new ConfigValue<>(List.of(
            "minecraft:player",
            "minecraft:villager",
            "minecraft:wandering_trader",
            "create:package"
    ));
    /** Entities that CANNOT travel (used in BLACKLIST and TAG_WITH_BLACKLIST modes). */
    public final ConfigValue<List<String>> ENTITY_BLACKLIST = new ConfigValue<>(List.of(
            "minecraft:wither",
            "minecraft:ender_dragon"
    ));
    /** Multiplier for the speed of the tubes, between 0.5 and 99. Highly experimental. */
    public final ConfigValue<Double> SPEED_MULTIPLIER = new ConfigValue<>(1.0);
    /** Stress impact of the Hyper Entrance block. */
    public final ConfigValue<Double> STRESS_IMPACT_ENTRANCE = new ConfigValue<>(4.0);
    /** Stress impact of the Hyper Accelerator block. */
    public final ConfigValue<Double> STRESS_IMPACT_ACCELERATOR = new ConfigValue<>(4.0);
    /**
     * Whether hypertubes have solid collision along their curved path. Disabling only stops new tubes
     * from gaining collision; existing tubes keep theirs until they are reconnected.
     */
    public final ConfigValue<Boolean> TUBE_COLLISION = new ConfigValue<>(true);

    private final Set<EntityType<?>> cachedWhitelist = new HashSet<>();
    private final Set<EntityType<?>> cachedBlacklist = new HashSet<>();

    private ServerConfig() {
    }

    public static ServerConfig get() {
        return INSTANCE;
    }

    public static void load() {
        ServerConfig config = INSTANCE;
        JsonObject json = ConfigFile.read(FILE);
        try {
            if (json.has("entityListMode")) {
                config.ENTITY_LIST_MODE.set(EntityListMode.valueOf(json.get("entityListMode").getAsString()));
            }
            if (json.has("entityWhitelist")) {
                config.ENTITY_WHITELIST.set(readList(json.getAsJsonArray("entityWhitelist")));
            }
            if (json.has("entityBlacklist")) {
                config.ENTITY_BLACKLIST.set(readList(json.getAsJsonArray("entityBlacklist")));
            }
            if (json.has("speedMultiplier")) {
                config.SPEED_MULTIPLIER.set(clamp(json.get("speedMultiplier").getAsDouble(), 0.5, 99.0));
            }
            if (json.has("entranceStressImpact")) {
                config.STRESS_IMPACT_ENTRANCE.set(clamp(json.get("entranceStressImpact").getAsDouble(), 0.0, 100.0));
            }
            if (json.has("acceleratorStressImpact")) {
                config.STRESS_IMPACT_ACCELERATOR.set(clamp(json.get("acceleratorStressImpact").getAsDouble(), 0.0, 100.0));
            }
            if (json.has("tubeCollision")) {
                config.TUBE_COLLISION.set(json.get("tubeCollision").getAsBoolean());
            }
        } catch (Exception e) {
            HypertubeMod.LOGGER.error("Invalid value in {}, keeping defaults for the rest", FILE, e);
        }

        JsonObject out = new JsonObject();
        out.addProperty("entityListMode", config.ENTITY_LIST_MODE.get().name());
        out.add("entityWhitelist", writeList(config.ENTITY_WHITELIST.get()));
        out.add("entityBlacklist", writeList(config.ENTITY_BLACKLIST.get()));
        out.addProperty("speedMultiplier", config.SPEED_MULTIPLIER.get());
        out.addProperty("entranceStressImpact", config.STRESS_IMPACT_ENTRANCE.get());
        out.addProperty("acceleratorStressImpact", config.STRESS_IMPACT_ACCELERATOR.get());
        out.addProperty("tubeCollision", config.TUBE_COLLISION.get());
        ConfigFile.write(FILE, out);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static List<String> readList(JsonArray array) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }
        return list;
    }

    private static JsonArray writeList(List<String> list) {
        JsonArray array = new JsonArray();
        list.forEach(array::add);
        return array;
    }

    public void init() {
        loadEntityList(ENTITY_WHITELIST.get(), cachedWhitelist);
        loadEntityList(ENTITY_BLACKLIST.get(), cachedBlacklist);
        TubeUtils.SPEED_MULTIPLIER = SPEED_MULTIPLIER.get().floatValue();
    }

    private void loadEntityList(List<String> entityIds, Set<EntityType<?>> targetSet) {
        targetSet.clear();
        for (String entityId : entityIds) {
            try {
                Identifier location = Identifier.parse(entityId);
                BuiltInRegistries.ENTITY_TYPE.getOptional(location).ifPresentOrElse(
                        targetSet::add,
                        () -> HypertubeMod.LOGGER.warn("Unknown entity type in config: {}", entityId)
                );
            } catch (Exception e) {
                HypertubeMod.LOGGER.warn("Invalid entity ID in config: {} - {}", entityId, e.getMessage());
            }
        }
    }

    public static boolean canEntityTravel(EntityType<?> type) {
        boolean isInTag = type.is(TravelConstants.TRAVELLER_ENTITIES);
        return get().canEntityTravel(type, isInTag);
    }

    public boolean canEntityTravel(EntityType<?> entityType, boolean isInTag) {
        return switch (ENTITY_LIST_MODE.get()) {
            case TAG_ONLY -> isInTag;
            case WHITELIST -> cachedWhitelist.contains(entityType);
            case BLACKLIST -> !cachedBlacklist.contains(entityType);
            case TAG_WITH_BLACKLIST -> isInTag && !cachedBlacklist.contains(entityType);
        };
    }

    public Set<EntityType<?>> getWhitelist() {
        return cachedWhitelist;
    }

    public Set<EntityType<?>> getBlacklist() {
        return cachedBlacklist;
    }

    public enum EntityListMode {
        TAG_ONLY,
        WHITELIST,
        BLACKLIST,
        TAG_WITH_BLACKLIST
    }
}
