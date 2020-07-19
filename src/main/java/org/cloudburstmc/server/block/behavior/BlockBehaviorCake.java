package org.cloudburstmc.server.block.behavior;

import com.nukkitx.math.vector.Vector3f;
import org.cloudburstmc.server.block.BlockState;
import org.cloudburstmc.server.block.BlockTypes;
import org.cloudburstmc.server.item.Item;
import org.cloudburstmc.server.item.ItemIds;
import org.cloudburstmc.server.item.food.Food;
import org.cloudburstmc.server.level.Level;
import org.cloudburstmc.server.math.BlockFace;
import org.cloudburstmc.server.player.Player;
import org.cloudburstmc.server.utils.BlockColor;
import org.cloudburstmc.server.utils.Identifier;

/**
 * @author Nukkit Project Team
 */
public class BlockBehaviorCake extends BlockBehaviorTransparent {

    public BlockBehaviorCake(Identifier id) {
        super(id);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getResistance() {
        return 2.5f;
    }

    @Override
    public float getMinX() {
        return this.getX() + (1 + getMeta() * 2) / 16f;
    }

    @Override
    public float getMinY() {
        return this.getY();
    }

    @Override
    public float getMinZ() {
        return this.getZ() + 0.0625f;
    }

    @Override
    public float getMaxX() {
        return this.getX() - 0.0625f + 1;
    }

    @Override
    public float getMaxY() {
        return this.getY() + 0.5f;
    }

    @Override
    public float getMaxZ() {
        return this.getZ() - 0.0625f + 1;
    }

    @Override
    public boolean place(Item item, BlockState blockState, BlockState target, BlockFace face, Vector3f clickPos, Player player) {
        if (down().getId() != BlockTypes.AIR) {
            getLevel().setBlock(blockState.getPosition(), this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down().getId() == BlockTypes.AIR) {
                getLevel().setBlock(this.getPosition(), BlockState.AIR, true);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public Item[] getDrops(Item hand) {
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return Item.get(ItemIds.CAKE);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null && player.getFoodData().getLevel() < player.getFoodData().getMaxLevel()) {
            if (getMeta() <= 0x06) setMeta(getMeta() + 1);
            if (getMeta() >= 0x06) {
                getLevel().setBlock(this.getPosition(), BlockState.AIR, true);
            } else {
                Food.getByRelative(this).eatenBy(player);
                getLevel().setBlock(this.getPosition(), this, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    public int getComparatorInputOverride() {
        return (7 - this.getMeta()) * 2;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public boolean canWaterlogSource() {
        return true;
    }
}