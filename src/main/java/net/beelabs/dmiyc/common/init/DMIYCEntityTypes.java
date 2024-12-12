package net.beelabs.dmiyc.common.init;

import net.beelabs.dmiyc.common.DMIYC;
import net.beelabs.dmiyc.common.entity.ClickableEntity;
import net.beelabs.dmiyc.common.entity.EndCrystalProjectileEntity;
import net.beelabs.dmiyc.common.entity.IceShardEntity;
import net.beelabs.dmiyc.common.entity.PermafrostEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DMIYCEntityTypes {
    public static final EntityType<PermafrostEntity> PERMAFROST = register("permafrost", EntityType.Builder.<PermafrostEntity>create(PermafrostEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).maxTrackingRange(64));
    public static final EntityType<IceShardEntity> ICE_SHARD = register("ice_shard", EntityType.Builder.<IceShardEntity>create(IceShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).maxTrackingRange(64));
    public static final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE = register("end_crystal_projectile", EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(1f, 1f).maxTrackingRange(64));
    public static final EntityType<ClickableEntity> CLICKABLE_ENTITY = register("clickable_entity", EntityType.Builder.<ClickableEntity>create(ClickableEntity::new, SpawnGroup.MISC).dimensions(EntityType.END_CRYSTAL.getWidth(), EntityType.END_CRYSTAL.getHeight()).maxTrackingRange(64));


    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Identifier id = DMIYC.id(name);
        EntityType<?> type = builder.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id));
        return (EntityType<T>) Registry.register(Registries.ENTITY_TYPE, id, type);
    }

    public static void init() {
        DMIYC.LOGGER.info("Registering Entity Types for " + DMIYC.MOD_ID);
    }
}
