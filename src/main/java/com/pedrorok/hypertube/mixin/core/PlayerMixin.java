package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.core.data.PersistentData;
import com.pedrorok.hypertube.core.camera.DetachedPlayerDirController;
import com.pedrorok.hypertube.core.travel.TravelConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/06/2025
 * @project Create Hypertube
 */
@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void createHypertube$onTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!PersistentData.get(entity).getBoolean(TravelConstants.TRAVEL_TAG)) return;

        if (!(entity instanceof Player player) || !entity.level().isClientSide) return;
        createHypertube$tickInClient(player);
    }

    @Unique
    private void createHypertube$tickInClient(Player player) {
        if (!Minecraft.getInstance().player.getUUID().equals(player.getUUID())) {
            return;
        }
        player.setYRot(DetachedPlayerDirController.get().getYaw());
        player.setXRot(DetachedPlayerDirController.get().getPitch());
    }
}
