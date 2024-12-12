package net.beelabs.dmiyc.common.entity;

import net.beelabs.dmiyc.common.init.DMIYCComponentTypes;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.beelabs.dmiyc.common.init.DMIYCParticles;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermafrostEntity extends PersistentProjectileEntity {
    public static final ItemStack BRIMSTONE_STACK;
    public float maxY = 0;
    private final Set<Entity> hitEntities = new HashSet<>(), killedEntities = new HashSet<>();


    static {
        BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
        BRIMSTONE_STACK.set(DMIYCComponentTypes.PERMAFROST_DAMAGE, Integer.MAX_VALUE);
    }

    public static final TrackedData<Float> DAMAGE = DataTracker.registerData(PermafrostEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> FORCED_PITCH = DataTracker.registerData(PermafrostEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(PermafrostEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public int distanceTraveled = 0, ticksExisted = 0;

    public PermafrostEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public PermafrostEntity(World world, LivingEntity owner, @Nullable ItemStack shotFrom) {
        super(DMIYCEntityTypes.PERMAFROST, owner, world, ItemStack.EMPTY, shotFrom);
        setPosition(owner.getX(), owner.getEyeY() - 0.3, owner.getZ());
    }

    @Override
    public void tick() {
        if (isCritical()) setCritical(false);
        setVelocity(Vec3d.ZERO);
        this.castRay();
        if (!getWorld().isClient) discard();
    }

    private void castRay() {
        Vec3d start = getPos(), end = start.add(getRotationVector());
        maxY = 0;
        while (maxY < 100) {
            maxY++;
            BlockHitResult hitResult = getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                spawnShards(hitResult.getPos(), false);
                break;
            }
            this.scanForEntities(hitResult);
            this.displayParticles(hitResult, start);
            start = end;
            end = start.add(getRotationVector());
            if (maxY == 100) spawnShards(hitResult.getPos(), false);
        }
    }


    private void displayParticles(BlockHitResult hitResult, Vec3d start) {
        Vec3d hitPos = hitResult.getPos();
        Vec3d rayDirection = hitPos.subtract(start).normalize();
        double distance = start.distanceTo(hitPos);
        if (getWorld() instanceof ServerWorld serverWorld) {
            for (double i = 0; i < distance; i += 0.7) {
                Vec3d particlePos = start.add(rayDirection.multiply(i));
                serverWorld.spawnParticles(DMIYCParticles.FORMED_SNOWFLAKE, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.1, 0.1, 0.1, 0.05);
                serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.BLUE_ICE, 1)), particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }

    private void scanForEntities(BlockHitResult hitResult) {
        getWorld().getOtherEntities(this.getOwner(), Box.from(hitResult.getPos()).expand(0.5), EntityPredicates.EXCEPT_SPECTATOR.and(entity -> canEntityBeHit(this.getOwner(), entity))).forEach(this::onHitEntity);
    }

    public void onHitEntity(Entity entity) {
        if (this.getWorld() instanceof ServerWorld server) {
            entity.damage(server, entity.getDamageSources().arrow(this, this.getOwner()), 8);
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.NEUTRAL, 1F, 1f);
            hitEntities.add(entity);
            if (entity instanceof LivingEntity living){
                DMIYCComponents.STRONG_FROZEN.get(living).addFrozenTicks(160);
                if (living.isDead()) killedEntities.add(living);
            }
            boolean frozen = entity instanceof EndCrystalProjectileEntity;
            this.spawnShards(new Vec3d(entity.getX(), entity.getEyeY() + 1, entity.getZ()), frozen);
            if (frozen) this.createFrozenExplosion((EndCrystalProjectileEntity) entity);
        }
    }

    private void spawnShards(Vec3d pos, boolean freezing) {
        if (this.getWorld() instanceof ServerWorld server) {
            server.spawnParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 0);
            for (int i = 0; i < (freezing ? 140 : 70); i++) this.getWorld().spawnEntity(IceShardEntity.createIceShard(this.getWorld(), this.getOwner(), pos, freezing));
        }
    }

    private void createFrozenExplosion(EndCrystalProjectileEntity crystal) {
        if (this.getWorld() instanceof ServerWorld server) {
            List<LivingEntity> entities = crystal.getWorld().getEntitiesByClass(LivingEntity.class, new Box(crystal.getX() - 20, crystal.getY() - 20, crystal.getZ() - 20, crystal.getX() + 20, crystal.getY() + 20, crystal.getZ() + 20), e -> true);
            for (LivingEntity entity : entities){
                HitResult hitResult = entity.getWorld().raycast(new RaycastContext(crystal.getPos(), entity.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, crystal));
                if (hitResult.getType() != HitResult.Type.BLOCK){
                    DMIYCComponents.STRONG_FROZEN.get(entity).addFrozenTicks(100);
                }
            }
            server.spawnParticles(DMIYCParticles.END_CRYSTAL_PERMAFROST_SHOCKWAVE, crystal.getX(), crystal.getY(), crystal.getZ(), 1, 0, 0, 0, 0);
            for (int i = 0; i < 1000; i++) server.spawnParticles(DMIYCParticles.FORMED_SNOWFLAKE, crystal.getX(), crystal.getY(), crystal.getZ(), 1, 1.25, 1.25, 1.25, 0.625);
        }
    }


    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setDamage(nbt.getFloat("Damage"));
        dataTracker.set(FORCED_PITCH, nbt.getFloat("ForcedPitch"));
        dataTracker.set(FORCED_YAW, nbt.getFloat("ForcedYaw"));
        distanceTraveled = nbt.getInt("DistanceTraveled");
        ticksExisted = nbt.getInt("TicksExisted");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Damage", (float) getDamage());
        nbt.putFloat("ForcedPitch", getPitch());
        nbt.putFloat("ForcedYaw", getYaw());
        nbt.putInt("DistanceTraveled", distanceTraveled);
        nbt.putInt("TicksExisted", ticksExisted);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DAMAGE, 0F);
        builder.add(FORCED_PITCH, 0F);
        builder.add(FORCED_YAW, 0F);
    }

    @Override
    public float getPitch() {
        return dataTracker.get(FORCED_PITCH);
    }

    @Override
    public float getYaw() {
        return dataTracker.get(FORCED_YAW);
    }

    @Override
    public void setDamage(double damage) {
        dataTracker.set(DAMAGE, (float) damage);
    }

    @Override
    public double getDamage() {
        return dataTracker.get(DAMAGE);
    }

    public float getDamageMultiplier(int distanceTraveled) {
        if (distanceTraveled < 8) {
            return MathHelper.lerp(distanceTraveled / 8F, 0.25F, 1);
        }
        return Math.min(2, MathHelper.lerp((distanceTraveled - 8) / 200F, 1F, 2F));
    }

    private boolean canEntityBeHit(Entity owner, Entity entity) {
        if (entity instanceof LivingEntity) {
            return !hitEntities.contains(entity) && entity.isAlive() && DMIYCUtil.shouldHurt(owner, entity);
        } else if (entity instanceof EndCrystalProjectileEntity) {
            return !hitEntities.contains(entity);
        }
        return false;
    }
}
