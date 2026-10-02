package com.pedrorok.hypertube.ponder;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.registry.ModBlocks;
import com.pedrorok.hypertube.registry.ModItems;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.zurrtum.create.catnip.registry.RegisteredObjectsHelper;
import com.zurrtum.create.client.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;

/**
 * @author Rok, Pedro Lucas nmm. 04/02/2026
 * @project Create Hypertube
 */
public class HypertubesPonderTags {

    public static final Identifier

            HYPERTUBE_SYSTEMS = loc("hypertube_systems");

    private static Identifier loc(String id) {
        return HypertubeMod.of(id);
    }

    public static void register(PonderTagRegistrationHelper<Identifier> helper) {

        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                RegisteredObjectsHelper::getKeyOrThrow);

        helper.registerTag(HYPERTUBE_SYSTEMS)
                .addToIndex()
                .item(ModBlocks.HYPERTUBE.get(), true, false)
                .title("Hypertube Systems")
                .description("Blocks and items used in Hypertube transportation systems.")
                .register();

        HELPER.addToTag(HYPERTUBE_SYSTEMS)
                .add(ModBlocks.HYPERTUBE)
                .add(ModBlocks.HYPERTUBE_ENTRANCE)
                .add(ModBlocks.HYPER_ACCELERATOR)
                .add(ModBlocks.HYPER_JUNCTION)
                .add(ModItems.REDSTONE_DETECTOR)
                .add(ModItems.TUBE_SCANNER)
        ;
    }

}
