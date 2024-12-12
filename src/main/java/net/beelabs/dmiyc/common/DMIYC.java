package net.beelabs.dmiyc.common;

import com.mojang.serialization.MapCodec;
import net.beelabs.dmiyc.common.event.BullrushAirMovementSpeedEvent;
import net.beelabs.dmiyc.common.event.MultiplyMovementSpeedEvent;
import net.beelabs.dmiyc.common.init.DMIYCComponentTypes;
import net.beelabs.dmiyc.common.init.DMIYCEnchantmentEffects;
import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.beelabs.dmiyc.common.init.DMIYCParticles;
import net.beelabs.dmiyc.common.particle.EndCrystalPermafrostShockwaveParticle;
import net.beelabs.dmiyc.common.particle.FormedSnowflakeParticle;
import net.beelabs.dmiyc.common.payload.BullrushPayload;
import net.beelabs.dmiyc.common.payload.SpringstepBouncePayload;
import net.beelabs.dmiyc.common.payload.SpringstepPayload;
import net.beelabs.dmiyc.common.payload.VolleyPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMIYC implements ModInitializer {
	public static final String MOD_ID = "dmiyc";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		initPayloads();
		initEvents();
		DMIYCEnchantmentEffects.registerEnchantmentEffects();

		DMIYCComponentTypes.init();
		DMIYCEntityTypes.init();
		DMIYCParticles.init();
		ParticleFactoryRegistry.getInstance().register(DMIYCParticles.END_CRYSTAL_PERMAFROST_SHOCKWAVE, EndCrystalPermafrostShockwaveParticle.DefaultFactory::new);
		ParticleFactoryRegistry.getInstance().register(DMIYCParticles.FORMED_SNOWFLAKE, FormedSnowflakeParticle.Factory::new);

	}

	private void initPayloads() {
		PayloadTypeRegistry.playC2S().register(BullrushPayload.ID, BullrushPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(BullrushPayload.ID, new BullrushPayload.Receiver());

		PayloadTypeRegistry.playC2S().register(SpringstepPayload.ID, SpringstepPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SpringstepPayload.ID, new SpringstepPayload.Receiver());

		PayloadTypeRegistry.playC2S().register(SpringstepBouncePayload.ID, SpringstepBouncePayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SpringstepBouncePayload.ID, new SpringstepBouncePayload.Receiver());

		PayloadTypeRegistry.playC2S().register(VolleyPayload.ID, VolleyPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(VolleyPayload.ID, new VolleyPayload.Receiver());
	}

	private void initEvents(){
		MultiplyMovementSpeedEvent.EVENT.register(new BullrushAirMovementSpeedEvent());
	}





	public static Identifier id(String value) {
		return Identifier.of(MOD_ID, value);
	}
}