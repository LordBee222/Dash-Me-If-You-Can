package net.beelabs.dmiyc.data;

import net.beelabs.dmiyc.common.init.DMIYCEnchantments;
import net.beelabs.dmiyc.data.provider.DynamicRegistryProvider;
import net.beelabs.dmiyc.data.provider.EnchantmentTagProvider;
import net.beelabs.dmiyc.data.provider.ItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class DMIYCDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(EnchantmentTagProvider::new);
		pack.addProvider(ItemTagProvider::new);
		pack.addProvider(DynamicRegistryProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, DMIYCEnchantments::bootstrap);
	}
}
