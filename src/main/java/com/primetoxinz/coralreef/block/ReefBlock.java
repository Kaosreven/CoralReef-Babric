package com.primetoxinz.coralreef.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class ReefBlock extends TemplateBlock {
    public ReefBlock(Identifier id) {
        super(id, Material.STONE);
        setTranslationKey(id);
        setHardness(1.5f);
    }
}
