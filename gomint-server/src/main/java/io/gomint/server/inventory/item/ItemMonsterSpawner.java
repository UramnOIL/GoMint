package io.gomint.server.inventory.item;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 52 )
 public class ItemMonsterSpawner extends ItemStack implements io.gomint.inventory.item.ItemMonsterSpawner {

    // CHECKSTYLE:OFF
    public ItemMonsterSpawner( short data, int amount ) {
        super( 52, data, amount );
    }

    public ItemMonsterSpawner( short data, int amount, NBTTagCompound nbt ) {
        super( 52, data, amount, nbt );
    }
    // CHECKSTYLE:ON

}
