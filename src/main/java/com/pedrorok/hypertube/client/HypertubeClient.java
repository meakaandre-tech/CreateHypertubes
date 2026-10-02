package com.pedrorok.hypertube.client;

import com.pedrorok.hypertube.config.ClientConfig;
import com.pedrorok.hypertube.events.ClientEvents;
import com.pedrorok.hypertube.events.ModClientEvents;
import com.pedrorok.hypertube.network.ClientNetworkHandler;
import com.pedrorok.hypertube.registry.ModBlockEntities;
import com.pedrorok.hypertube.registry.ModKeybinds;
import com.zurrtum.create.client.AllBlockEntityBehaviours;
import com.pedrorok.hypertube.registry.ModPartialModels;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client entry point for the Fabric port.
 */
public class HypertubeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientConfig.load();
        ModPartialModels.init();
        ModKeybinds.register();
        AllBlockEntityBehaviours.add(ModBlockEntities.HYPERTUBE_ENTRANCE.get(), TubeTooltipBehaviour::new);
        AllBlockEntityBehaviours.add(ModBlockEntities.HYPER_ACCELERATOR.get(), TubeTooltipBehaviour::new);
        AllBlockEntityBehaviours.add(ModBlockEntities.HYPER_JUNCTION.get(), TubeTooltipBehaviour::new);
        ClientNetworkHandler.register();
        ModClientEvents.register();
        ClientEvents.register();
    }
}
