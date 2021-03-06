package io.gomint.inventory.item;

import io.gomint.GoMint;

/**
 * @author geNAZt
 * @version 1.0
 */
public interface ItemPhantomMembrane extends ItemStack {

    /**
     * Create a new item stack with given class and amount
     *
     * @param amount which is used for the creation
     */
    static ItemPhantomMembrane create( int amount ) {
        return GoMint.instance().createItemStack( ItemPhantomMembrane.class, amount );
    }

}
