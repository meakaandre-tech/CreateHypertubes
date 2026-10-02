package com.pedrorok.hypertube.mixin.core;

import com.pedrorok.hypertube.events.ModServerEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stands in for NeoForge's BlockEvent.EntityPlaceEvent.
 */
@Mixin(BlockItem.class)
public abstract class BlockItemPlaceMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void hypertube$beforePlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (!ModServerEvents.allowBlockPlace(context.getPlayer(), context.getLevel(), context.getClickedPos())) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
