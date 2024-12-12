package net.beelabs.dmiyc.common.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class FormedSnowflakeParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public FormedSnowflakeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.setPos(x, y, z);
        this.gravityStrength = 0.225F;
        this.velocityMultiplier = 1.0F;
        this.maxAge = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;

        //this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
       // this.setSpriteForAge(this.spriteProvider);
        super.tick();
        /*
        this.velocityX *= 0.95F;
        this.velocityY *= 0.9F;
        this.velocityZ *= 0.95F;

         */
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
            FormedSnowflakeParticle particle =  new FormedSnowflakeParticle(clientWorld, x, y, z, velX, velY, velZ, this.spriteProvider);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
