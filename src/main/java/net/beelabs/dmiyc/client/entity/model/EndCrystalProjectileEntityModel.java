package net.beelabs.dmiyc.client.entity.model;

import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class EndCrystalProjectileEntityModel extends EntityModel<EndCrystalEntityRenderState> {
    private static final String OUTER_GLASS = "outer_glass";
    private static final String INNER_GLASS = "inner_glass";
    private static final float field_52906 = (float)Math.sin(Math.PI / 4);
    public final ModelPart outerGlass;
    public final ModelPart innerGlass;
    public final ModelPart cube;

    public EndCrystalProjectileEntityModel(ModelPart modelPart) {
        super(modelPart);
        this.outerGlass = modelPart.getChild("outer_glass");
        this.innerGlass = this.outerGlass.getChild("inner_glass");
        this.cube = this.innerGlass.getChild(EntityModelPartNames.CUBE);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float f = 0.875F;
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        ModelPartData modelPartData2 = modelPartData.addChild("outer_glass", modelPartBuilder, ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData modelPartData3 = modelPartData2.addChild("inner_glass", modelPartBuilder, ModelTransform.NONE.withScale(0.875F));
        modelPartData3.addChild(
                EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE.withScale(0.765625F)
        );
        return TexturedModelData.of(modelData, 64, 32);
    }
    @Override
    public void setAngles(EndCrystalEntityRenderState endCrystalEntityRenderState) {
        super.setAngles(endCrystalEntityRenderState);
        float f = endCrystalEntityRenderState.age * 3.0F;
        float g = EndCrystalEntityRenderer.getYOffset(endCrystalEntityRenderState.age) * 16.0F;
        this.outerGlass.pivotY += g / 2.0F;
        this.outerGlass.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(f).rotateAxis((float) (Math.PI / 3), field_52906, 0.0F, field_52906));
        this.innerGlass.rotate(new Quaternionf().setAngleAxis((float) (Math.PI / 3), field_52906, 0.0F, field_52906).rotateY(f * (float) (Math.PI / 180.0)));
        this.cube.rotate(new Quaternionf().setAngleAxis((float) (Math.PI / 3), field_52906, 0.0F, field_52906).rotateY(f * (float) (Math.PI / 180.0)));
    }
}
