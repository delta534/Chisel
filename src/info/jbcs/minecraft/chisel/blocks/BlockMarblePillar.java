package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMV;
import info.jbcs.minecraft.chisel.render.BlockMarblePillarRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMarblePillar extends BlockMarble{
	public Icon sides[]=new Icon[6];
	
	public BlockMarblePillar(String name, int i, Material m) {
		super(name,i,m);
	}
	
	public BlockMarblePillar(int i, Material m) {
		super(i,m);
	}

	@Override
	public int getRenderType() {
		return BlockMarblePillarRenderer.id;
	}

	public Icon getCtmIcon(int index, int metadata){
		CarvableVariation var=carverHelper.variations.get(metadata);
		
		if(index>=4) return var.icon;
		if(!(var instanceof VariationCTMV)) return var.icon;
    	return ((VariationCTMV)var).seamsCtmVert.icons[index];
    }
   
}
