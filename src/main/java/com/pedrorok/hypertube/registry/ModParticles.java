package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

/**
 * @author Rok, Pedro Lucas nmm. Created on 03/06/2025
 * @project Create Hypertube
 */
public class ModParticles {

    private static final SimpleParticleType SUCTION = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            HypertubeMod.of("suction_particle"),
            new SimpleParticleType(true) {
            }
    );

    public static final Supplier<SimpleParticleType> SUCTION_PARTICLE = () -> SUCTION;

    public static void register() {
    }
}
