package org.cloudburstmc.server.block.behavior;

import org.cloudburstmc.server.item.Item;
import org.cloudburstmc.server.utils.Identifier;

public class BlockBehaviorMonsterEgg extends BlockBehaviorSolid {
    public static final int STONE = 0;
    public static final int COBBLESTONE = 1;
    public static final int STONE_BRICK = 2;
    public static final int MOSSY_BRICK = 3;
    public static final int CRACKED_BRICK = 4;
    public static final int CHISELED_BRICK = 5;

    public BlockBehaviorMonsterEgg(Identifier id) {
        super(id);
    }

    @Override
    public float getHardness() {
        return 0.75f;
    }

    @Override
    public float getResistance() {
        return 3.75f;
    }

    @Override
    public Item[] getDrops(Item hand) {
        return new Item[0];
    }
}