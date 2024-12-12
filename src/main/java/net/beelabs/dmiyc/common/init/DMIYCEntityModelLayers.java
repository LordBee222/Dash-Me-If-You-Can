package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DMIYCEntityModelLayers {
    public static final EntityModelLayer END_CRYSTAL_PROJECTILE =
            new EntityModelLayer(DMIYC.id("end_crystal_projectile"), "main");

}
