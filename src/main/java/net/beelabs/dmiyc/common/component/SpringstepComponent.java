package net.beelabs.dmiyc.common.component;

import net.beelabs.dmiyc.client.DMIYCClient;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.enchantment.effect.BullrushEffect;
import net.beelabs.dmiyc.common.enchantment.effect.SpringstepEffect;
import net.beelabs.dmiyc.common.event.MultiplyMovementSpeedEvent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.payload.BullrushPayload;
import net.beelabs.dmiyc.common.payload.SpringstepBouncePayload;
import net.beelabs.dmiyc.common.payload.SpringstepPayload;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class SpringstepComponent implements CommonTickingComponent, AutoSyncedComponent {
    private final PlayerEntity obj;
    private boolean shouldRefresh = false;
    private int cooldown = 0, lastCooldown = 0, jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

    private int maxJumps = 0;
    private boolean hasAirJump = false;

    public SpringstepComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        shouldRefresh = tag.getBoolean("ShouldRefresh");
        cooldown = tag.getInt("Cooldown");
        lastCooldown = tag.getInt("LastCooldown");
        jumpCooldown = tag.getInt("JumpCooldown");
        jumpsLeft = tag.getInt("JumpsLeft");
        ticksInAir = tag.getInt("TicksInAir");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("ShouldRefresh", shouldRefresh);
        tag.putInt("Cooldown", cooldown);
        tag.putInt("LastCooldown", lastCooldown);
        tag.putInt("JumpCooldown", jumpCooldown);
        tag.putInt("JumpsLeft", jumpsLeft);
        tag.putInt("TicksInAir", ticksInAir);
    }

    @Override
    public void tick() {
        int entityCooldown = SpringstepEffect.getChargeCooldown(obj);
        maxJumps = SpringstepEffect.getAirJumps(obj);
        hasAirJump = maxJumps > 0;
        if (hasAirJump) {
            if (!shouldRefresh) {
                if (obj.isOnGround()) {
                    shouldRefresh = true;
                }
            } else if (cooldown > 0) {
                cooldown--;
                if (cooldown == 0 && jumpsLeft < maxJumps) {
                    jumpsLeft++;
                    setCooldown(entityCooldown);
                }
            }
            if (shouldRefresh) {
                jumpsLeft = SpringstepEffect.getAirJumps(obj);
            }
            if (jumpCooldown > 0) {
                jumpCooldown--;
            }
            if (obj.isOnGround()) {
                ticksInAir = 0;
            } else {
                ticksInAir++;
            }
        } else {
            shouldRefresh = false;
            setCooldown(0);
            jumpCooldown = 0;
            jumpsLeft = 0;
            ticksInAir = 0;
        }
    }

    @Override
    public void clientTick() {
        tick();
        //DMIYC.LOGGER.info("Client hasAirJump? " + hasAirJump);
        // DMIYC.LOGGER.info("Client obj.jumping? " + obj.jumping);
        //DMIYC.LOGGER.info("Client canUse? " + canUse());
        if (hasAirJump){
            if (shouldBounceOnEntity()){
                bounceOnEntity();
                SpringstepBouncePayload.send();
            }
            if (canUse() && obj.jumping) {
                use();
                SpringstepPayload.send();
            }
        }
    }

    public void sync() {
        DMIYCComponents.SPRINGSTEP.sync(obj);
    }

    public int getCooldown() {
        return cooldown;
    }

    private void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        lastCooldown = cooldown;
    }

    public int getLastCooldown() {
        return lastCooldown;
    }

    public int getJumpsLeft() {
        return jumpsLeft;
    }

    public int getMaxJumps() {
        return maxJumps;
    }

    public boolean hasAirJump() {
        return hasAirJump;
    }

    public boolean shouldBounceOnEntity() {
        Box playerBox = this.obj.getBoundingBox();
        for (Entity entity : obj.getWorld().getOtherEntities(this.obj, playerBox)) {
            if (entity instanceof LivingEntity living) {
                if (living == obj) continue;
                Box entityBox = living.getBoundingBox();
                if (playerBox.intersects(entityBox)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void bounceOnEntity() {
        obj.setVelocity(obj.getVelocity().getX(), MultiplyMovementSpeedEvent.getJumpStrength(obj, SpringstepEffect.getAirJumpStrength(obj)), obj.getVelocity().getZ());
        if (obj.isSprinting()) {
            float rad = (float) Math.toRadians(obj.getYaw());
            obj.addVelocityInternal(new Vec3d(-MathHelper.sin(rad) * 0.2, 0, MathHelper.cos(rad) * 0.2));
        }
        obj.velocityDirty = true;
        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST, SoundCategory.NEUTRAL, 1F, 1f);
        if (obj.getWorld() instanceof ServerWorld serverWorld) serverWorld.spawnParticles(ParticleTypes.GUST_EMITTER_SMALL, obj.getParticleX(1), obj.getY(), obj.getParticleZ(1), 1, 0, 0, 0, 0);
        shouldRefresh = true;

    }


    public boolean canUse() {
        int entityJumpCooldown = SpringstepEffect.getJumpCooldown(obj);
        return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= (obj.getWorld().isClient ? entityJumpCooldown : entityJumpCooldown - 1) && !obj.isOnGround() && DMIYCUtil.isGroundedOrAirborne(obj);
    }

    public void use() {
        obj.setVelocity(obj.getVelocity().getX(), MultiplyMovementSpeedEvent.getJumpStrength(obj, SpringstepEffect.getAirJumpStrength(obj)), obj.getVelocity().getZ());
        if (obj.isSprinting()) {
            float rad = (float) Math.toRadians(obj.getYaw());
            obj.addVelocityInternal(new Vec3d(-MathHelper.sin(rad) * 0.2, 0, MathHelper.cos(rad) * 0.2));
        }
        obj.velocityDirty = true;
        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST, SoundCategory.NEUTRAL, 1F, 1f);
        if (obj.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.GUST_EMITTER_SMALL, obj.getParticleX(1), obj.getY(), obj.getParticleZ(1), 1, 0, 0, 0, 0);

            for (int i = 0; i < 8; i++) {
                // serverWorld.spawnParticles(ParticleTypes.CLOUD, obj.getParticleX(1), obj.getY(), obj.getParticleZ(1), 1, 0, 0, 0, 0.02);

            }
        }

        if (cooldown == 0 || jumpsLeft == maxJumps) {
            setCooldown(SpringstepEffect.getChargeCooldown(obj));
        }
        shouldRefresh = false;
        jumpCooldown = SpringstepEffect.getJumpCooldown(obj);
        jumpsLeft--;
    }

    public void setShouldRefresh(boolean shouldRefresh) {
        this.shouldRefresh = shouldRefresh;
    }

    public boolean isShouldRefresh() {
        return shouldRefresh;
    }

    public void reset() {
        setCooldown(SpringstepEffect.getChargeCooldown(obj));
        jumpsLeft = 0;
    }
}
