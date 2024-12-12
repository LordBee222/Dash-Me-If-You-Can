package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.*;
import net.beelabs.dmiyc.common.enchantment.effect.BullrushEffect;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.function.UnaryOperator;

public class DMIYCComponents implements EntityComponentInitializer {
    public static final ComponentKey<BullrushComponent> BULLRUSH = ComponentRegistry.getOrCreate(DMIYC.id("bullrush"), BullrushComponent.class);
   public static final ComponentKey<SpringstepComponent> SPRINGSTEP = ComponentRegistry.getOrCreate(DMIYC.id("springstep"), SpringstepComponent.class);
   // public static final ComponentKey<GaleforceComponent> GALEFORCE = ComponentRegistry.getOrCreate(DMIYC.id("galeforce"), GaleforceComponent.class);
   public static final ComponentKey<StrongFrozenComponent> STRONG_FROZEN = ComponentRegistry.getOrCreate(DMIYC.id("strong_frozen"), StrongFrozenComponent.class);
    public static final ComponentKey<VolleyComponent> VOLLEY = ComponentRegistry.getOrCreate(DMIYC.id("volley"), VolleyComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(BULLRUSH, BullrushComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(SPRINGSTEP, SpringstepComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerFor(LivingEntity.class, STRONG_FROZEN, StrongFrozenComponent::new);
       // registry.registerForPlayers(STRONG_FROZEN, StrongFrozenComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(VOLLEY, VolleyComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
