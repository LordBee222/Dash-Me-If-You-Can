package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.enchantment.effect.*;
import net.beelabs.dmiyc.common.tag.DMIYCTags;
import net.beelabs.dmiyc.data.provider.EnchantmentTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;

public class DMIYCEnchantments {
    public static final RegistryKey<Enchantment> BULLRUSH = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(DMIYC.MOD_ID, "bullrush"));
    public static final RegistryKey<Enchantment> SPRINGSTEP = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(DMIYC.MOD_ID, "springstep"));
    public static final RegistryKey<Enchantment> GALEFORCE = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(DMIYC.MOD_ID, "galeforce"));
    public static final RegistryKey<Enchantment> PERMAFROST = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(DMIYC.MOD_ID, "permafrost"));
    public static final RegistryKey<Enchantment> VOLLEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(DMIYC.MOD_ID, "volley"));


    public static void bootstrap(Registerable<Enchantment> registry) {
        var items = registry.getRegistryLookup(RegistryKeys.ITEM);
        register(registry, BULLRUSH, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(DMIYCTags.SHIELD_ENCHANTABLE),
                5,
                1,
                Enchantment.leveledCost(5, 8),
                Enchantment.leveledCost(25, 7),
                2,
                AttributeModifierSlot.MAINHAND,
                AttributeModifierSlot.OFFHAND))
                .addNonListEffect(
                        DMIYCEnchantmentEffects.BULLRUSH,
                        new BullrushEffect(
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(3F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(5F)))));



        register(registry, SPRINGSTEP, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(5, 8),
                        Enchantment.leveledCost(25, 7),
                        2,
                        AttributeModifierSlot.FEET))
                .addNonListEffect(
                        DMIYCEnchantmentEffects.SPRINGSTEP,
                        new SpringstepEffect(
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(3)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.45F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)))));


        register(registry, GALEFORCE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(5, 8),
                        Enchantment.leveledCost(25, 7),
                        2,
                        AttributeModifierSlot.LEGS))
                .addNonListEffect(
                        DMIYCEnchantmentEffects.GALEFORCE,
                        new GaleforceEffect(
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.25F, -0.5F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.1F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.8F)))));

        register(registry, PERMAFROST, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(5, 8),
                        Enchantment.leveledCost(25, 7),
                        2,
                        AttributeModifierSlot.MAINHAND,
                        AttributeModifierSlot.OFFHAND))
                .addEffect(
                        DMIYCEnchantmentEffects.ALLOW_CROSSBOW_COOLDOWN_RELOADING)
                .addNonListEffect(
                        DMIYCEnchantmentEffects.PERMAFROST,
                        new PermafrostEffect(
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F))
                        )));


        register(registry, VOLLEY, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(5, 8),
                        Enchantment.leveledCost(25, 7),
                        2,
                        AttributeModifierSlot.MAINHAND))
                .addNonListEffect(
                        DMIYCEnchantmentEffects.VOLLEY,
                        new VolleyEffect(
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.25F)),
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(30F))

                        )));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
