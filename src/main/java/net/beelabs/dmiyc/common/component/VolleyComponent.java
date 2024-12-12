package net.beelabs.dmiyc.common.component;

import net.beelabs.dmiyc.common.enchantment.effect.BullrushEffect;
import net.beelabs.dmiyc.common.enchantment.effect.VolleyEffect;
import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.beelabs.dmiyc.common.payload.BullrushPayload;
import net.beelabs.dmiyc.common.payload.VolleyPayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class VolleyComponent implements CommonTickingComponent, AutoSyncedComponent {
    private final PlayerEntity obj;

    public VolleyComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        if (this.obj.getMainHandStack().getItem() != Items.MACE) return;
        if (this.obj.getOffHandStack().getItem() != Items.END_CRYSTAL) return;
        if (this.obj.getItemCooldownManager().isCoolingDown(this.obj.getOffHandStack())) return;
        if (!VolleyEffect.hasVolley(obj)) return;
        boolean isRightClickPressed = MinecraftClient.getInstance().options.useKey.isPressed();
        if (isRightClickPressed) VolleyPayload.send();
    }

    public void throwCrystal(){
        this.obj.getWorld().spawnEntity(getEndCrystalProjectileEntity());
        obj.getWorld().playSound(null, obj.getX(), obj.getY(), obj.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 1F, 1f);
        this.obj.getOffHandStack().decrementUnlessCreative(1, this.obj);
        this.obj.getItemCooldownManager().set(this.obj.getOffHandStack(), 60);

    }

    private @NotNull EndCrystalProjectileEntity getEndCrystalProjectileEntity() {
        EndCrystalProjectileEntity crystal = new EndCrystalProjectileEntity(this.obj.getWorld(), this.obj);
        crystal.setPosition(
                this.obj.getX() + this.obj.getRotationVector().x * 0.7,
                this.obj.getEyeY(),
                this.obj.getZ() + this.obj.getRotationVector().z * 0.7);

        crystal.setVelocity(0, 0.1, 0);
        return crystal;
    }

    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

    }

    public PlayerEntity getObj() {
        return obj;
    }
}
