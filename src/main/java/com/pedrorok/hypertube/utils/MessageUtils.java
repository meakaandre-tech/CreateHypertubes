package com.pedrorok.hypertube.utils;

import com.pedrorok.hypertube.core.data.PersistentData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * @author Rok, Pedro Lucas nmm. Created on 27/05/2025
 * @project Create Hypertube
 */
public class MessageUtils {

    public static void sendActionMessage(Player player, Component message) {
        sendActionMessage(player, message, false);
    }

    public static void sendActionMessage(Player player, Component message, boolean forceStay) {
        if (!forceStay && PersistentData.get(player).getLongOr("last_action_message_stay", 0L) > System.currentTimeMillis()) {
            return; // Don't send if the last message is still active
        }
        if (forceStay) {
            PersistentData.get(player).putLong("last_action_message_stay", System.currentTimeMillis() + 2000);
        }
        player.sendOverlayMessage(message);
    }

    public static void sendActionMessage(Player player, String message) {
        sendActionMessage(player, Component.translatable(message));
    }
}
