package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;

import java.util.List;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMarbleCarpet extends BlockCarpet implements Carvable {
	public CarvableHelper carverHelper;

	public BlockMarbleCarpet(String name, int i, Material m) {
		super(name==null?i:Chisel.config.getBlock(name, i).getInt(i));

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);		
	}


	@Override
	public Icon getIcon(int side, int metadata) {
		return carverHelper.getVariation(metadata).getIcon(side);
	}

    @Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side){
        int meta= world.getBlockMetadata(x,y,z);
        return carverHelper.getVariation(meta).getBlockTexture(world, x, y, z, side);
    }

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerIcons(IconRegister register) {
		carverHelper.registerIcons("Chisel",this,register);
	}

    @Override
	public void getSubBlocks(int blockId, CreativeTabs tabs, List list){
		carverHelper.registerSubBlocks(this,tabs,list);
    }
	
	@Override
	public int getRenderType() {
		return Chisel.RenderCarpetId;
	}

	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}

    @Override
    public CarvableHelper getHelper() {
        return carverHelper;
    }

}
