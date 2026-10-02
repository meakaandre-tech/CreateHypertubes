package com.pedrorok.hypertube.registry;

import com.mojang.blaze3d.platform.InputConstants;
import com.pedrorok.hypertube.HypertubeMod;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * @author Rok, Pedro Lucas nmm. Created on 28/07/2025
 * @project Create Hypertube
 */
public enum ModKeybinds {

    ESCAPE("tube_escape", GLFW.GLFW_KEY_LEFT_SHIFT);

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(HypertubeMod.of("hypertube"));

    private final String description;
    private final int defaultKey;
    private KeyMapping mapping;

    ModKeybinds(String description, int defaultKey) {
        this.description = description;
        this.defaultKey = defaultKey;
    }

    public boolean isDown() {
        return mapping != null && !mapping.isUnbound() && isKeyPressed();
    }

    private boolean isKeyPressed() {
        int keyCode = KeyMappingHelper.getBoundKeyOf(mapping).getValue();
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), keyCode);
    }

    public Component message() {
        return mapping.getTranslatedKeyMessage();
    }

    public static void register() {
        for (ModKeybinds key : values()) {
            key.mapping = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.hypertube." + key.description, key.defaultKey, CATEGORY));
        }
    }
}
