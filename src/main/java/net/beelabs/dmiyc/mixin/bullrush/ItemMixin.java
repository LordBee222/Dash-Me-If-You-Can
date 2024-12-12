package net.beelabs.dmiyc.mixin.bullrush;

import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void stoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir){
        if (stack.getItem() instanceof ShieldItem shield && user instanceof PlayerEntity player){
            BullrushComponent component = DMIYCComponents.BULLRUSH.get(player);
            if(component.isBashing()){
                component.postBash();
            }
        }
    }
}
