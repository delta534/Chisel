package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.CarvableHelper;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

public class BlockMarbleWall extends BlockWall {
	CarvableHelper carverHelper;

	public BlockMarbleWall(int id, Block block) {
		super(id, block);
		
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
		carverHelper.registerIcons("Chisel",this,register);
	}

    @Override
	public void getSubBlocks(int blockId, CreativeTabs tabs, List list){
		carverHelper.registerSubBlocks(this,tabs,list);
    }
    @Override
	public String getUnlocalizedName()
	{
		return "Chisel"+carverHelper.blockName;
	}
	
}
