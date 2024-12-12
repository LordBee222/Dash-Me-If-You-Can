package net.beelabs.dmiyc.common.payload;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.component.VolleyComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VolleyPayload(Float x) implements CustomPayload {
    public static final CustomPayload.Id<VolleyPayload> ID = new Id<>(DMIYC.id("volley"));
    public static final PacketCodec<PacketByteBuf, VolleyPayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, VolleyPayload::x, VolleyPayload::new);


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new VolleyPayload(1f));
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<VolleyPayload> {
        @Override
        public void receive(VolleyPayload payload, ServerPlayNetworking.Context context) {
            VolleyComponent component = DMIYCComponents.VOLLEY.get(context.player());
            PlayerEntity player = component.getObj();
            if (player != null &&
                    player.getMainHandStack().getItem() == Items.MACE &&
                    !player.getItemCooldownManager().isCoolingDown(new ItemStack(Items.MACE)) &&
                    player.getOffHandStack().getItem() == Items.END_CRYSTAL) {
                component.throwCrystal();
            }
        }
    }
}
