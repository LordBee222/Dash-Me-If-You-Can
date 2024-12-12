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

public record SpringstepBouncePayload() implements CustomPayload {
    public static final CustomPayload.Id<SpringstepBouncePayload> ID = new Id<>(DMIYC.id("springstep_bounce"));
    public static final PacketCodec<PacketByteBuf, SpringstepBouncePayload> CODEC = PacketCodec.unit(new SpringstepBouncePayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new SpringstepBouncePayload());
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SpringstepBouncePayload> {
        @Override
        public void receive(SpringstepBouncePayload payload, ServerPlayNetworking.Context context) {
            SpringstepComponent airJumpComponent = DMIYCComponents.SPRINGSTEP.get(context.player());
            if (airJumpComponent.shouldBounceOnEntity() && airJumpComponent.hasAirJump()) {
                airJumpComponent.bounceOnEntity();
            }
        }


    }
}
