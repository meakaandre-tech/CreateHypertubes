package com.pedrorok.hypertube.client;

import com.pedrorok.hypertube.core.TubeGoggleInfo;
import com.zurrtum.create.client.foundation.blockEntity.behaviour.tooltip.KineticTooltipBehaviour;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Adds the tube block entity's own lines after Create's kinetic goggle info.
 */
public class TubeTooltipBehaviour<T extends KineticBlockEntity> extends KineticTooltipBehaviour<T> {
    public TubeTooltipBehaviour(T be) {
        super(be);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (blockEntity instanceof TubeGoggleInfo info) {
            added |= info.addToGoggleTooltip(tooltip, isPlayerSneaking);
        }
        return added;
    }
}
