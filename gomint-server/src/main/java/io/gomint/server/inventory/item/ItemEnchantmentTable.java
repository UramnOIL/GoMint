package io.gomint.server.inventory.item;
import io.gomint.inventory.item.ItemType;

import io.gomint.server.registry.RegisterInfo;
import io.gomint.taglib.NBTTagCompound;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 116 )
 public class ItemEnchantmentTable extends ItemStack implements io.gomint.inventory.item.ItemEnchantmentTable {



    @Override
    public String getBlockId() {
        return "minecraft:enchanting_table";
    }

    @Override
    public ItemType getType() {
        return ItemType.ENCHANTMENT_TABLE;
    }

}
