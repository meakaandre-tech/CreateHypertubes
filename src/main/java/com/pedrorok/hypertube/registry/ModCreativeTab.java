package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * @author Rok, Pedro Lucas nmm. Created on 21/04/2025
 * @project Create Hypertube
 */
public class ModCreativeTab {

    public static CreativeModeTab TUBE_TAB;

    public static void register() {
        TUBE_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                HypertubeMod.of("create_hypertubes"),
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + HypertubeMod.MOD_ID))
                        .icon(ModBlocks.HYPERTUBE::asStack)
                        .displayItems((parameters, output) -> {
                            for (Item item : ModBlocks.TAB_ITEMS) {
                                output.accept(item);
                            }
                        })
                        .build()
        );
    }
}
