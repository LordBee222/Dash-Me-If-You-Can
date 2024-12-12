package net.beelabs.dmiyc.data.provider;

import net.beelabs.dmiyc.common.init.DMIYCEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
    public static final List<Identifier> DMIYC_ALL_ENCHANTMENTS = new ArrayList<>();

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture.completeAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        FabricTagProvider<Enchantment>.FabricTagBuilder nonTreasure = getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE);
        FabricTagProvider<Enchantment>.FabricTagBuilder tooltipOrder = getOrCreateTagBuilder(EnchantmentTags.TOOLTIP_ORDER);
        DMIYC_ALL_ENCHANTMENTS.forEach(nonTreasure::addOptional);
        DMIYC_ALL_ENCHANTMENTS.forEach(tooltipOrder::addOptional);

        getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
                .addOptional(DMIYCEnchantments.BULLRUSH)
                .addOptional(DMIYCEnchantments.SPRINGSTEP);
    }
}
