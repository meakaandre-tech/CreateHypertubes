package com.pedrorok.hypertube.ponder;

import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;
import com.pedrorok.hypertube.ponder.scenes.AcceleratorScenes;
import com.pedrorok.hypertube.ponder.scenes.AttachmentScenes;
import com.pedrorok.hypertube.ponder.scenes.EntranceScenes;
import com.pedrorok.hypertube.ponder.scenes.SplitterScenes;
import com.pedrorok.hypertube.ponder.scenes.TubeScenes;
import com.pedrorok.hypertube.registry.ModBlocks;
import com.pedrorok.hypertube.registry.ModItems;
import com.zurrtum.create.client.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.Identifier;

/**
 * @author Rok, Pedro Lucas nmm. 27/01/2026
 * @project Create Hypertube
 */
public class HypertubesPonderScenes {

    public static void register(PonderSceneRegistrationHelper<Identifier> helper) {
        PonderSceneRegistrationHelper<ItemLike> HELPER = helper.withKeyFunction(item -> BuiltInRegistries.ITEM.getKey(item.asItem()));
        HELPER.forComponents(ModBlocks.HYPERTUBE)
                .addStoryBoard("simple_tube", TubeScenes::simpleTube);
        HELPER.forComponents(ModBlocks.HYPERTUBE_ENTRANCE)
                .addStoryBoard("entrance", EntranceScenes::entranceScene);
        HELPER.forComponents(ModBlocks.HYPER_ACCELERATOR)
                .addStoryBoard("accelerator", AcceleratorScenes::acceleratorScene);
        HELPER.forComponents(ModBlocks.HYPER_JUNCTION)
                .addStoryBoard("splitter", SplitterScenes::splitterScene);
        HELPER.forComponents(ModItems.REDSTONE_DETECTOR, ModItems.TUBE_SCANNER)
                .addStoryBoard("attachment", AttachmentScenes::attachmentScene);
    }
}
