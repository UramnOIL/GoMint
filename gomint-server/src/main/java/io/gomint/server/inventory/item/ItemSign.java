package io.gomint.server.inventory.item;

import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 323 )
public class ItemSign extends ItemStack implements io.gomint.inventory.item.ItemSign {

    @Override
    public String getBlockId() {
        return "minecraft:wall_sign";
    }

    @Override
    public ItemType getType() {
        return ItemType.SIGN;
    }

}
