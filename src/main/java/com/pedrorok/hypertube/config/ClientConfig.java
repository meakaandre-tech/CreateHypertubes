package com.pedrorok.hypertube.config;

import com.google.gson.JsonObject;
import com.pedrorok.hypertube.HypertubeMod;

/**
 * @author Rok, Pedro Lucas nmm. Created on 03/06/2025
 * @project Create Hypertube
 */
public class ClientConfig {
    private static final String FILE = HypertubeMod.MOD_ID + "-client.json";
    private static final ClientConfig INSTANCE = new ClientConfig();

    /** Allow first-person view inside the tube. Default is false for better experience. */
    public final ConfigValue<Boolean> ALLOW_FPV_INSIDE_TUBE = new ConfigValue<>(false);

    private ClientConfig() {
    }

    public static ClientConfig get() {
        return INSTANCE;
    }

    public static void load() {
        JsonObject json = ConfigFile.read(FILE);
        if (json.has("allowFPVInsideTheTube")) {
            INSTANCE.ALLOW_FPV_INSIDE_TUBE.set(json.get("allowFPVInsideTheTube").getAsBoolean());
        }
        json.addProperty("allowFPVInsideTheTube", INSTANCE.ALLOW_FPV_INSIDE_TUBE.get());
        ConfigFile.write(FILE, json);
    }
}
