package net.beelabs.dmiyc.mixin;

import net.beelabs.dmiyc.common.event.MultiplyMovementSpeedEvent;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private float multiplyMovementSpeed(float value) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return value * MultiplyMovementSpeedEvent.EVENT.invoker().multiply(1, entity.getWorld(), entity);
    }
}
