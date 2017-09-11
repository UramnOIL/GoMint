package io.gomint.server.world.block;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( id = 53 )
public class OakWoodStairs extends Stairs {

    @Override
    public int getBlockId() {
        return 53;
    }

    @Override
    public long getBreakTime() {
        return 3000;
    }

}
