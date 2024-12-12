package net.beelabs.dmiyc.common.payload;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.SpringstepComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SpringstepPayload() implements CustomPayload {
    public static final CustomPayload.Id<SpringstepPayload> ID = new Id<>(DMIYC.id("springstep"));
    public static final PacketCodec<PacketByteBuf, SpringstepPayload> CODEC = PacketCodec.unit(new SpringstepPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new SpringstepPayload());
    }

    public static void sendBounceOffEntity() {
        ClientPlayNetworking.send(new SpringstepPayload());
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SpringstepPayload> {
        @Override
        public void receive(SpringstepPayload payload, ServerPlayNetworking.Context context) {
            SpringstepComponent airJumpComponent = DMIYCComponents.SPRINGSTEP.get(context.player());
            if (airJumpComponent.hasAirJump() && airJumpComponent.canUse()) {
                airJumpComponent.use();
            }
        }


    }
}
