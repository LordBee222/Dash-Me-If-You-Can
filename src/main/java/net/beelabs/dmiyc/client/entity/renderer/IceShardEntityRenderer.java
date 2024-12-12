package net.beelabs.dmiyc.client.entity.renderer;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.entity.IceShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;

public class IceShardEntityRenderer extends ProjectileEntityRenderer<IceShardEntity, ProjectileEntityRenderState> {
    private static final Identifier TEXTURE = DMIYC.id("textures/entity/projectiles/ice_shard.png");


    public IceShardEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTexture(ProjectileEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public ProjectileEntityRenderState createRenderState() {
        return new ProjectileEntityRenderState();
    }
}
