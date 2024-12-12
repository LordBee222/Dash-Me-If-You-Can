package net.beelabs.dmiyc.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.concurrent.atomic.AtomicBoolean;

public record VolleyEffect(EnchantmentValueEffect hitStrength,
                           EnchantmentValueEffect hitCooldown) {

    public static final Codec<VolleyEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EnchantmentValueEffect.CODEC.fieldOf("hit_strength").forGetter(VolleyEffect::hitStrength),
                    EnchantmentValueEffect.CODEC.fieldOf("hit_cooldown").forGetter(VolleyEffect::hitCooldown))
            .apply(instance, VolleyEffect::new));

    public static boolean hasVolley(LivingEntity entity) {
        ItemStack stack = entity.getMainHandStack();
        AtomicBoolean bl = new AtomicBoolean(false);
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            VolleyEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.VOLLEY);
            if (effect != null) bl.set(true);
        });
        return bl.get();
    }

    public static float getHitStrength(LivingEntity entity) {
        ItemStack stack = entity.getMainHandStack();
        MutableFloat mutableFloat = new MutableFloat(0);
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            VolleyEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.VOLLEY);
            if (effect != null) {
                mutableFloat.setValue(effect.hitStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
            }
        });
        return mutableFloat.floatValue();
    }

    public static float getHitCooldown(LivingEntity entity) {
        ItemStack stack = entity.getMainHandStack();
        MutableFloat mutableFloat = new MutableFloat(0);
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            VolleyEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.VOLLEY);
            if (effect != null) {
                mutableFloat.setValue(effect.hitCooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
            }
        });
        return mutableFloat.floatValue();
    }
}
