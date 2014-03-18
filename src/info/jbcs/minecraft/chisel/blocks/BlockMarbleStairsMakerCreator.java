package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.core.CarvableHelper;
import net.minecraft.block.Block;

public interface BlockMarbleStairsMakerCreator {
    public BlockMarbleStairs create(String name, int i, Block block, int meta, CarvableHelper helper, int ind);
}
