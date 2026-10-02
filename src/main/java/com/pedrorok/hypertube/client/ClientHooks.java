package com.pedrorok.hypertube.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Access to the client game state for classes that are also loaded on a dedicated server.
 * Those classes must not name client-only types (Minecraft, LocalPlayer, ClientLevel) in their own method bodies:
 * the verifier resolves them when the class is linked and the dedicated server does not have them.
 * Only call these from code paths that run on the client.
 */
public final class ClientHooks {
    private ClientHooks() {
    }

    @Nullable
    public static Player player() {
        return Minecraft.getInstance().player;
    }

    @Nullable
    public static Level level() {
        return Minecraft.getInstance().level;
    }

    @Nullable
    public static HitResult hitResult() {
        return Minecraft.getInstance().hitResult;
    }

    @Nullable
    public static Entity entity(int id) {
        return Minecraft.getInstance().level.getEntity(id);
    }
}
