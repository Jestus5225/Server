package org.cloudburstmc.server.block.behavior;

import com.nukkitx.math.vector.Vector3i;
import org.cloudburstmc.server.block.BlockState;
import org.cloudburstmc.server.item.Item;
import org.cloudburstmc.server.level.Level;
import org.cloudburstmc.server.utils.Identifier;
import org.cloudburstmc.server.utils.Rail;

import static org.cloudburstmc.server.block.BlockTypes.GOLDEN_RAIL;

/**
 * Created by Snake1999 on 2016/1/11.
 * Contributed by: larryTheCoder on 2017/7/18.
 * <p>
 * Nukkit Project,
 * Minecart and Riding Project,
 * Package cn.nukkit.block in project Nukkit.
 */
public class BlockBehaviorRailPowered extends BlockBehaviorRail {

    public BlockBehaviorRailPowered(Identifier id) {
        super(id);
        canBePowered = true;
    }

    @Override
    public int onUpdate(int type) {
        // Warning: I din't recommended this on slow networks server or slow client
        //          Network below 86Kb/s. This will became unresponsive to clients 
        //          When updating the block state. Espicially on the world with many rails. 
        //          Trust me, I tested this on my server.
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (super.onUpdate(type) == Level.BLOCK_UPDATE_NORMAL) {
                return 0; // Already broken
            }
            boolean wasPowered = isActive();
            boolean isPowered = level.isBlockPowered(this.getPosition())
                    || checkSurrounding(this.getPosition(), true, 0)
                    || checkSurrounding(this.getPosition(), false, 0);

            // Avoid Block minstake
            if (wasPowered != isPowered) {
                setActive(isPowered);
                level.updateAround(this.getPosition().down());
                if (getOrientation().isAscending()) {
                    level.updateAround(this.getPosition().up());
                }
            }
            return type;
        }
        return 0;
    }

    /**
     * Check the surrounding of the rail
     *
     * @param pos      The rail position
     * @param relative The relative of the rail that will be checked
     * @param power    The count of the rail that had been counted
     * @return Boolean of the surrounding area. Where the powered rail on!
     */
    protected boolean checkSurrounding(Vector3i pos, boolean relative, int power) {
        // The powered rail can power up to 8 blocks only
        if (power >= 8) {
            return false;
        }
        // The position of the floor numbers
        int dx = pos.getX();
        int dy = pos.getY();
        int dz = pos.getZ();
        // First: get the base block
        BlockBehaviorRail block;
        BlockState blockState2 = level.getBlock(dx, dy, dz);

        // Second: check if the rail is Powered rail
        if (Rail.isRailBlock(blockState2)) {
            block = (BlockBehaviorRail) blockState2;
        } else {
            return false;
        }

        // Used to check if the next ascending rail should be what
        Rail.Orientation base = null;
        boolean onStraight = true;
        // Third: Recalculate the base position
        switch (block.getOrientation()) {
            case STRAIGHT_NORTH_SOUTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                }
                break;
            case STRAIGHT_EAST_WEST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                }
                break;
            case ASCENDING_EAST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                    dy++;
                    onStraight = false;
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            case ASCENDING_WEST:
                if (relative) {
                    dx--;
                    dy++;
                    onStraight = false;
                } else {
                    dx++;
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            case ASCENDING_NORTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                    dy++;
                    onStraight = false;
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
                break;
            case ASCENDING_SOUTH:
                if (relative) {
                    dz++;
                    dy++;
                    onStraight = false;
                } else {
                    dz--;
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
                break;
            default:
                // Unable to determinate the rail orientation
                // Wrong rail?
                return false;
        }
        // Next check the if rail is on power state
        return canPowered(Vector3i.from(dx, dy, dz), base, power, relative)
                || onStraight && canPowered(Vector3i.from(dx, dy - 1, dz), base, power, relative);
    }

    protected boolean canPowered(Vector3i pos, Rail.Orientation state, int power, boolean relative) {
        BlockState blockState = level.getBlock(pos);
        // What! My block is air??!! Impossible! XD
        if (!(blockState instanceof BlockBehaviorRailPowered)) {
            return false;
        }

        // Sometimes the rails are diffrent orientation
        Rail.Orientation base = ((BlockBehaviorRailPowered) blockState).getOrientation();

        // Possible way how to know when the rail is activated is rail were directly powered
        // OR recheck the surrounding... Which will returns here =w=        
        return (state != Rail.Orientation.STRAIGHT_EAST_WEST
                || base != Rail.Orientation.STRAIGHT_NORTH_SOUTH
                && base != Rail.Orientation.ASCENDING_NORTH
                && base != Rail.Orientation.ASCENDING_SOUTH)
                && (state != Rail.Orientation.STRAIGHT_NORTH_SOUTH
                || base != Rail.Orientation.STRAIGHT_EAST_WEST
                && base != Rail.Orientation.ASCENDING_EAST
                && base != Rail.Orientation.ASCENDING_WEST)
                && (level.isBlockPowered(pos) || checkSurrounding(pos, relative, power + 1));
    }

    @Override
    public Item[] getDrops(Item hand) {
        return new Item[]{
                Item.get(GOLDEN_RAIL)
        };
    }
}