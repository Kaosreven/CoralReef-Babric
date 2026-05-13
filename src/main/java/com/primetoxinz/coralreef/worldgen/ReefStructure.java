package com.primetoxinz.coralreef.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.List;
import java.util.Random;

public class ReefStructure extends Structure {
    private final Block reefBase;
    private final Block reefRock;
    private final List<Block> corals;

    public ReefStructure(Block reefBase, Block reefRock, List<Block> corals) {
        this.reefBase = reefBase;
        this.reefRock = reefRock;
        this.corals = corals;
    }

    @Override
    public boolean generate(Level level, Random rand, int x, int y, int z) {
        int floorY = findOceanFloor(level, x, z);
        if (floorY < 0) return false;

        int radius = 3 + rand.nextInt(4);
        boolean placed = false;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                int wx = x + dx, wz = z + dz;
                int localFloor = findOceanFloor(level, wx, wz);
                if (localFloor < 0) continue;

                int blockId = level.getBlockID(wx, localFloor, wz);
                if (canReplace(blockId)) {
                    level.setBlock(wx, localFloor, wz, reefBase.id);
                    placed = true;

                    // Place coral if two water blocks exist above the reef
                    if (!corals.isEmpty()
                            && isWater(level, wx, localFloor + 1, wz)
                            && isWater(level, wx, localFloor + 2, wz)
                            && rand.nextDouble() < 0.4) {
                        Block coral = corals.get(rand.nextInt(corals.size()));
                        level.setBlock(wx, localFloor + 1, wz, coral.id);
                    }
                }
            }
        }

        // Scatter a few reef rock formations in and around the disk
        for (int i = 0; i < 3; i++) {
            int rx = x + rand.nextInt(radius * 2 + 1) - radius;
            int rz = z + rand.nextInt(radius * 2 + 1) - radius;
            int rFloor = findOceanFloor(level, rx, rz);
            if (rFloor >= 0) {
                placeRock(level, rand, rx, rFloor, rz);
            }
        }

        return placed;
    }

    private void placeRock(Level level, Random rand, int x, int y, int z) {
        int size = 1 + rand.nextInt(2);
        for (int dx = -size; dx <= size; dx++) {
            for (int dy = 0; dy <= size; dy++) {
                for (int dz = -size; dz <= size; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= size * size) {
                        int wx = x + dx, wy = y + dy, wz = z + dz;
                        if (isWater(level, wx, wy, wz)) {
                            level.setBlock(wx, wy, wz, reefRock.id);
                        }
                    }
                }
            }
        }
    }

    private int findOceanFloor(Level level, int x, int z) {
        for (int y = 60; y > 5; y--) {
            int id = level.getBlockID(x, y, z);
            if (!isWaterOrAir(id)) {
                // solid block — check if the block above is water (i.e., we're underwater)
                if (level.getMaterial(x, y + 1, z) == Material.WATER) {
                    return y;
                }
                return -1;
            }
        }
        return -1;
    }

    private boolean isWater(Level level, int x, int y, int z) {
        return level.getMaterial(x, y, z) == Material.WATER;
    }

    private boolean isWaterOrAir(int id) {
        return id == 0
            || id == Block.FLOWING_WATER.id
            || id == Block.STILL_WATER.id;
    }

    private boolean canReplace(int id) {
        return id == Block.SAND.id
            || id == Block.GRAVEL.id
            || id == Block.DIRT.id;
    }
}
