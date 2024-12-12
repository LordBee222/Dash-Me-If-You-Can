package net.beelabs.dmiyc.mixin.permafrost;

import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void freezeWhileHolding(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if (selected && stack.isOf(Items.CROSSBOW) && EnchantmentHelper.hasAnyEnchantmentsWith(stack, DMIYCEnchantmentEffects.PERMAFROST) && CrossbowItem.isCharged(stack) && entity instanceof LivingEntity livingEntity && livingEntity.canFreeze()){
            StrongFrozenComponent component = DMIYCComponents.STRONG_FROZEN.get(livingEntity);
            //component.setFrozenTicks(0);

            //livingEntity.setFrozenTicks(142);
        }
    }
}
