package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DMIYCParticles {
    public static final SimpleParticleType END_CRYSTAL_PERMAFROST_SHOCKWAVE = Registry.register(Registries.PARTICLE_TYPE,
            DMIYC.id("end_crystal_permafrost_shockwave"), FabricParticleTypes.simple(true));

    public static final SimpleParticleType FORMED_SNOWFLAKE = Registry.register(Registries.PARTICLE_TYPE,
            DMIYC.id("formed_snowflake"), FabricParticleTypes.simple(true));

    public static void init(){
        DMIYC.LOGGER.info("Registering Particles for " + DMIYC.MOD_ID);
    }
}
