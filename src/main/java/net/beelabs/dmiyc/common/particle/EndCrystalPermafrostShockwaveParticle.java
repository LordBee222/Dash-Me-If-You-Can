package net.beelabs.dmiyc.common.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class EndCrystalPermafrostShockwaveParticle extends SpriteBillboardParticle {
    public final SpriteProvider spriteProvider;


    protected EndCrystalPermafrostShockwaveParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);

        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;

        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(this.spriteProvider);

        int scaleAgeModifier = 1 + new Random().nextInt(10);

        this.scale *= 240.0f;
        this.maxAge = 7;
    }



    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.setSpriteForAge(this.spriteProvider);
        if (!this.dead) {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            if (this.age++ >= this.maxAge) {
                this.markDead();
            }
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.setSpriteForAge(this.spriteProvider);
        Quaternionf quaternionf = new Quaternionf();
        this.getRotator().setRotation(quaternionf, camera, tickDelta);
        if (this.angle != 0.0F) {
            quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
        }
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
    }



    /*
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.setSpriteForAge(spriteProvider);

        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternionf quaternion2;
        if (this.angle == 0.0F) {
            quaternion2 = camera.getRotation();
        } else {
            quaternion2 = new Quaternionf(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion2.rotateZ(i);
        }

        Vector3f vec3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vec3f.rotate(quaternion2);
        Vector3f[] Vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vector3f Vec3f2 = Vec3fs[k];
            Vec3f2.rotate(new Quaternionf().rotateXYZ((float) Math.toRadians(90f), 0f, 0f));
            Vec3f2.mul(j);
            Vec3f2.add(f, g, h);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = this.getBrightness(tickDelta);

        vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(l);
        vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(l);
        vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(l);
        vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(l);


        this.setSpriteForAge(spriteProvider);

        Vec3d cameraPos = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());

        // Fixed rotation quaternion for a horizontal shockwave
        Quaternionf fixedRotation = new Quaternionf().rotateXYZ(0, 0, 0); // No rotation, stays flat on XZ plane.

        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-1.0F, 0.0F, -1.0F), // Bottom-left
                new Vector3f(-1.0F, 0.0F, 1.0F),  // Top-left
                new Vector3f(1.0F, 0.0F, 1.0F),   // Top-right
                new Vector3f(1.0F, 0.0F, -1.0F)   // Bottom-right
        };

        float size = this.getSize(tickDelta);

        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.rotate(fixedRotation); // Apply fixed rotation
            vertex.mul(size);             // Scale to particle size
            vertex.add(f, g, h);          // Translate to particle position
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickDelta);

        vertexConsumer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
                .texture(maxU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
                .texture(maxU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
                .texture(minU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
                .texture(minU, maxV).color(red, green, blue, alpha).light(light);
    }
    */

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new EndCrystalPermafrostShockwaveParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
