package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.component.BullrushComponent;
import net.beelabs.dmiyc.common.component.SpringstepComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.UUID;

public class DMIYCComponentTypes {
    public static final ComponentType<UUID> PERMAFROST_UUID = new ComponentType.Builder<UUID>().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC).build();
    public static final ComponentType<Integer> PERMAFROST_DAMAGE = new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, DMIYC.id("permafrost_uuid"), PERMAFROST_UUID);
        Registry.register(Registries.DATA_COMPONENT_TYPE, DMIYC.id("permafrost_damage"), PERMAFROST_DAMAGE);
    }
}
