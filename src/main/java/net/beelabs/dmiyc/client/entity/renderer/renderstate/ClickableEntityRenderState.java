package net.beelabs.dmiyc.client.entity.renderer.renderstate;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;

public class ClickableEntityRenderState extends ProjectileEntityRenderState {
    public int ticksExisted = 0;
    public float damage = 0, damageMultiplier = 0;
    public boolean hi = false;
    public int distanceTraveled = 0;
}
