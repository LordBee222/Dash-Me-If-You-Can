package net.beelabs.dmiyc.data.provider;

import com.mojang.serialization.Lifecycle;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.init.DMIYCEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.concurrent.CompletableFuture;

public class DynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public DynamicRegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
     //   entries.addAll(registries.getOrThrow(RegistryKeys.ENCHANTMENT));
        DMIYCEnchantments.bootstrap(createRegisterable(registries, entries));

    }

    @Override
    public String getName() {
        return "DMIYC Dynamic Registry Provider";
    }

    private static <T> Registerable<T> createRegisterable(RegistryWrapper.WrapperLookup registries, Entries entries) {
        return new Registerable<>() {
            @Override
            public RegistryEntry.Reference<T> register(RegistryKey<T> key, T value, Lifecycle lifecycle) {
                return (RegistryEntry.Reference<T>) entries.add(key, value);
            }

            @Override
            public <S> RegistryEntryLookup<S> getRegistryLookup(RegistryKey<? extends Registry<? extends S>> registryRef) {
                return registries.getOrThrow(registryRef);
            }
        };
    }
}
