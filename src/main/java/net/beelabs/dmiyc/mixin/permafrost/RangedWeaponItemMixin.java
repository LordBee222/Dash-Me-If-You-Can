package net.beelabs.dmiyc.mixin.permafrost;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.enchantment.effect.PermafrostEffect;
import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.beelabs.dmiyc.common.entity.PermafrostEntity;
import net.beelabs.dmiyc.common.init.DMIYCComponentTypes;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {

    @ModifyReturnValue(method = "createArrowEntity", at = @At("RETURN"))
    private ProjectileEntity dmiyc$brimstone(ProjectileEntity original, World world, LivingEntity shooter, ItemStack weaponStack) {
        // Create a mutable container for the return value
        AtomicReference<ProjectileEntity> result = new AtomicReference<>(original);

        EnchantmentHelper.forEachEnchantment(weaponStack, (enchantment, level) -> {
            PermafrostEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.PERMAFROST);
            if (effect != null) {
                int damage = 10;
                if (world instanceof ServerWorld serverWorld) {
                    StrongFrozenComponent component = DMIYCComponents.STRONG_FROZEN.get(shooter);
                    component.addFrozenTicks(80);
                    shooter.damage(serverWorld, shooter.getDamageSources().generic(), 3);
                }
                PermafrostEntity brimstone = new PermafrostEntity(world, shooter, weaponStack);
                brimstone.setDamage(damage);

                // Update the return value
                shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.NEUTRAL, 1F, 1f);
                result.set(brimstone);
            }
        });

        // Return the result (modified or original)
        return result.get();
    }


    @WrapOperation(method = "method_61659", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"))
    private void dmiyc$brimstone(RangedWeaponItem instance, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target, Operation<Void> original) {
        if (projectile instanceof PermafrostEntity brimstone) {
            brimstone.getDataTracker().set(PermafrostEntity.FORCED_PITCH, shooter.getPitch());
            brimstone.getDataTracker().set(PermafrostEntity.FORCED_YAW, shooter.getHeadYaw() + yaw);
            if (yaw != 0) {
                brimstone.setDamage(brimstone.getDamage() / 2);
            }
        }
        original.call(instance, shooter, projectile, index, speed, divergence, yaw, target);
    }

    @Inject(method = "shootAll", at = @At("TAIL"))
    private void dmiyc$brimstone(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci) {
        int damage = 10;
            if (shooter instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, (int) (CrossbowItem.getPullTime(stack, shooter) * (damage / 12F)));
            }
    }
}
