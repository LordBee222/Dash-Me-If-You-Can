package net.beelabs.dmiyc.common.event;

import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BullrushAirMovementSpeedEvent implements MultiplyMovementSpeedEvent {
    @Override
    public float multiply(float currentMultiplier, World world, LivingEntity living) {
        if (living instanceof PlayerEntity player) {
            BullrushComponent component = DMIYCComponents.BULLRUSH.get(player);
            if (!living.isOnGround() && component.isHasIncreasedAirMovement()) {
                return currentMultiplier * 5;
                //return Math.min(MAXIMUM_MULTIPLIER, currentMultiplier * 5);
            }
        }
        return Math.min(MAXIMUM_MULTIPLIER, currentMultiplier);
    }
}
