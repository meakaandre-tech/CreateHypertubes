package com.pedrorok.hypertube.events;

import com.pedrorok.hypertube.client.particles.SuctionParticle;
import com.pedrorok.hypertube.ponder.HypertubesPonderPlugin;
import com.pedrorok.hypertube.registry.ModParticles;
import com.zurrtum.create.client.ponder.foundation.PonderIndex;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

/**
 * @author Rok, Pedro Lucas nmm. Created on 21/04/2025
 * @project Create Hypertube
 */
public class ModClientEvents {

    public static void register() {
        PonderIndex.addPlugin(new HypertubesPonderPlugin());
        ParticleProviderRegistry.getInstance().register(ModParticles.SUCTION_PARTICLE.get(), SuctionParticle.Provider::new);
    }
}
