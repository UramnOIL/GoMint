package io.gomint.server.world.block;

import io.gomint.world.block.BlockType;

import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo( sId = "minecraft:powered_repeater" )
public class RedstoneRepeaterActive extends Block implements io.gomint.world.block.BlockRedstoneRepeaterActive {

    @Override
    public String getBlockId() {
        return "minecraft:powered_repeater";
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public float getBlastResistance() {
        return 0.0f;
    }

    @Override
    public BlockType getType() {
        return BlockType.REDSTONE_REPEATER_ACTIVE;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
