package com.pedrorok.hypertube.network;

import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

/**
 * Loader-neutral stand-in for NeoForge's PayloadContext.
 */
public record PayloadContext(Player player, Consumer<Runnable> executor) {
    public void enqueueWork(Runnable work) {
        executor.accept(work);
    }
}
