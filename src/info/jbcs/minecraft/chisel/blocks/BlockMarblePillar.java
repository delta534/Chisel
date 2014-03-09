package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.BlockMarblePillarRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMarblePillar extends BlockMarble{
	public Icon []sides=new Icon[6];
	
	public BlockMarblePillar(String name, int i, Material m) {
		super(name,i,m);
	}

	@Override
	public int getRenderType() {
		return BlockMarblePillarRenderer.id;
	}

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return  sides[side];
    }

    public Icon getCtmIcon(int index, int metadata){
		CarvableVariation var=carverHelper.variations.get(metadata);
		return var.getIndexedIcon(index);
    }
   
}
