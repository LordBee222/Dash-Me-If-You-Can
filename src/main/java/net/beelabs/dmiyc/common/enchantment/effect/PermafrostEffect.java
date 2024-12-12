package net.beelabs.dmiyc.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public record PermafrostEffect(EnchantmentValueEffect chargeTimeMultiplier) {
    public static final Codec<PermafrostEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EnchantmentValueEffect.CODEC.fieldOf("charge_time_multiplier").forGetter(PermafrostEffect::chargeTimeMultiplier))
            .apply(instance, PermafrostEffect::new));

    public static float getChargeTimeMultiplier(Random random, ItemStack stack) {
        MutableFloat value = new MutableFloat();
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            PermafrostEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.PERMAFROST);
            if (effect != null) {
                value.setValue(effect.chargeTimeMultiplier().apply(level, random, value.floatValue()));
            }
        });
        return value.floatValue();
    }

    public static int getBrimstoneDamage(float progress) {
        return (int) (6 * progress) * 2;
    }
}
