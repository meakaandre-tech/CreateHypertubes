package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * @author Rok, Pedro Lucas nmm. Created on 02/06/2025
 * @project Create Hypertube
 */
public class ModSounds {

    public static final Supplier<SoundEvent> HYPERTUBE_SUCTION = register("suction");
    public static final Supplier<SoundEvent> TRAVELING = register("traveling");
    public static final Supplier<SoundEvent> HYPERTUBE_ENTRANCE_OPEN = register("entrance_open");
    public static final Supplier<SoundEvent> HYPERTUBE_ENTRANCE_CLOSE = register("entrance_close");
    public static final Supplier<SoundEvent> CHOSE_DIRECTION = register("chose_direction");

    private static Supplier<SoundEvent> register(String name) {
        Identifier id = HypertubeMod.of(name);
        SoundEvent event = Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
        return () -> event;
    }

    public static void register() {
    }
}
