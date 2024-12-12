package net.beelabs.dmiyc.common.event;

import net.beelabs.dmiyc.common.component.SpringstepComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EquipmentResetEvent  implements ServerEntityEvents.EquipmentChange {
    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if (equipmentSlot.isArmorSlot()) {
            if (EnchantmentHelper.hasAnyEnchantmentsWith(currentStack, DMIYCEnchantmentEffects.SPRINGSTEP)) {
                SpringstepComponent airJumpComponent = DMIYCComponents.SPRINGSTEP.getNullable(livingEntity);
                if (airJumpComponent != null) {
                    airJumpComponent.reset();
                    airJumpComponent.sync();
                }
            }
        }
    }
}
