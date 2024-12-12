package net.beelabs.dmiyc.common.component;

import net.beelabs.dmiyc.common.init.DMIYCComponents;
import net.beelabs.dmiyc.common.init.DMIYCParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class StrongFrozenComponent implements CommonTickingComponent, AutoSyncedComponent {
    private int frozenTicks = 0;
    private LivingEntity obj;

    public StrongFrozenComponent(LivingEntity obj) {
        this.obj = obj;
    }

    @Override
    public void tick() {
        if (this.isFrozen()){
            --this.frozenTicks;
            if (this.obj.canFreeze() && this.obj.getWorld() instanceof ServerWorld serverWorld){
                serverWorld.spawnParticles(DMIYCParticles.FORMED_SNOWFLAKE, this.obj.getParticleX(1), this.obj.getRandomBodyY(), this.obj.getParticleZ(1), 1, 0.1, 0.1, 0.1, 0.05);
                if (this.canDamage()){
                    this.obj.damage(serverWorld, this.obj.getDamageSources().freeze(), 2.0F);
                    DMIYCComponents.STRONG_FROZEN.sync(this.obj);
                }
            }
        }
    }

    public void addFrozenTicks(int ticksToAdd){
       // Integer i = this.frozenTicks;
        this.frozenTicks += ticksToAdd;
        DMIYCComponents.STRONG_FROZEN.sync(this.obj);
    }

    public void setFrozenTicks(int frozenTicks) {
        this.frozenTicks = frozenTicks;
        DMIYCComponents.STRONG_FROZEN.sync(this.obj);
    }

    public boolean canDamage(){
        return this.obj.age % 20 == 0;
    }

    public boolean isFrozen(){
        return this.frozenTicks > 0;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        frozenTicks = tag.getInt("frozen_ticks");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("frozen_ticks", frozenTicks);
    }
}
