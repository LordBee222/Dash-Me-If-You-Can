package net.beelabs.dmiyc.common.entity;

import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class EndCrystalProjectileEntity extends ThrownEntity {
    public int endCrystalAge;
    private boolean isHit;

    public EndCrystalProjectileEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
        this.endCrystalAge = this.random.nextInt(100000);
        this.isHit = false;
    }

    public EndCrystalProjectileEntity(World world, LivingEntity owner) {
        this(DMIYCEntityTypes.END_CRYSTAL_PROJECTILE, world);
        this.setOwner(owner);
        this.intersectionChecked = true;
        this.endCrystalAge = this.random.nextInt(100000);
        this.isHit = false;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        //super.initDataTracker(builder);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isHit", this.isHit);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.isHit = nbt.getBoolean("isHit");
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return super.createSpawnPacket(entityTrackerEntry);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity player && player.getMainHandStack().isOf(Items.MACE)) {
            for (int i = 0; i < 15; i++) world.spawnParticles(ParticleTypes.END_ROD, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
            this.isHit = true;
            Vec3d velocity = player.getVelocity().add(player.getRotationVec(1.0F).multiply(2f));
            this.setVelocity(velocity.getX(), velocity.getY() + 0.5, velocity.getZ());
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.NEUTRAL, 1F, 1f);
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_MACE_SMASH_AIR, SoundCategory.NEUTRAL, 1F, 1f);
            this.velocityModified = true;
            player.getItemCooldownManager().set(player.getMainHandStack(), 40);
            return false;
        }
        this.discard();
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1F, 1f);

        world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, World.ExplosionSourceType.MOB);
        return true;
    }

    @Override
    protected double getGravity() {
        return 0.05;
    }


    @Override
    public void tick() {
        super.tick();
        ++this.endCrystalAge;
        this.extinguish();
        if (this.isHit && this.getWorld() instanceof ServerWorld world) {
            for (int i = 0; i < 4; i++){
                world.spawnParticles(ParticleTypes.ENCHANT, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return true;
    }

    protected RaycastContext.ShapeType getRaycastShapeType() {
        return RaycastContext.ShapeType.COLLIDER;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.getWorld().playSound(null, entityHitResult.getEntity().getX(), entityHitResult.getEntity().getY(), entityHitResult.getEntity().getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1F, 1f);
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
            }
            discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.getWorld().playSound(null, blockHitResult.getPos().getX(), blockHitResult.getPos().getY(), blockHitResult.getPos().getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1F, 1f);
                discard();
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
            }
        }
    }
}
