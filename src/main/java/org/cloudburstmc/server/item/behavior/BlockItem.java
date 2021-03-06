package org.cloudburstmc.server.item.behavior;

import org.cloudburstmc.server.block.BlockIds;
import org.cloudburstmc.server.block.BlockState;
import org.cloudburstmc.server.block.util.BlockStateMetaMappings;
import org.cloudburstmc.server.utils.Identifier;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockItem extends Item {

    private BlockState blockState;

    public BlockItem(BlockState blockState) {
        super(blockState.getType());
        this.blockState = blockState;
    }

    public BlockState getBlock() {
        return blockState;
    }

    @Override
    public void setMeta(int meta) {
        if ((meta & 0xffff) == 0xffff || BlockStateMetaMappings.hasMeta(getId(), meta)) {
            super.setMeta(meta);
        } else {
            super.setMeta(0);
        }
    }

    @Override
    public int getMaxStackSize() {
        //Shulker boxes don't stack!
        Identifier id = this.getId();
        if (id == BlockIds.SHULKER_BOX || id == BlockIds.UNDYED_SHULKER_BOX) {
            return 1;
        }

        return super.getMaxStackSize();
    }

    @Override
    protected void onMetaChange(int newMeta) {
        this.blockState = BlockStateMetaMappings.getStateFromMeta(this.getId(), newMeta);
    }
}
