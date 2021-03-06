package io.gomint.inventory.item;

import io.gomint.GoMint;

/**
 * @author geNAZt
 * @version 1.0
 */
public interface ItemBucket extends ItemStack, ItemBurnable {

    /**
     * Create a new item stack with given class and amount
     *
     * @param amount which is used for the creation
     */
    static ItemBucket create( int amount ) {
        return GoMint.instance().createItemStack( ItemBucket.class, amount );
    }

    /**
     * Set content of the bucket
     *
     * @param type of the content
     */
    void setContent( Content type );

    /**
     * Get the content from this bucket
     *
     * @return content of bucket
     */
    Content getContent();

    enum Content {
        /**
         * Water content
         */
        WATER,

        /**
         * Lava content
         */
        LAVA,

        /**
         * Milk content
         */
        MILK,

        /**
         * No content
         */
        EMPTY
    }

}
