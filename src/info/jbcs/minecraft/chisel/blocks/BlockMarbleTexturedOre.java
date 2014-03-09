package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.utilities.BlockTexturedOre;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

public class BlockMarbleTexturedOre extends BlockTexturedOre implements Carvable {
	public CarvableHelper carverHelper;

	public BlockMarbleTexturedOre(String name,int i, Material mat, String baseIcon) {
		super(name==null?i:Chisel.config.getBlock(name, i).getInt(i), mat, baseIcon);
		
		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);

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
