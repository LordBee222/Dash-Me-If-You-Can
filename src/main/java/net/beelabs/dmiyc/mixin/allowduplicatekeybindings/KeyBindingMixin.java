package net.beelabs.dmiyc.mixin.allowduplicatekeybindings;

import com.llamalad7.mixinextras.sugar.Local;
import net.beelabs.dmiyc.common.util.DMIYCUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Inject(method = "onKeyPressed", at = @At("TAIL"))
    private static void dmiyc$allowDuplicateKeybindings(InputUtil.Key key, CallbackInfo ci, @Local KeyBinding keyBinding) {
        if (DMIYCUtil.allowDuplicateKeybinding(keyBinding)) {
            for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
                if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && DMIYCUtil.allowDuplicateKeybinding(keyBinding2)) {
                    keyBinding2.timesPressed++;
                }
            }
        }
    }

    @Inject(method = "setKeyPressed", at = @At("TAIL"))
    private static void dmiyc$allowDuplicateKeybindings(InputUtil.Key key, boolean pressed, CallbackInfo ci, @Local KeyBinding keyBinding) {
        if (DMIYCUtil.allowDuplicateKeybinding(keyBinding)) {
            for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
                if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && DMIYCUtil.allowDuplicateKeybinding(keyBinding2)) {
                    keyBinding2.setPressed(pressed);
                }
            }
        }
    }
}