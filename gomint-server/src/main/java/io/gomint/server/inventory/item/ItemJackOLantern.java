package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 91 )
public class ItemJackOLantern extends ItemStack implements io.gomint.inventory.item.ItemJackOLantern {



    @Override
    public String getBlockId() {
        return "minecraft:lit_pumpkin";
    }

    @Override
    public ItemType getType() {
        return ItemType.JACK_O_LANTERN;
    }

}
