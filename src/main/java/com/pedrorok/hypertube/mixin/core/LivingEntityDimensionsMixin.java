package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.core.travel.TravelManager;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stands in for NeoForge's EntityEvent.Size: entities inside a hypertube get a small hitbox.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDimensionsMixin {
    @Unique
    private static final EntityDimensions HYPERTUBE$IN_TUBE = EntityDimensions.fixed(0.5F, 0.5F);

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void hypertube$tubeDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (TravelManager.hasHyperTubeData((LivingEntity) (Object) this)) {
            cir.setReturnValue(HYPERTUBE$IN_TUBE);
        }
    }
}
