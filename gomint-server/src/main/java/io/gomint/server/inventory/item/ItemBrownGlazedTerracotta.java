package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 232 )
public class ItemBrownGlazedTerracotta extends ItemStack implements io.gomint.inventory.item.ItemBrownGlazedTerracotta {



    @Override
    public String getBlockId() {
        return "minecraft:brown_glazed_terracotta";
    }

    @Override
    public ItemType getType() {
        return ItemType.BROWN_GLAZED_TERRACOTTA;
    }

}
