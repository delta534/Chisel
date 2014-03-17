package info.jbcs.minecraft.chisel.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMarbleSlab extends BlockMarble {
	public Block master;
	public BlockMarbleSlab bottom;
	public BlockMarbleSlab top;
	public boolean isBottom;

	public BlockMarbleSlab(String name,int bottomId,int topId, Block m) {
		super(name+".bottom",bottomId);
		
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        opaqueCubeLookup[blockID] = true;

		master = m;
		bottom = this;
		top = new BlockMarbleSlab(name,this,topId);
		
		isBottom=true;
	}

	public BlockMarbleSlab(String name,BlockMarbleSlab bottomBlock,int topId) {
		super(name+".top",topId);
		
		setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        opaqueCubeLookup[blockID] = true;

		master = bottomBlock.master;
		bottom = bottomBlock;
		top = this;
		
		carverHelper=bottomBlock.carverHelper;
		isBottom=false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		if (isBottom) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return bottom.blockID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return bottom.blockID;
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
//		return master.getIcon(side, metadata);
	}

    @Override
	public void getSubBlocks(int blockId, CreativeTabs tabs, List list){
		if(isBottom) super.getSubBlocks(blockId,tabs,list);
    }
}
