package com.pedrorok.hypertube.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pedrorok.hypertube.HypertubeMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads and writes a small JSON config file in the game's config folder.
 */
final class ConfigFile {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ConfigFile() {
    }

    static JsonObject read(String fileName) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                HypertubeMod.LOGGER.error("Could not read {}, using defaults", fileName, e);
            }
        }
        return new JsonObject();
    }

    static void write(String fileName, JsonObject json) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(json, writer);
        } catch (Exception e) {
            HypertubeMod.LOGGER.error("Could not write {}", fileName, e);
        }
    }
}
