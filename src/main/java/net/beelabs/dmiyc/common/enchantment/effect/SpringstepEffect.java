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

public record SpringstepEffect (EnchantmentValueEffect airJumps, EnchantmentValueEffect airJumpStrength,
                                EnchantmentValueEffect chargeCooldown, EnchantmentValueEffect jumpCooldown) {
    public static final Codec<SpringstepEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EnchantmentValueEffect.CODEC.fieldOf("air_jumps").forGetter(SpringstepEffect::airJumps),
                    EnchantmentValueEffect.CODEC.fieldOf("air_jump_strength").forGetter(SpringstepEffect::airJumpStrength),
                    EnchantmentValueEffect.CODEC.fieldOf("charge_cooldown").forGetter(SpringstepEffect::chargeCooldown),
                    EnchantmentValueEffect.CODEC.fieldOf("jump_cooldown").forGetter(SpringstepEffect::jumpCooldown))
            .apply(instance, SpringstepEffect::new));

    public static int getAirJumps(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                SpringstepEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.SPRINGSTEP);
                if (effect != null) {
                    mutableFloat.setValue(effect.airJumps().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return MathHelper.floor(mutableFloat.floatValue());
    }

    public static float getAirJumpStrength(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                SpringstepEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.SPRINGSTEP);
                if (effect != null) {
                    mutableFloat.setValue(effect.airJumpStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return mutableFloat.floatValue();
    }

    public static int getChargeCooldown(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                SpringstepEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.SPRINGSTEP);
                if (effect != null) {
                    mutableFloat.setValue(effect.chargeCooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return MathHelper.floor(mutableFloat.floatValue() * 20);
    }

    public static int getJumpCooldown(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                SpringstepEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.SPRINGSTEP);
                if (effect != null) {
                    mutableFloat.setValue(effect.jumpCooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return MathHelper.floor(mutableFloat.floatValue() * 20);
    }
}
