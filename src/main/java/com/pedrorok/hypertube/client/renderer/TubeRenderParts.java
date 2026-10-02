package com.pedrorok.hypertube.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.hypertube.blocks.blockentities.ActionTubeBlockEntity;
import com.pedrorok.hypertube.client.BezierTextureRenderer;
import com.pedrorok.hypertube.core.connection.BezierConnection;
import com.pedrorok.hypertube.core.connection.interfaces.IConnection;
import com.pedrorok.hypertube.utils.RenderUtils;
import com.zurrtum.create.client.catnip.render.CachedBuffers;
import com.zurrtum.create.client.catnip.render.SuperByteBuffer;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * The parts every hypertube block entity draws: the curved tubes leaving the block and any attachments on it.
 * Collected while the render state is extracted, drawn when it is submitted.
 */
public class TubeRenderParts {

    private static final BezierTextureRenderer TUBES = BezierTextureRenderer.get();

    public final List<SuperByteBufferRenderState> attachments = new ArrayList<>();
    public final List<BezierTextureRenderer.Prepared> tubes = new ArrayList<>();

    public void clear() {
        attachments.clear();
        tubes.clear();
    }

    public void addTube(BlockPos pos, IConnection connection) {
        if (connection instanceof BezierConnection bezier) {
            BezierTextureRenderer.Prepared prepared = TUBES.prepare(pos, bezier);
            if (prepared != null) {
                tubes.add(prepared);
            }
        }
    }

    public void addAttachments(ActionTubeBlockEntity be, BlockState blockState, Direction facing, boolean isTubeVertical, int light) {
        be.getTubeAttachments().forEach((direct, attachment) -> {
            SuperByteBuffer model = CachedBuffers.partial(attachment.getPartialModel(blockState, be, direct), blockState);
            RenderUtils.rotateToFace(model, facing, direct.getOpposite(), isTubeVertical);
            attachments.add(model.light(light).extractRenderState());
        });
    }

    public void submit(PoseStack matrices, SubmitNodeCollector queue) {
        for (SuperByteBufferRenderState attachment : attachments) {
            attachment.submit(matrices, queue);
        }
        for (BezierTextureRenderer.Prepared tube : tubes) {
            TUBES.submit(tube, matrices, queue);
        }
    }
}
