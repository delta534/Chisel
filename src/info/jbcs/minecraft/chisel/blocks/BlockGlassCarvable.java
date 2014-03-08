package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;

import java.util.List;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

public class BlockGlassCarvable extends BlockGlass implements Carvable {
	public CarvableHelper carverHelper;

	public BlockGlassCarvable(int i) {
		super(i, Material.glass, false);

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return Chisel.RenderCTMId;
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
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
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}
	@Override
	public String getUnlocalizedName()
	{
		return "Chisel"+carverHelper.blockName;
	}
	
    

}
