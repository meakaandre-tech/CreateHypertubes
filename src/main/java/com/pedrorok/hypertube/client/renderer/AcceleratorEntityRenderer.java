package com.pedrorok.hypertube.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.hypertube.blocks.HyperAcceleratorBlock;
import com.pedrorok.hypertube.blocks.blockentities.HyperAcceleratorBlockEntity;
import com.pedrorok.hypertube.registry.ModPartialModels;
import com.zurrtum.create.client.catnip.render.CachedBuffers;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import com.zurrtum.create.client.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * @author Rok, Pedro Lucas nmm. Created on 02/06/2025
 * @project Create Hypertube
 */
public class AcceleratorEntityRenderer extends KineticBlockEntityRenderer<HyperAcceleratorBlockEntity, AcceleratorEntityRenderer.State> {

    public AcceleratorEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(HyperAcceleratorBlockEntity be, State state, float tickProgress, Vec3 cameraPos,
                                   @Nullable CrumblingOverlay crumblingOverlay) {
        state.parts.clear();
        state.cogwheel = null;

        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof HyperAcceleratorBlock)) {
            state.blockPos = be.getBlockPos();
            state.blockEntityType = be.getType();
            return;
        }
        updateBaseRenderState(be, state, be.getLevel(), crumblingOverlay);

        Direction facing = blockState.getValue(HyperAcceleratorBlock.FACING);
        state.parts.addAttachments(be, blockState, facing, facing.getAxis().isVertical(), state.lightCoords);

        float angle = getAngleForBe(be, state.blockPos, facing.getAxis());
        state.cogwheel = CachedBuffers.partialFacingVertical(ModPartialModels.COGWHEEL_HOLE, blockState, facing)
                .light(state.lightCoords)
                .rotateCentered(angle, state.direction)
                .color(state.color)
                .extractRenderState();

        state.parts.addTube(state.blockPos, be.getConnectionOne());
        state.parts.addTube(state.blockPos, be.getConnectionTwo());
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state.cogwheel != null) {
            state.cogwheel.submit(matrices, queue);
        }
        state.parts.submit(matrices, queue);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public boolean shouldRender(HyperAcceleratorBlockEntity be, Vec3 cameraPos) {
        return true;
    }

    public static class State extends KineticRenderState {
        public final TubeRenderParts parts = new TubeRenderParts();
        public @Nullable SuperByteBufferRenderState cogwheel;
    }
}
