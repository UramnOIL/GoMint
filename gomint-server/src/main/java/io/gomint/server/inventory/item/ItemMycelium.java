package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 110 )
 public class ItemMycelium extends ItemStack implements io.gomint.inventory.item.ItemMycelium {



    @Override
    public String getBlockId() {
        return "minecraft:mycelium";
    }

    @Override
    public ItemType getType() {
        return ItemType.MYCELIUM;
    }

}
