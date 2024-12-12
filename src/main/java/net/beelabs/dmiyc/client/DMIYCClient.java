package net.beelabs.dmiyc.client;

import net.beelabs.dmiyc.client.entity.renderer.ClickableEntityRenderer;
import net.beelabs.dmiyc.client.entity.renderer.EndCrystalProjectileEntityRenderer;
import net.beelabs.dmiyc.client.entity.renderer.IceShardEntityRenderer;
import net.beelabs.dmiyc.client.entity.renderer.PermafrostEntityRenderer;
import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.beelabs.dmiyc.common.entity.IceShardEntity;
import net.beelabs.dmiyc.common.init.DMIYCEntityModelLayers;
import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class DMIYCClient implements ClientModInitializer {
    public static final KeyBinding GALEFORCE_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + DMIYC.MOD_ID + ".galeforce", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories." + DMIYC.MOD_ID)));
    public static final KeyBinding SPRINGSTEP_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + DMIYC.MOD_ID + ".springstep", GLFW.GLFW_KEY_SPACE, "key.categories." + DMIYC.MOD_ID)));


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(DMIYCEntityTypes.PERMAFROST, PermafrostEntityRenderer::new);
        EntityRendererRegistry.register(DMIYCEntityTypes.CLICKABLE_ENTITY, ClickableEntityRenderer::new);

        EntityRendererRegistry.register(DMIYCEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
        EntityRendererRegistry.register(DMIYCEntityTypes.END_CRYSTAL_PROJECTILE, EndCrystalProjectileEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(DMIYCEntityModelLayers.END_CRYSTAL_PROJECTILE, EndCrystalEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(DMIYCEntityTypes.END_CRYSTAL_PROJECTILE, EndCrystalProjectileEntityRenderer::new);
    }



    private static KeyBinding registerKeyBinding(Supplier<KeyBinding> supplier) {
        KeyBinding keyBinding = supplier.get();
        DMIYCUtil.VANILLA_AND_DMIYC_BINDINGS.add(keyBinding);
        return keyBinding;
    }
}
