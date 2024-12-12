package net.beelabs.dmiyc.common.init;

import com.mojang.serialization.MapCodec;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.enchantment.effect.*;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

import java.util.function.UnaryOperator;

public class DMIYCEnchantmentEffects {

    public static final ComponentType<BullrushEffect> BULLRUSH = register("bullrush", builder -> builder.codec(BullrushEffect.CODEC));
    public static final ComponentType<SpringstepEffect> SPRINGSTEP = register("springstep", builder -> builder.codec(SpringstepEffect.CODEC));
    public static final ComponentType<GaleforceEffect> GALEFORCE = register("galeforce", builder -> builder.codec(GaleforceEffect.CODEC));
    public static final ComponentType<VolleyEffect> VOLLEY = register("volley", builder -> builder.codec(VolleyEffect.CODEC));

    public static final ComponentType<PermafrostEffect> PERMAFROST = register("permafrost", builder -> builder.codec(PermafrostEffect.CODEC));
    public static final ComponentType<Unit> ALLOW_CROSSBOW_COOLDOWN_RELOADING = register("allow_crossbow_cooldown_reloading", builder -> builder.codec(Unit.CODEC));

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DMIYC.id(id), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerEnchantmentEffects() {
        DMIYC.LOGGER.info("Registering Mod Enchantment Effects for " + DMIYC.MOD_ID);
    }
}
