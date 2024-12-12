package net.beelabs.dmiyc.common.util;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class DMIYCUtil {
    public static final Set<KeyBinding> VANILLA_AND_DMIYC_BINDINGS = new HashSet<>();

    public static boolean allowDuplicateKeybinding(KeyBinding keyBinding) {
        if (keyBinding == null) return false;
        return VANILLA_AND_DMIYC_BINDINGS.contains(keyBinding);
    }

    public static boolean isGroundedOrAirborne(LivingEntity living, boolean allowWater) {
        if (living instanceof PlayerEntity player && player.getAbilities().flying) {
            return false;
        }
        if (!allowWater) {
            if (living.isTouchingWater() || living.isSwimming()) {
                return false;
            }
        }
        return !living.isGliding() && !living.hasVehicle() && !living.isClimbing();
    }

    public static boolean shouldHurt(Entity attacker, Entity hitEntity) {
        if (attacker == null || hitEntity == null) {
            return true;
        }
        if (attacker == hitEntity || attacker.getVehicle() == hitEntity) {
            return false;
        }
        if (hitEntity instanceof PlayerEntity hitPlayer && attacker instanceof PlayerEntity attackingPlayer) {
            return attackingPlayer.shouldDamagePlayer(hitPlayer);
        } else if (hitEntity instanceof Ownable ownable) {
            return shouldHurt(attacker, ownable.getOwner());
        }
        return true;
    }

    public static boolean isGroundedOrAirborne(LivingEntity living) {
        return isGroundedOrAirborne(living, false);
    }

}
