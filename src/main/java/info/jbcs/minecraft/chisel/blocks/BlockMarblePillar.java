package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.BlockMarblePillarRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockMarblePillar extends BlockMarble {
    public IIcon[] sides = new IIcon[6];

    public BlockMarblePillar(String name, int i, Material m) {
        super(name, i, m);
    }

    @Override
    public int getRenderType() {
        return BlockMarblePillarRenderer.id;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return sides[side];
    }

    public IIcon getCtmIcon(int index, int metadata) {
        RenderVariation var = carverHelper.variations.get(metadata);
        return var.getIndexedIcon(index);
    }

}
