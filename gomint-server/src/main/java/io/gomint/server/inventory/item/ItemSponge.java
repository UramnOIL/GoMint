package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 19 )
 public class ItemSponge extends ItemStack implements io.gomint.inventory.item.ItemSponge {



    @Override
    public String getBlockId() {
        return "minecraft:sponge";
    }

    @Override
    public ItemType getType() {
        return ItemType.SPONGE;
    }

}
