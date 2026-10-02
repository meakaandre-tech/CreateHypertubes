package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.blocks.*;
import com.pedrorok.hypertube.items.HypertubeItem;
import com.pedrorok.hypertube.registry.entry.BlockEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/04/2025
 * @project Create Hypertube
 */
public class ModBlocks {
    /** Items shown in the creative tab, in registration order. */
    public static final List<Item> TAB_ITEMS = new ArrayList<>();

    private static BlockBehaviour.Properties tubeProperties() {
        return BlockBehaviour.Properties.of()
                .destroyTime(1.0f)
                .explosionResistance(10.0f)
                .sound(SoundType.METAL)
                .noOcclusion()
                .isViewBlocking((state, level, pos) -> false)
                .isSuffocating((state, level, pos) -> false);
    }

    public static final BlockEntry<HypertubeBlock> HYPERTUBE = block("hypertube", HypertubeBlock::new, ModBlocks::tubeProperties, HypertubeItem::new);

    public static final BlockEntry<HyperEntranceBlock> HYPERTUBE_ENTRANCE = block("hypertube_entrance", HyperEntranceBlock::new, ModBlocks::tubeProperties, BlockItem::new);

    public static final BlockEntry<HyperAcceleratorBlock> HYPER_ACCELERATOR = block("hypertube_accelerator", HyperAcceleratorBlock::new, ModBlocks::tubeProperties, BlockItem::new);

    public static final BlockEntry<HyperJunctionBlock> HYPER_JUNCTION = block("hypertube_junction", HyperJunctionBlock::new, ModBlocks::tubeProperties, BlockItem::new);

    public static final BlockEntry<TubePathBlock> TUBE_PATH = block(
            "tube_path",
            TubePathBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F)
                    .noOcclusion()
                    .noLootTable()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false),
            null
    );

    private static <T extends Block> BlockEntry<T> block(
            String name,
            Function<BlockBehaviour.Properties, T> factory,
            Supplier<BlockBehaviour.Properties> properties,
            BiFunction<Block, Item.Properties, ? extends Item> itemFactory
    ) {
        Identifier id = HypertubeMod.of(name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        T block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.get().setId(blockKey)));
        if (itemFactory != null) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            Item item = itemFactory.apply(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            if (item instanceof BlockItem blockItem) {
                blockItem.registerBlocks(Item.BY_BLOCK, item);
            }
            Registry.register(BuiltInRegistries.ITEM, itemKey, item);
            TAB_ITEMS.add(item);
        }
        return new BlockEntry<>(block);
    }

    public static void register() {
    }
}
