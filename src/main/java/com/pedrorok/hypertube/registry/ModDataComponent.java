package com.pedrorok.hypertube.registry;

import com.pedrorok.hypertube.HypertubeMod;
import com.pedrorok.hypertube.core.connection.BezierConnection;
import com.pedrorok.hypertube.core.connection.SimpleConnection;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

/**
 * @author Rok, Pedro Lucas nmm. Created on 23/04/2025
 * @project Create Hypertube
 */
public class ModDataComponent {

    public static final DataComponentType<SimpleConnection> TUBE_CONNECTING_FROM = register(
            "tube_connecting_from",
            builder -> builder.persistent(SimpleConnection.CODEC).networkSynchronized(SimpleConnection.STREAM_CODEC)
    );

    public static final DataComponentType<BezierConnection> BEZIER_CONNECTION = register(
            "bezier_connection",
            builder -> builder.persistent(BezierConnection.CODEC).networkSynchronized(BezierConnection.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                HypertubeMod.of(name),
                builder.apply(DataComponentType.builder()).build()
        );
    }

    public static void register() {
    }
}
