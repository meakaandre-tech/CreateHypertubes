package com.pedrorok.hypertube.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.hypertube.blocks.HyperAcceleratorBlock;
import com.pedrorok.hypertube.blocks.HyperJunctionBlock;
import net.minecraft.world.level.block.state.BlockState;
import com.pedrorok.hypertube.blocks.blockentities.HyperJunctionBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * @author Rok, Pedro Lucas nmm. Created on 25/06/2025
 * @project Create Hypertube
 */
public class JunctionEntityRenderer implements BlockEntityRenderer<HyperJunctionBlockEntity, JunctionEntityRenderer.State> {

    public JunctionEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(HyperJunctionBlockEntity be, State state, float tickProgress, Vec3 cameraPos,
                                   @Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(be, state, crumblingOverlay);
        state.parts.clear();
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof HyperJunctionBlock)) {
            return;
        }
        state.parts.addAttachments(be, blockState, blockState.getValue(HyperAcceleratorBlock.FACING), false, state.lightCoords);
        state.parts.addTube(state.blockPos, be.getConnectionOne());
        state.parts.addTube(state.blockPos, be.getConnectionTwo());
        state.parts.addTube(state.blockPos, be.getConnectionThree());
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        state.parts.submit(matrices, queue);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public boolean shouldRender(HyperJunctionBlockEntity be, Vec3 cameraPos) {
        return true;
    }

    public static class State extends BlockEntityRenderState {
        public final TubeRenderParts parts = new TubeRenderParts();
    }
}
