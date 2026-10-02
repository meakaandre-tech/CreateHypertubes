package com.pedrorok.hypertube.core.data;

import com.pedrorok.hypertube.HypertubeMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

/**
 * Fabric replacement for NeoForge's Entity#getPersistentData: a saved NBT tag attached to every entity.
 */
public final class PersistentData {
    public static final AttachmentType<CompoundTag> TYPE = AttachmentRegistry.<CompoundTag>builder()
            .persistent(CompoundTag.CODEC)
            .initializer(CompoundTag::new)
            .buildAndRegister(HypertubeMod.of("persistent_data"));

    private PersistentData() {
    }

    public static CompoundTag get(Entity entity) {
        return entity.getAttachedOrCreate(TYPE);
    }

    public static void init() {
    }
}
