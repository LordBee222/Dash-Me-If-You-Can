package net.beelabs.dmiyc.mixin.permafrost;

import net.beelabs.dmiyc.common.init.DMIYCEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity{

    @Shadow
    private ItemStack stack;

    @Shadow
    protected abstract ItemStack getDefaultItemStack();

    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void dmiyc$fixModProjectileSerializationRead(NbtCompound nbt, CallbackInfo ci) {
        if (isDisallowed()) {
            stack = getDefaultItemStack();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void dmiyc$fixModProjectileSerializationWrite(NbtCompound nbt, CallbackInfo ci) {
        if (isDisallowed()) {
            stack = Items.BEDROCK.getDefaultStack();
        }
    }

    @Unique
    private boolean isDisallowed() {
        return getType() == DMIYCEntityTypes.PERMAFROST;
    }
}
