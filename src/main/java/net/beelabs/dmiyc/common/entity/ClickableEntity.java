package net.beelabs.dmiyc.common.entity;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponentTypes;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ClickableEntity extends PersistentProjectileEntity {
    public ClickableEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity player && player.getMainHandStack().isOf(Items.MACE)) {
            for (int i = 0; i < 15; i++)
                world.spawnParticles(ParticleTypes.CRIT, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
            return false;
        }

        this.discard();
        world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, World.ExplosionSourceType.MOB);
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
            }
            discard();
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.END_CRYSTAL);
    }
}
