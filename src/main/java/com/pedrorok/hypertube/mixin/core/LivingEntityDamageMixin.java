package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Entities take no damage while travelling. These hooks lived on Entity before damage handling
 * moved to the server-side methods on LivingEntity.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void createHypertube$cancelHurt(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!PersistentData.get((LivingEntity) (Object) this).getBooleanOr(TravelConstants.TRAVEL_TAG, false)) return;
        cir.setReturnValue(false);
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void createHypertube$cancelInvulnerableTo(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (!PersistentData.get((LivingEntity) (Object) this).getBooleanOr(TravelConstants.TRAVEL_TAG, false)) return;
        cir.setReturnValue(true);
    }
}
