package net.beelabs.dmiyc.mixin.permafrost.strongFreeze;

import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.HeartType.class)
public class InGameHudHeartTypeMixin {

    @Inject(method = "fromPlayerState", at = @At("HEAD"), cancellable = true)
    private static void injectFromPlayerState(PlayerEntity player, CallbackInfoReturnable<InGameHud.HeartType> cir) {
        StrongFrozenComponent component = DMIYCComponents.STRONG_FROZEN.get(player);
        if (component.isFrozen()) {
            cir.setReturnValue(InGameHud.HeartType.FROZEN);
        }
    }
}
