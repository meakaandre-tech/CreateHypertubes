package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.blocks.blockentities.*;
import com.pedrorok.hypertube.registry.entry.BlockEntityEntry;
import com.pedrorok.hypertube.registry.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

/**
 * @author Rok, Pedro Lucas nmm. Created on 21/04/2025
 * @project Create Hypertube
 */
public class ModBlockEntities {

    @FunctionalInterface
    private interface Factory<T extends BlockEntity> {
        T create(BlockEntityType<?> type, BlockPos pos, BlockState state);
    }

    public static final BlockEntityEntry<HyperEntranceBlockEntity> HYPERTUBE_ENTRANCE = register("hypertube_entrance_entity", HyperEntranceBlockEntity::new, ModBlocks.HYPERTUBE_ENTRANCE);

    public static final BlockEntityEntry<HypertubeBlockEntity> HYPERTUBE = register("hypertube_entity", HypertubeBlockEntity::new, ModBlocks.HYPERTUBE);

    public static final BlockEntityEntry<HyperAcceleratorBlockEntity> HYPER_ACCELERATOR = register("hyper_accelerator_entity", HyperAcceleratorBlockEntity::new, ModBlocks.HYPER_ACCELERATOR);

    public static final BlockEntityEntry<HyperJunctionBlockEntity> HYPER_JUNCTION = register("hyper_junction_entity", HyperJunctionBlockEntity::new, ModBlocks.HYPER_JUNCTION);

    public static final BlockEntityEntry<TubePathBlockEntity> TUBE_PATH = register("tube_path_entity", TubePathBlockEntity::new, ModBlocks.TUBE_PATH);

    private static <T extends BlockEntity> BlockEntityEntry<T> register(String name, Factory<T> factory, BlockEntry<?> block) {
        BlockEntityEntry<T> entry = new BlockEntityEntry<>();
        BlockEntityType<T> type = new BlockEntityType<>((pos, state) -> factory.create(entry.get(), pos, state), Set.of(block.get()));
        entry.set(Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, HypertubeMod.of(name), type));
        return entry;
    }

    public static void register() {
    }
}
