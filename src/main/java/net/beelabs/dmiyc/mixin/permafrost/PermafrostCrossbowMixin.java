package net.beelabs.dmiyc.mixin.permafrost;

import net.beelabs.dmiyc.common.entity.PermafrostEntity;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerEntity.class, HostileEntity.class})
public class PermafrostCrossbowMixin {
    @Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"), cancellable = true)
    private void dmiyc$brimstone(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, DMIYCEnchantmentEffects.PERMAFROST)) {
            cir.setReturnValue(PermafrostEntity.BRIMSTONE_STACK.copy());
        }
    }

}
