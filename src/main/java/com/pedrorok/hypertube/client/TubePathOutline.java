package com.pedrorok.hypertube.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pedrorok.hypertube.blocks.blockentities.TubePathBlockEntity;
import com.pedrorok.hypertube.core.collision.TubeCollision;
import com.pedrorok.hypertube.core.collision.TubeCollision.Polyline;
import com.pedrorok.hypertube.core.collision.TubeFiller;
import com.pedrorok.hypertube.core.connection.BezierConnection;
import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm.
 * @project Create Hypertube
 */
public final class TubePathOutline {

    private static final float STEP = 0.5f;

    private static final float R = 0f;
    private static final float G = 0f;
    private static final float B = 0f;
    private static final float A = 0.4f;

    private TubePathOutline() {
    }

    /**
     * @return false when the tube outline was drawn and the vanilla block outline should be skipped
     */
    public static boolean renderTubeOutline(LevelRenderContext context, BlockOutlineRenderState outline) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return true;
        }

        BlockPos hit = outline.pos();
        if (!(level.getBlockEntity(hit) instanceof TubePathBlockEntity)) {
            return true;
        }
        BezierConnection bezier = TubeFiller.ownerBezier(level, hit);
        if (bezier == null) {
            return true;
        }

        draw(context, bezier, bezier.getFromPos().pos(), level);
        return false;
    }

    private static void draw(LevelRenderContext context, BezierConnection bezier, BlockPos origin, Level level) {
        List<Vec3> points = bezier.getBezierPoints(level, origin);
        if (points.size() < 2) {
            return;
        }

        Vec3 camera = context.levelState().cameraRenderState.pos;
        float width = context.gameRenderer().gameRenderState().windowRenderState.appropriateLineWidth;

        Polyline path = Polyline.of(points);
        float length = (float) path.length();
        int steps = Math.max(2, Math.round(length / STEP));

        Vec3[][] rings = new Vec3[steps + 1][];
        for (int i = 0; i <= steps; i++) {
            Vec3[] corners = TubeCollision.crossSectionCorners(path, (double) length * i / steps);
            for (int c = 0; c < 4; c++) {
                corners[c] = corners[c].subtract(camera);
            }
            rings[i] = corners;
        }

        context.submitNodeCollector().submitCustomGeometry(context.poseStack(), RenderTypes.lines(), (pose, consumer) -> {
            for (int i = 0; i <= steps; i++) {
                if (i == 0 || i == steps) {
                    loop(consumer, pose, rings[i], width);
                }
                if (i > 0) {
                    for (int c = 0; c < 4; c++) {
                        line(consumer, pose, rings[i - 1][c], rings[i][c], width);
                    }
                }
            }
        });
    }

    private static void loop(VertexConsumer consumer, PoseStack.Pose pose, Vec3[] corners, float width) {
        for (int c = 0; c < 4; c++) {
            line(consumer, pose, corners[c], corners[(c + 1) % 4], width);
        }
    }

    private static void line(VertexConsumer consumer, PoseStack.Pose pose, Vec3 a, Vec3 b, float width) {
        Vec3 normal = b.subtract(a);
        if (normal.lengthSqr() < 1.0e-9) {
            return;
        }
        normal = normal.normalize();
        consumer.addVertex(pose, (float) a.x, (float) a.y, (float) a.z)
                .setColor(R, G, B, A)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z)
                .setLineWidth(width);
        consumer.addVertex(pose, (float) b.x, (float) b.y, (float) b.z)
                .setColor(R, G, B, A)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z)
                .setLineWidth(width);
    }
}
