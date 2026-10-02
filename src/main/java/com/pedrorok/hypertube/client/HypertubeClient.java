package com.pedrorok.hypertube.client;

import com.pedrorok.hypertube.config.ClientConfig;
import com.pedrorok.hypertube.events.ClientEvents;
import com.pedrorok.hypertube.events.ModClientEvents;
import com.pedrorok.hypertube.network.ClientNetworkHandler;
import com.pedrorok.hypertube.registry.ModBlockEntities;
import com.pedrorok.hypertube.registry.ModKeybinds;
import com.zurrtum.create.client.AllBlockEntityBehaviours;
import com.pedrorok.hypertube.registry.ModPartialModels;
import com.pedrorok.hypertube.client.renderer.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

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
        BlockEntityRendererRegistry.register(ModBlockEntities.HYPERTUBE.get(), HypertubeBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HYPERTUBE_ENTRANCE.get(), EntranceBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HYPER_ACCELERATOR.get(), AcceleratorEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HYPER_JUNCTION.get(), JunctionEntityRenderer::new);
        ClientNetworkHandler.register();
        ModClientEvents.register();
        ClientEvents.register();
    }
}
