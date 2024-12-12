package net.beelabs.dmiyc.common.entity;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.beelabs.dmiyc.common.init.DMIYCParticles;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceShardEntity extends PersistentProjectileEntity {
    private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, Items.ICE.getDefaultStack());
    public static final TrackedData<Boolean> FREEZING = DataTracker.registerData(IceShardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public IceShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setSound(this.getHitSound());
        setDamage(4);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public static IceShardEntity createIceShard(World world, Entity owner, Vec3d pos, boolean freezing){
        float vel = freezing ? 1f : 0.5f;
        IceShardEntity shard = DMIYCEntityTypes.ICE_SHARD.create(world, SpawnReason.TRIGGERED);
        shard.setOwner(owner);
        shard.setPos(pos.getX(), pos.getY(), pos.getZ());
        shard.updateTrackedPosition(pos.getX(), pos.getY(), pos.getZ());
        shard.setVelocity(shard.random.nextGaussian() * vel, shard.random.nextGaussian() * vel, shard.random.nextGaussian() * vel);
        shard.setFreezing(freezing);
        return shard;
    }

    protected void onTargetHit(Entity entity) {
        if (entity.canFreeze()) {
            entity.setFrozenTicks(400);
        }
    }

    public void addParticles() {
        ((ServerWorld) getWorld()).spawnParticles(getParticleEffect(), getX(), getY(), getZ(), 8, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ICE);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.AMETHYST_SHARD);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_GLASS_BREAK;
    }

    @Override
    public void tick() {
        this.pickupType = PickupPermission.DISALLOWED;
        super.tick();
        if (this.isInGround()) {
            if (!getWorld().isClient) {
                playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
                addParticles();
                discard();
            }
        }

        if (!getWorld().isClient) {
            if (age > 400) {
                playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
                addParticles();
                discard();
            }
        }

        if (this.isFreezing() && this.getWorld() instanceof ServerWorld world) {
            world.spawnParticles(DMIYCParticles.FORMED_SNOWFLAKE, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
            world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.BLUE_ICE, 1)), this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (getWorld() instanceof ServerWorld serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = getOwner();
            if (DMIYCUtil.shouldHurt(owner, entity) && entity.damage(serverWorld, entity.getDamageSources().arrow(this, owner), (float) getDamage())) {
                onTargetHit(entity);
                playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
                addParticles();
                if (this.isFreezing()) {
                    StrongFrozenComponent component = DMIYCComponents.STRONG_FROZEN.get(entity);
                    component.addFrozenTicks(40);
                }
                discard();
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockState state = getWorld().getBlockState(blockHitResult.getBlockPos());
        state.onProjectileHit(getWorld(), state, blockHitResult, this);
        if (!getWorld().isClient) {
            playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
            addParticles();
            discard();
        }
    }

    protected ParticleEffect getParticleEffect() {
        return PARTICLE;
    }

    public boolean isFreezing() {
        return this.getDataTracker().get(FREEZING);
    }

    public void setFreezing(boolean freezing) {
        this.getDataTracker().set(FREEZING, freezing);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("freezing")) this.setFreezing(nbt.getBoolean("freezing"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("freezing", this.isFreezing());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FREEZING, false);
    }
}
