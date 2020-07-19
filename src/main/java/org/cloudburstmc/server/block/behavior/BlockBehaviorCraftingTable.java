package org.cloudburstmc.server.block.behavior;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import org.cloudburstmc.server.item.Item;
import org.cloudburstmc.server.item.ItemTool;
import org.cloudburstmc.server.player.Player;
import org.cloudburstmc.server.player.Player.CraftingType;
import org.cloudburstmc.server.utils.BlockColor;
import org.cloudburstmc.server.utils.Identifier;

/**
 * Created on 2015/12/5 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockBehaviorCraftingTable extends BlockBehaviorSolid {
    public BlockBehaviorCraftingTable(Identifier id) {
        super(id);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public float getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            player.craftingType = CraftingType.BIG;
            player.setCraftingGrid(player.getUIInventory().getBigCraftingGrid());
            ContainerOpenPacket pk = new ContainerOpenPacket();
            pk.setId((byte) -1);
            pk.setBlockPosition(this.getPosition());
            pk.setType(ContainerType.WORKBENCH);
            pk.setUniqueEntityId(player.getUniqueId());
            player.sendPacket(pk);
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}