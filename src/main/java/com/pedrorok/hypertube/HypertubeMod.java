package com.pedrorok.hypertube;

import com.pedrorok.hypertube.config.ServerConfig;
import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.core.smarttube.ITubeAttachment;
import com.pedrorok.hypertube.events.ModServerEvents;
import com.pedrorok.hypertube.events.PlayerSyncEvents;
import com.pedrorok.hypertube.network.NetworkHandler;
import com.pedrorok.hypertube.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/04/2025
 * @project Create Hypertube
 */
public class HypertubeMod implements ModInitializer {
    public static final String MOD_ID = "create_hypertube";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerConfig.load();

        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();

        ModCreativeTab.register();
        ModDataComponent.register();
        ModParticles.register();
        ModSounds.register();
        PersistentData.init();

        ITubeAttachment.init();

        NetworkHandler.register();
        ModServerEvents.register();
        PlayerSyncEvents.register();
    }

    public static Identifier of(String resourceId) {
        return Identifier.fromNamespaceAndPath(MOD_ID, resourceId);
    }
}
