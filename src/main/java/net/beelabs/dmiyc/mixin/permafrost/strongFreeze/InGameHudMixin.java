package net.beelabs.dmiyc.mixin.permafrost.strongFreeze;

import net.beelabs.dmiyc.common.component.StrongFrozenComponent;
import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.mixin.accessor.InGameHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private static final Identifier POWDER_SNOW_OUTLINE = Identifier.ofVanilla("textures/misc/powder_snow_outline.png");

/*
    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void drawFrozenHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci){
        if (!blinking && type == InGameHud.HeartType..NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN) || player.hasStatusEffect(PickYourPoison.TORPOR) || player.hasStatusEffect(PickYourPoison.NUMBNESS))) {
            Identifier textureId;
            if (player.hasStatusEffect(PickYourPoison.TORPOR)) {
                textureId = InGameHud.HeartType.FROZEN;
            } else {
                return;
            }
            context.drawTexture(textureId, x, y, halfHeart ? 9 : 0, v, 9, 9);
            ci.cancel();
        }
    }

 */




    @Inject(method = "renderMiscOverlays", at = @At("TAIL"))
    private void renderFrozenOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        InGameHud hud = (InGameHud) (Object) this;
        InGameHudAccessor accessor = (InGameHudAccessor) hud;
        Entity entity = accessor.DMIYC$getClient().getCameraEntity();
        if (entity instanceof PlayerEntity player){
            StrongFrozenComponent component = DMIYCComponents.STRONG_FROZEN.get(player);
            if (component.isFrozen()) {
                hud.renderOverlay(context, POWDER_SNOW_OUTLINE, 1f);
            }
        }
    }
}
