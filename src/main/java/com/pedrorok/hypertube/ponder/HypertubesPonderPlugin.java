package com.pedrorok.hypertube.ponder;

import com.pedrorok.hypertube.HypertubeMod;
import com.zurrtum.create.client.ponder.api.registration.PonderPlugin;
import com.zurrtum.create.client.ponder.api.registration.PonderSceneRegistrationHelper;
import com.zurrtum.create.client.ponder.api.registration.PonderTagRegistrationHelper;
import com.zurrtum.create.client.ponder.foundation.ui.PonderUI;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author Rok, Pedro Lucas nmm. 27/01/2026
 * @project Create Hypertube
 */
public class HypertubesPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return HypertubeMod.MOD_ID;
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<Identifier> helper) {
        HypertubesPonderScenes.register(helper);
    }

    public static boolean isAnyPonderScreenOpen() {
        return Minecraft.getInstance().screen instanceof PonderUI;
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<Identifier> helper) {
        HypertubesPonderTags.register(helper);
    }
}
