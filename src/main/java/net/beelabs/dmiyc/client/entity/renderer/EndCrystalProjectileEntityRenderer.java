package net.beelabs.dmiyc.client.entity.renderer;

import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class EndCrystalProjectileEntityRenderer extends EntityRenderer<EndCrystalProjectileEntity, EndCrystalEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/end_crystal/end_crystal.png");
    private static final RenderLayer END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private final EndCrystalEntityModel model;

    public EndCrystalProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new EndCrystalEntityModel(context.getPart(EntityModelLayers.END_CRYSTAL));

    }

    @Override
    public void render(EndCrystalEntityRenderState endCrystalEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        matrixStack.translate(0.0F, -0.5F, 0.0F);
        this.model.setAngles(endCrystalEntityRenderState);
        this.model.render(matrixStack, vertexConsumerProvider.getBuffer(END_CRYSTAL), i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(endCrystalEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    public static float getYOffset(float f) {
        float g = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        g = (g * g + g) * 0.4F;
        return g - 1.4F;
    }

    public EndCrystalEntityRenderState createRenderState() {
        return new EndCrystalEntityRenderState();
    }

    public void updateRenderState(EndCrystalProjectileEntity endCrystalEntity, EndCrystalEntityRenderState endCrystalEntityRenderState, float f) {
        super.updateRenderState(endCrystalEntity, endCrystalEntityRenderState, f);
        endCrystalEntityRenderState.age = (float)endCrystalEntity.endCrystalAge + f;
        endCrystalEntityRenderState.baseVisible = false;
    }

    public boolean shouldRender(EndCrystalProjectileEntity endCrystalEntity, Frustum frustum, double d, double e, double f) {
        return super.shouldRender(endCrystalEntity, frustum, d, e, f);
    }
}
