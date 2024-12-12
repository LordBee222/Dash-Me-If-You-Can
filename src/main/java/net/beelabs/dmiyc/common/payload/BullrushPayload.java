package net.beelabs.dmiyc.common.payload;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public record BullrushPayload(Float x) implements CustomPayload {
    public static final CustomPayload.Id<BullrushPayload> ID = new Id<>(DMIYC.id("bullrush"));
    public static final PacketCodec<PacketByteBuf, BullrushPayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, BullrushPayload::x, BullrushPayload::new);


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new BullrushPayload(1f));
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BullrushPayload> {
        @Override
        public void receive(BullrushPayload payload, ServerPlayNetworking.Context context) {
            BullrushComponent component = DMIYCComponents.BULLRUSH.get(context.player());
            PlayerEntity player = component.getObj();
            if (player != null && player.getActiveItem().getItem() instanceof ShieldItem shield) component.use();
        }
    }
}
