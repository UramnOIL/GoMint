package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 108 )
 public class ItemBrickStairs extends ItemStack implements io.gomint.inventory.item.ItemBrickStairs {



    @Override
    public String getBlockId() {
        return "minecraft:brick_stairs";
    }

    @Override
    public ItemType getType() {
        return ItemType.BRICK_STAIRS;
    }

}
