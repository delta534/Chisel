package info.jbcs.minecraft.chisel.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.core.CarvingVariation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChiselGrass extends BlockGrass implements Carvable{
	public CarvableHelper carverHelper;
	public static BlockMarble chiselDirt=Chisel.blockDirt;
	public static CarvableVariation[] snowVar=new CarvableVariation[16];
	public BlockChiselGrass(int i) {
		this(null, i);
	}
	public static boolean DirtGrassCheck(IBlockAccess world, int x,int y,int z,int id,int meta)
	{
		int blockMeta=world.getBlockMetadata(x, y, z);
		int blockID=world.getBlockId(x, y, z);
		/*if(meta==blockMeta&&
				(blockID==Chisel.blockDirt.blockID||id==Chisel.blockDirt.blockID)&&
				(blockID==Chisel.blockGrass.blockID||id==Chisel.blockGrass.blockID))
			return true;*/
		return false;
	}
	public BlockChiselGrass(String name,int i) {
		super(name==null?i:Chisel.config.getBlock(name, i).getInt(i));

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
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
	public String getLocalizedName()
	{
		return "Chisel"+carverHelper.blockName;
	}

	@Override
	public Icon getIcon(int side, int metadata) {
		return carverHelper.getVariation(metadata).getIcon(side);
	}
	@Override
	public void registerIcons(IconRegister register) {
		carverHelper.registerIcons("Chisel",this,register);
	}

    @Override
	public void getSubBlocks(int blockId, CreativeTabs tabs, List list){
		carverHelper.registerSubBlocks(this,tabs,list);
    }
	
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		int i = x + rand.nextInt(3) - 1;
		int j = y + rand.nextInt(5) - 3;
		int k = z + rand.nextInt(3) - 1;
		boolean checked=(world.getBlockId(i, j, k) == chiselDirt.blockID);
		if(chiselDirt.blockID==Block.dirt.blockID)
		{
			super.updateTick(world, x, y, z, rand);
			return;
		}
		if (!world.isRemote)
		{
			if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2)
			{
				int meta=world.getBlockMetadata(x, y, z);

				world.setBlock(x, y, z, chiselDirt.blockID);
				world.setBlockMetadataWithNotify(x, y, z, meta, 3);
			}
			else if (world.getBlockLightValue(x, y + 1, z) >= 9)
			{
				for (int l = 0; l < 4; ++l)
				{

					if (checked && world.getBlockLightValue(i, j + 1, k) >= 4 && world.getBlockLightOpacity(i, j + 1, k) <= 2)
					{
						int meta=world.getBlockMetadata(i, j, k);

							world.setBlock(i, j, k, blockID);
							world.setBlockMetadataWithNotify(i, j, k, meta, 3);
						
					}
				}
			}
		}
	}
	//@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
        int meta= world.getBlockMetadata(x,y,z);
		return carverHelper.getVariation(meta).getBlockTexture(world, x, y, z, side);
	/*
		int meta=world.getBlockMetadata(x, y, z);
		if (side == 1)
		{
			return this.carverHelper.getBlockTexture(world, x, y, z, side);
		}
		else if (side == 0)
		{
			return chiselDirt.getBlockTexture(world, x, y, z, side);
		}
		else
		{
			Material material = world.getBlockMaterial(x, y + 1, z);
			/*return material != Material.snow && material != Material.craftedSnow ? 
					carverHelper.getBlockTexture(world, x, y, z, side) : 
						CarvableHelper.getBlockTexture(world, x, y, z, side,snowVar[meta]);//I will get custom snow icons working later but for now lets just return the dirt

			return chiselDirt.getBlockTexture(world, x, y, z, side);

		}
	 */
	}
	@SideOnly(Side.CLIENT)
	public Icon getIconSideOverlay(int meta)
	{
		return BlockGrass.getIconSideOverlay();
		//return carverHelper.getVariation(meta).overlay; get this working once I figure out how to map the side icon;
	}
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = par1IBlockAccess.getBiomeGenForCoords(par2 + l1, par4 + k1).getBiomeGrassColor();
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
	@Override
	public int getRenderType() {
		return Chisel.RenderGrassID;
	}

}
