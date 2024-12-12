package net.beelabs.dmiyc.mixin.springstep;

import net.beelabs.dmiyc.common.component.SpringstepComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.entity.ExplodeEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExplodeEnchantmentEffect.class)
public abstract class ExplodeEnchantmentEffectMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    private void onApply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos, CallbackInfo ci){
        if (user instanceof PlayerEntity player){
            SpringstepComponent component = DMIYCComponents.SPRINGSTEP.get(player);
            if (!component.isShouldRefresh()){
                component.setShouldRefresh(true);
                DMIYCComponents.SPRINGSTEP.sync(player);
            }
        }
    }
}
