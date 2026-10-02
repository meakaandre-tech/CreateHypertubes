package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.events.ModServerEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric has no entity tick event, so this stands in for NeoForge's EntityTickEvent.Pre.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityTickMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void hypertube$onTick(CallbackInfo ci) {
        ModServerEvents.onEntityTick((LivingEntity) (Object) this);
    }
}
