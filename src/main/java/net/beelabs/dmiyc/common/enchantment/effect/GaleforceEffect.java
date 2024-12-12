package net.beelabs.dmiyc.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public record GaleforceEffect (EnchantmentValueEffect cooldown, EnchantmentValueEffect groundStrength,
                               EnchantmentValueEffect airStrength) {
    public static final Codec<GaleforceEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EnchantmentValueEffect.CODEC.fieldOf("cooldown").forGetter(GaleforceEffect::cooldown),
                    EnchantmentValueEffect.CODEC.fieldOf("ground_strength").forGetter(GaleforceEffect::groundStrength),
                    EnchantmentValueEffect.CODEC.fieldOf("air_strength").forGetter(GaleforceEffect::airStrength))
            .apply(instance, GaleforceEffect::new));

    public static int getCooldown(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                GaleforceEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.GALEFORCE);
                if (effect != null) {
                    mutableFloat.setValue(effect.cooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return MathHelper.floor(mutableFloat.floatValue() * 20);
    }

    public static float getGroundStrength(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                GaleforceEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.GALEFORCE);
                if (effect != null) {
                    mutableFloat.setValue(effect.groundStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return mutableFloat.floatValue();
    }

    public static float getAirStrength(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                GaleforceEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.GALEFORCE);
                if (effect != null) {
                    mutableFloat.setValue(effect.airStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return mutableFloat.floatValue();
    }
}
