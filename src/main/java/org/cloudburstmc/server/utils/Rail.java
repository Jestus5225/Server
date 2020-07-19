package org.cloudburstmc.server.utils;

import org.cloudburstmc.server.block.BlockState;
import org.cloudburstmc.server.math.BlockFace;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.cloudburstmc.server.block.BlockTypes.*;
import static org.cloudburstmc.server.utils.Rail.Orientation.State.*;

/**
 * INTERNAL helper class of railway
 * <p>
 * By lmlstarqaq http://snake1999.com/
 * Creation time: 2017/7/1 17:42.
 */
public final class Rail {

    public static boolean isRailBlock(BlockState blockState) {
        Objects.requireNonNull(blockState, "Rail block predicate can not accept null block");
        return isRailBlock(blockState.getType());
    }

    public enum Orientation {
        STRAIGHT_NORTH_SOUTH(0, STRAIGHT, BlockFace.NORTH, BlockFace.SOUTH, null),
        STRAIGHT_EAST_WEST(1, STRAIGHT, BlockFace.EAST, BlockFace.WEST, null),
        ASCENDING_EAST(2, ASCENDING, BlockFace.EAST, BlockFace.WEST, BlockFace.EAST),
        ASCENDING_WEST(3, ASCENDING, BlockFace.EAST, BlockFace.WEST, BlockFace.WEST),
        ASCENDING_NORTH(4, ASCENDING, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.NORTH),
        ASCENDING_SOUTH(5, ASCENDING, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH),
        CURVED_SOUTH_EAST(6, CURVED, BlockFace.SOUTH, BlockFace.EAST, null),
        CURVED_SOUTH_WEST(7, CURVED, BlockFace.SOUTH, BlockFace.WEST, null),
        CURVED_NORTH_WEST(8, CURVED, BlockFace.NORTH, BlockFace.WEST, null),
        CURVED_NORTH_EAST(9, CURVED, BlockFace.NORTH, BlockFace.EAST, null);

        private static final Orientation[] META_LOOKUP = new Orientation[values().length];
        private final int meta;
        private final State state;
        private final List<BlockFace> connectingDirections;
        private final BlockFace ascendingDirection;

        Orientation(int meta, State state, BlockFace from, BlockFace to, BlockFace ascendingDirection) {
            this.meta = meta;
            this.state = state;
            this.connectingDirections = Arrays.asList(from, to);
            this.ascendingDirection = ascendingDirection;
        }

        public static Orientation byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static Orientation straight(BlockFace face) {
            switch (face) {
                case NORTH:
                case SOUTH:
                    return STRAIGHT_NORTH_SOUTH;
                case EAST:
                case WEST:
                    return STRAIGHT_EAST_WEST;
            }
            return STRAIGHT_NORTH_SOUTH;
        }

        public static Orientation ascending(BlockFace face) {
            switch (face) {
                case NORTH:
                    return ASCENDING_NORTH;
                case SOUTH:
                    return ASCENDING_SOUTH;
                case EAST:
                    return ASCENDING_EAST;
                case WEST:
                    return ASCENDING_WEST;
            }
            return ASCENDING_EAST;
        }

        public static Orientation curved(BlockFace f1, BlockFace f2) {
            for (Orientation o : new Orientation[]{CURVED_SOUTH_EAST, CURVED_SOUTH_WEST, CURVED_NORTH_WEST, CURVED_NORTH_EAST}) {
                if (o.connectingDirections.contains(f1) && o.connectingDirections.contains(f2)) {
                    return o;
                }
            }
            return CURVED_SOUTH_EAST;
        }

        public static Orientation straightOrCurved(BlockFace f1, BlockFace f2) {
            for (Orientation o : new Orientation[]{STRAIGHT_NORTH_SOUTH, STRAIGHT_EAST_WEST, CURVED_SOUTH_EAST, CURVED_SOUTH_WEST, CURVED_NORTH_WEST, CURVED_NORTH_EAST}) {
                if (o.connectingDirections.contains(f1) && o.connectingDirections.contains(f2)) {
                    return o;
                }
            }
            return STRAIGHT_NORTH_SOUTH;
        }

        public int metadata() {
            return meta;
        }

        public boolean hasConnectingDirections(BlockFace... faces) {
            return Stream.of(faces).allMatch(connectingDirections::contains);
        }

        public List<BlockFace> connectingDirections() {
            return connectingDirections;
        }

        public Optional<BlockFace> ascendingDirection() {
            return Optional.ofNullable(ascendingDirection);
        }

        public enum State {
            STRAIGHT, ASCENDING, CURVED
        }

        public boolean isStraight() {
            return state == STRAIGHT;
        }

        public boolean isAscending() {
            return state == ASCENDING;
        }

        public boolean isCurved() {
            return state == CURVED;
        }

        static {
            for (Orientation o : values()) {
                META_LOOKUP[o.meta] = o;
            }
        }
    }

    public static boolean isRailBlock(Identifier id) {
        return id == RAIL || id == GOLDEN_RAIL || id == ACTIVATOR_RAIL || id == DETECTOR_RAIL;
    }

    private Rail() {
        //no instance
    }
}