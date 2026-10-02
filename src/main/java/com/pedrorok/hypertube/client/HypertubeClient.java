package com.pedrorok.hypertube.client;

import com.pedrorok.hypertube.config.ClientConfig;
import com.pedrorok.hypertube.events.ClientEvents;
import com.pedrorok.hypertube.events.ModClientEvents;
import com.pedrorok.hypertube.network.ClientNetworkHandler;
import com.pedrorok.hypertube.registry.ModKeybinds;
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
        ClientNetworkHandler.register();
        ModClientEvents.register();
        ClientEvents.register();
    }
}
