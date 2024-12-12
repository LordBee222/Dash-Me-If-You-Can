package net.beelabs.dmiyc.client.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.beelabs.dmiyc.client.entity.renderer.renderstate.PermafrostEntityRenderState;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.entity.PermafrostEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class    PermafrostEntityRenderer  extends ProjectileEntityRenderer<PermafrostEntity, PermafrostEntityRenderState> {
    private static final Identifier TEXTURE = DMIYC.id("textures/entity/projectiles/permafrost.png");

    public PermafrostEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public PermafrostEntityRenderState createRenderState() {
        return new PermafrostEntityRenderState();
    }

    @Override
    protected Identifier getTexture(PermafrostEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public void render(PermafrostEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float scale = MathHelper.lerp(state.damage / 12F, 0.1F, 1);
       // scale *= MathHelper.lerp(MathHelper.clamp((state.ticksExisted - (PermafrostEntity.getMaxTicks() - 10F)) / (PermafrostEntity.getMaxTicks() - (PermafrostEntity.getMaxTicks() - 10)), 0, 1), 1F, 0);
        float v = (Math.floorMod(state.ticksExisted, 40) + state.age) / 4F;
        float u = v + 4 * -0.5F / scale;
        VertexConsumer vertices = vertexConsumers.getBuffer(getRenderLayer());
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-state.yaw + 90));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch + 90));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.ticksExisted + state.age * 12));
        matrices.scale(scale, 1, scale);
        MatrixStack.Entry entry = matrices.peek();
       // matrices.translate(0, state.age, 0);
        for (int i = state.ticksExisted; i < state.distanceTraveled; i++) {
            renderSection(state, matrices, entry, vertices, u, v);
        }
        matrices.pop();
    }

    @Override
    public void updateRenderState(PermafrostEntity entity, PermafrostEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.distanceTraveled = (int) entity.distanceTraveled;
        state.ticksExisted = entity.ticksExisted;
        state.damage = (float) entity.getDamage();
        state.damageMultiplier = entity.getDamageMultiplier((int) (entity.distanceTraveled - 1));
    }

    @Override
    protected boolean canBeCulled(PermafrostEntity entity) {
        return false;
    }

    private static RenderLayer getRenderLayer() {
        return RenderLayer.getEntityAlpha(TEXTURE);
    }

    private static void renderSection(PermafrostEntityRenderState state, MatrixStack matrices, MatrixStack.Entry entry, VertexConsumer vertices, float u, float v) {
        matrices.scale(state.damageMultiplier, 1, state.damageMultiplier);
        for (int i = 0; i < 360; i += 15) {
            drawPlane(entry, vertices, u, v);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
        }
        matrices.scale(1 / state.damageMultiplier, 1, 1 / state.damageMultiplier);
        matrices.translate(0, 1, 0);
    }

    private static void drawPlane(MatrixStack.Entry entry, VertexConsumer vertices, float u, float v) {
        drawVertex(entry, vertices, 0, 0.25F, 0, v);
        drawVertex(entry, vertices, 0, 0, 1, v);
        drawVertex(entry, vertices, 1, 0, 1, u);
        drawVertex(entry, vertices, 1, 0.25F, 0, u);
    }

    private static void drawVertex(MatrixStack.Entry entry, VertexConsumer vertices, float y, float z, float u, float v) {
        vertices.vertex(entry, 0.05F, y, z).color(Colors.WHITE).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0, -1, 0);
    }
}
