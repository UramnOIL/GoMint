/*
 * Copyright (c) 2018, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.entity.tileentity;

import io.gomint.math.Location;
import io.gomint.server.inventory.item.Items;
import io.gomint.server.world.WorldAdapter;
import io.gomint.taglib.NBTTagCompound;

import java.awt.Color;

/**
 * @author geNAZt
 * @version 1.0
 */
public class CauldronTileEntity extends TileEntity {

    private Color color;

    private short potionId;
    private short potionType;

    public CauldronTileEntity( Location location ) {
        super( location );
    }

    public CauldronTileEntity( NBTTagCompound compound, WorldAdapter world, Items items ) {
        super( compound, world, items );

        int argb = compound.getInteger( "CustomColor", 0 );
        if ( argb != 0 ) {
            this.color = new Color( argb, true );
        }

        this.potionId = compound.getShort( "PotionId", (short) -1 );
        this.potionType = compound.getShort( "PotionType", (short) 0 );
    }

    @Override
    public void update( long currentMillis ) {

    }

    @Override
    public void toCompound( NBTTagCompound compound, SerializationReason reason ) {
        super.toCompound( compound, reason );

        compound.addValue( "id", "Cauldron" );

        if ( this.color != null ) {
            compound.addValue( "CustomColor", this.color.getRGB() );
        }

        compound.addValue( "PotionId", this.potionId );
        compound.addValue( "PotionType", this.potionType );
    }

}