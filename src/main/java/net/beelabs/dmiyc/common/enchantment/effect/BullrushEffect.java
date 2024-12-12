package net.beelabs.dmiyc.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableFloat;

public record BullrushEffect(EnchantmentValueEffect bashes, EnchantmentValueEffect bashStrength,
                             EnchantmentValueEffect bashCooldown, EnchantmentValueEffect movementModifier) {
    public static final Codec<BullrushEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EnchantmentValueEffect.CODEC.fieldOf("bashes").forGetter(BullrushEffect::bashes),
                    EnchantmentValueEffect.CODEC.fieldOf("bashStrength").forGetter(BullrushEffect::bashStrength),
                    EnchantmentValueEffect.CODEC.fieldOf("bashCooldown").forGetter(BullrushEffect::bashCooldown),
                    EnchantmentValueEffect.CODEC.fieldOf("movementModifier").forGetter(BullrushEffect::movementModifier))
            .apply(instance, BullrushEffect::new));

    public static int getBashes(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        ItemStack stack = entity.getActiveItem();
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
            BullrushEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.BULLRUSH);
            if (effect != null) {
                mutableFloat.setValue(effect.bashes().apply(level, entity.getRandom(), mutableFloat.floatValue()));
            }
        });
        return MathHelper.floor(mutableFloat.floatValue());
    }

    public static float getBashStrength(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                BullrushEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.BULLRUSH);
                if (effect != null) {
                    mutableFloat.setValue(effect.bashStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return mutableFloat.floatValue();
    }

    public static int getBashCooldown(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        for (ItemStack stack : entity.getArmorItems()) {
            EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                BullrushEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.BULLRUSH);
                if (effect != null) {
                    mutableFloat.setValue(effect.bashCooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                }
            });
        }
        return MathHelper.floor(mutableFloat.floatValue() * 20);
    }

    public static int getBashMovementSpeedModifier(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat(0);
        if (entity instanceof PlayerEntity player) {
            ItemStack stack = entity.getActiveItem();
            BullrushComponent component = DMIYCComponents.BULLRUSH.get(player);
            if (!player.isOnGround() && component.isHasIncreasedAirMovement()) {
                EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
                    BullrushEffect effect = enchantment.value().effects().get(DMIYCEnchantmentEffects.BULLRUSH);
                    if (effect != null) {
                        mutableFloat.setValue(effect.movementModifier().apply(level, entity.getRandom(), mutableFloat.floatValue()));
                    }
                });
            }
        }
        return MathHelper.floor(mutableFloat.floatValue() * 20);
    }
}
