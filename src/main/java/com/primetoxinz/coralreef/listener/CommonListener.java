package com.primetoxinz.coralreef.listener;

import com.primetoxinz.coralreef.CoralReefMod;
import com.primetoxinz.coralreef.block.CoralBlock;
import com.primetoxinz.coralreef.block.GrowableCoralBlock;
import com.primetoxinz.coralreef.block.ReefBlock;
import com.primetoxinz.coralreef.worldgen.ReefStructure;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent.ChunkDecoration;

import java.util.ArrayList;
import java.util.List;

public class CommonListener {
    public static Block REEF1;
    public static Block REEF2;
    public static final List<Block> CORALS = new ArrayList<>();

    private static ReefStructure reefStructure;

    @EventListener
    public void onBlockRegister(BlockRegistryEvent event) {
        REEF1 = new ReefBlock(CoralReefMod.id("reef1"));
        REEF2 = new ReefBlock(CoralReefMod.id("reef2"));

        CORALS.add(new CoralBlock(CoralReefMod.id("coral_orange")));
        CORALS.add(new CoralBlock(CoralReefMod.id("coral_magenta")));
        CORALS.add(new CoralBlock(CoralReefMod.id("coral_pink")));
        CORALS.add(new CoralBlock(CoralReefMod.id("coral_cyan")));
        CORALS.add(new GrowableCoralBlock(CoralReefMod.id("coral_lime"), 3));
        CORALS.add(new GrowableCoralBlock(CoralReefMod.id("coral_brown"), 3));
    }

    @EventListener
    public void onChunkDecoration(ChunkDecoration event) {
        if (event.world.dimension.id != 0) return;
        if (REEF1 == null) return;

        if (reefStructure == null) {
            reefStructure = new ReefStructure(REEF1, REEF2, CORALS);
        }

        // ~25% chance of generating a reef per chunk
        if (event.random.nextInt(4) == 0) {
            int x = event.x + event.random.nextInt(16);
            int z = event.z + event.random.nextInt(16);
            reefStructure.generate(event.world, event.random, x, 0, z);
        }
    }
}
