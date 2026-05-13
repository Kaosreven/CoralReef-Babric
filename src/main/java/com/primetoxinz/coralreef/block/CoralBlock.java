package com.primetoxinz.coralreef.block;

import com.primetoxinz.coralreef.listener.CommonListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class CoralBlock extends TemplateBlock {

    public CoralBlock(Identifier id) {
        super(id, Material.WATER);
        setTranslationKey(id);
        setHardness(0.0f);
        setLightEmittance(1.0f);
        setLightOpacity(0);
        setTicksRandomly(true);
    }

    @Override
    public boolean canPlaceAt(Level level, int x, int y, int z) {
        int below = level.getBlockID(x, y - 1, z);
        return CommonListener.REEF1 != null &&
               below == CommonListener.REEF1.id &&
               isAdjacentToWater(level, x, y, z);
    }

    @Override
    public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
        super.onAdjacentBlockUpdate(level, x, y, z, id);
        if (!canSurvive(level, x, y, z)) {
            drop(level, x, y, z, level.getBlockMeta(x, y, z));
            level.setBlock(x, y, z, 0);
        }
    }

    @Override
    public void onBlockRemoved(Level level, int x, int y, int z) {
        super.onBlockRemoved(level, x, y, z);
        if (isAdjacentToWater(level, x, y, z)) {
            level.setBlock(x, y, z, Block.STILL_WATER.id);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onRandomClientTick(Level level, int x, int y, int z, Random rand) {
        if (level.getMaterial(x, y + 1, z) == Material.WATER) {
            for (int i = 0; i < 4; i++) {
                double bx = x + rand.nextDouble();
                double by = y + rand.nextDouble();
                double bz = z + rand.nextDouble();
                level.addParticle("bubble", bx, by, bz, 0.0, 0.04, 0.0);
            }
        }
    }

    protected boolean canSurvive(Level level, int x, int y, int z) {
        int below = level.getBlockID(x, y - 1, z);
        return CommonListener.REEF1 != null &&
               below == CommonListener.REEF1.id &&
               isAdjacentToWater(level, x, y, z);
    }

    protected boolean isAdjacentToWater(Level level, int x, int y, int z) {
        return level.getMaterial(x + 1, y, z) == Material.WATER
            || level.getMaterial(x - 1, y, z) == Material.WATER
            || level.getMaterial(x, y, z + 1) == Material.WATER
            || level.getMaterial(x, y, z - 1) == Material.WATER
            || level.getMaterial(x, y + 1, z) == Material.WATER;
    }

    @Override
    public Box getCollisionShape(Level level, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getRenderType() {
        return 1;
    }
}
