package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.items.TubeAttachmentItem;
import com.pedrorok.hypertube.registry.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/04/2025
 * @project Create Hypertube
 */
public class ModItems {

    public static final ItemEntry<Item> HYPERTUBE_FUNNEL = item("hypertube_funnel", Item::new, true);

    public static final ItemEntry<TubeAttachmentItem> REDSTONE_DETECTOR = item("redstone_detector_tube_attachment", properties -> new TubeAttachmentItem("redstone_input", properties), true);

    public static final ItemEntry<TubeAttachmentItem> TUBE_SCANNER = item("tube_scanner_attachment", properties -> new TubeAttachmentItem("tube_scanner", properties), true);

    public static final ItemEntry<Item> TUBE_SCANNER_UNFINISHED = item("tube_scanner_unfinished", Item::new, false);

    private static <T extends Item> ItemEntry<T> item(String name, Function<Item.Properties, T> factory, boolean inTab) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, HypertubeMod.of(name));
        T item = Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
        if (inTab) {
            ModBlocks.TAB_ITEMS.add(item);
        }
        return new ItemEntry<>(item);
    }

    public static void register() {
    }
}
