package net.beelabs.dmiyc.client.entity.renderer;

import net.beelabs.dmiyc.client.entity.renderer.renderstate.ClickableEntityRenderState;
import net.beelabs.dmiyc.client.entity.renderer.renderstate.PermafrostEntityRenderState;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.entity.ClickableEntity;
import net.beelabs.dmiyc.common.entity.PermafrostEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ClickableEntityRenderer extends ProjectileEntityRenderer<ClickableEntity, ClickableEntityRenderState> {
    private static final Identifier TEXTURE = DMIYC.id("textures/entity/projectiles/ice_shard.png");


    public ClickableEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public ClickableEntityRenderState createRenderState() {
        return new ClickableEntityRenderState();
    }

    @Override
    public void render(ClickableEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    protected Identifier getTexture(ClickableEntityRenderState state) {
        return null;
    }

    @Override
    public void updateRenderState(ClickableEntity entity, ClickableEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
    }

    @Override
    protected boolean canBeCulled(ClickableEntity entity) {
        return false;
    }
}
