package net.beelabs.dmiyc.mixin.permafrost;

import net.beelabs.dmiyc.common.enchantment.effect.BullrushEffect;
import net.beelabs.dmiyc.common.enchantment.effect.PermafrostEffect;
import net.beelabs.dmiyc.common.init.DMIYCComponentTypes;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), cancellable = true)
    private void enchancement$brimstone(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            PermafrostEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.PERMAFROST);
            if (effect != null) {
                MutableText hearts = Texts.bracketed(Text.literal("❤").append(" x" + 10 / 2).formatted(Formatting.RED)).formatted(Formatting.DARK_RED);
                tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(" ").append(Texts.bracketed(Text.translatable("enchantment.enchancement.brimstone").append(" ").append(hearts))));
                ci.cancel();
            }
        });
    }

}
