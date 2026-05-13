package com.primetoxinz.coralreef.block;

import com.primetoxinz.coralreef.listener.CommonListener;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class GrowableCoralBlock extends CoralBlock {
    private final int maxHeight;

    public GrowableCoralBlock(Identifier id, int maxHeight) {
        super(id);
        this.maxHeight = maxHeight;
        setTicksRandomly(true);
    }

    @Override
    public boolean canPlaceAt(Level level, int x, int y, int z) {
        int below = level.getBlockID(x, y - 1, z);
        if (CommonListener.REEF1 != null && below == CommonListener.REEF1.id) {
            return isAdjacentToWater(level, x, y, z);
        }
        return below == this.id && isAdjacentToWater(level, x, y, z);
    }

    @Override
    protected boolean canSurvive(Level level, int x, int y, int z) {
        int below = level.getBlockID(x, y - 1, z);
        boolean validBase = (CommonListener.REEF1 != null && below == CommonListener.REEF1.id)
                         || below == this.id;
        return validBase && isAdjacentToWater(level, x, y, z);
    }

    @Override
    public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
        if (!canSurvive(level, x, y, z)) {
            drop(level, x, y, z, 0);
            level.setBlock(x, y, z, 0);
            return;
        }
        // grow upward if there's water above and height limit not reached
        if (level.getMaterial(x, y + 1, z) == Material.WATER) {
            int height = 1;
            while (height < maxHeight && level.getBlockID(x, y - height, z) == this.id) {
                height++;
            }
            if (height < maxHeight) {
                int meta = level.getBlockMeta(x, y, z);
                if (meta >= 15) {
                    level.setBlock(x, y + 1, z, this.id);
                    level.setBlockMeta(x, y, z, 0);
                } else {
                    level.setBlockMeta(x, y, z, meta + 1);
                }
            }
        }
    }
}
