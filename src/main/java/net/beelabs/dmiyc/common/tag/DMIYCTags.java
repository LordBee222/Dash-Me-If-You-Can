package net.beelabs.dmiyc.common.tag;

import net.beelabs.dmiyc.common.DMIYC;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class DMIYCTags {
    public static final TagKey<Item> SHIELD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, DMIYC.id("shield_enchantable"));
}
