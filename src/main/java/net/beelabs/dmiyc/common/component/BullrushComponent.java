package net.beelabs.dmiyc.common.component;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.enchantment.effect.BullrushEffect;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.payload.BullrushPayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class BullrushComponent implements CommonTickingComponent, AutoSyncedComponent {
    private final PlayerEntity obj;
    private boolean bashing = false;
    private int bashingPostTicks = 0;
    private boolean hasIncreasedAirMovement = false;

    public BullrushComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    public void setBashing(boolean bashing) {
        this.bashing = bashing;
    }

    public void setBashingPostTicks(int bashingPostTicks) {
        this.bashingPostTicks = bashingPostTicks;
    }

    public PlayerEntity getObj() {
        return obj;
    }

    public boolean isBashing() {
        return bashing;
    }

    public int getBashingPostTicks() {
        return bashingPostTicks;
    }

    public boolean isHasIncreasedAirMovement() {
        return hasIncreasedAirMovement;
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        if (this.obj.getActiveItem().getItem() != Items.SHIELD) return;
        if (this.obj.getItemCooldownManager().isCoolingDown(new ItemStack(Items.SHIELD))) return;
        if (this.bashing) return;
        if (BullrushEffect.getBashes(obj) <= 0) return;
        boolean isLeftClickPressed = MinecraftClient.getInstance().options.attackKey.isPressed(), isRightClickPressed = MinecraftClient.getInstance().options.useKey.isPressed();
        if (isLeftClickPressed && isRightClickPressed) BullrushPayload.send();
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
        if (obj.getActiveItem().getItem() instanceof ShieldItem && this.bashing && BullrushEffect.getBashes(obj) > 0) {
            if (obj.getWorld() instanceof ServerWorld serverWorld) {
                List<LivingEntity> list = obj.getWorld().getEntitiesByClass(LivingEntity.class, obj.getBoundingBox().expand(1.5D), e -> true);
                if (list.isEmpty()) return;
                for (LivingEntity entityHit : list) {
                    if (entityHit != obj && entityHit.damage(serverWorld, obj.getDamageSources().mobAttack(obj), 4f)) {
                        entityHit.pushAwayFrom(obj);
                        float knockbackStrength = 3.0F;
                        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.NEUTRAL, 1F, 0.75f);
                        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.NEUTRAL, 1F, 1f);
                        if (entityHit.getWorld() instanceof ServerWorld server){
                            for (int duration = 0; duration < 10; ++duration) {
                                double d0 = entityHit.getRandom().nextGaussian() * 0.02D;
                                double d1 = entityHit.getRandom().nextGaussian() * 0.02D;
                                double d2 = entityHit.getRandom().nextGaussian() * 0.02D;
                                SimpleParticleType type = ParticleTypes.CRIT;
                                server.spawnParticles(type, entityHit.getParticleX(1.0D), entityHit.getRandomBodyY() + 1.0D, entityHit.getParticleZ(1.0D), 1, d0, d1, d2, 0.02D);
                            }
                        }
                        entityHit.takeKnockback(knockbackStrength, MathHelper.sin(entityHit.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(entityHit.getYaw() * ((float) Math.PI / 180F)));
                    }
                }
            }
            this.bashingPostTicks -= 1;
            if (this.bashingPostTicks <= 0) {
                this.postBash();
            }
        }
        if (this.obj.isOnGround() || this.obj.isTouchingWater()) {
            if (this.bashing) {
               this.postBash();
            }
            this.disableIncreasedMovement();
        }
    }

    public void postBash(){
        this.bashing = false;
        this.bashingPostTicks = 0;
        this.obj.getItemCooldownManager().set(new ItemStack(Items.SHIELD), 100);
        obj.clearActiveItem();
    }

    public void disableIncreasedMovement(){
        this.hasIncreasedAirMovement = false;
        DMIYCComponents.BULLRUSH.sync(this.obj);
    }

    public void use() {
        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.BLOCK_GRASS_HIT, SoundCategory.NEUTRAL, 1F, 1f);
        this.hasIncreasedAirMovement = true;
        Vec3d velocity = this.obj.getVelocity().add(this.obj.getRotationVec(1.0F).multiply(1.25f));
        this.obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
        this.obj.velocityModified = true;
        this.obj.fallDistance = 0;
        this.bashingPostTicks = 20;
        this.bashing = true;
        DMIYCComponents.BULLRUSH.sync(this.obj);
    }

    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
        bashing = tag.getBoolean("bashing");
        bashingPostTicks = tag.getInt("bashing_post_ticks");
        hasIncreasedAirMovement = tag.getBoolean("increased_movement");

    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
        tag.putBoolean("bashing", bashing);
        tag.putInt("bashing_post_ticks", bashingPostTicks);
        tag.putBoolean("increased_movement", hasIncreasedAirMovement);

    }
}
