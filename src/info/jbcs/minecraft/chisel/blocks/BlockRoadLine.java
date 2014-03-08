package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.render.BlockRoadLineRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRoadLine extends Block {
	public Icon aloneIcon;
	public Icon halfLineIcon;
	public Icon fullLineIcon;
	
	public BlockRoadLine(String name, int i) {
		super(name == null ? i : Chisel.config.getBlock(name, i).getInt(i), Material.circuits);

		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.00390625f, 1.0f);
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return BlockRoadLineRenderer.id;
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || par1World.getBlockId(par2, par3 - 1, par4) == Block.glowStone.blockID;
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (par1World.isRemote) return;

		if (! this.canPlaceBlockAt(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
			par1World.setBlockToAir(par2, par3, par4);
		}

		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		blockIcon = aloneIcon = reg.registerIcon("Chisel:line-marking/white-center");
		halfLineIcon = reg.registerIcon("Chisel:line-marking/white-side");
		fullLineIcon = reg.registerIcon("Chisel:line-marking/white-long");
	}

}
