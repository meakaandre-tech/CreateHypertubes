package com.pedrorok.hypertube.core;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Block entities that add lines to Create's goggle overlay. Create Fly reads goggle info from a
 * client-side behaviour, so {@code TubeTooltipBehaviour} forwards to this.
 */
public interface TubeGoggleInfo {
    boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking);
}
