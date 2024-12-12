package net.beelabs.dmiyc.mixin.permafrost.strongFreeze;

import net.beelabs.dmiyc.common.DMIYC;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "animateDamage", at = @At("HEAD"), cancellable = true)
    private void stopTilt(float yaw, CallbackInfo ci){
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getRecentDamageSource().isOf(DamageTypes.FREEZE)) {
            DMIYC.LOGGER.info("STOPPED TILT");
            ci.cancel();
        }
    }
}
