package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockMarble extends Block implements Carvable {
	public CarvableHelper carverHelper;

	public BlockMarble(int i) {
		this(null, i, Material.rock);
	}

	public BlockMarble(String name,int i) {
		this(name, i, Material.rock);
	}

	public BlockMarble(int i, Material m) {
		this(null, i, m);
	}
	public BlockMarble(String name,int i, Material m) {
		super(name==null?i:Chisel.config.getBlock(name, i).getInt(i), m);

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public Icon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

    @Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side){
    	return carverHelper.getBlockTexture(world, x, y, z, side);
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
		return Chisel.RenderCTMId;
	}

	@Override
	public CarvableVariation getVariation(int metadata) {
		
		return carverHelper.getVariation(metadata);
	}
	@Override
	public String getUnlocalizedName()
	{
		return "Chisel"+carverHelper.blockName;
	}
	
}
