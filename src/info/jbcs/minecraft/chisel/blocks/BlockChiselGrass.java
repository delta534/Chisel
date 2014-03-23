package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.core.variation.GrassCarvableHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockChiselGrass extends BlockGrass implements Carvable {
    public CarvableHelper carverHelper;
    public static BlockMarble chiselDirt = Chisel.blockDirt;
    public static BlockGrass grass = Block.grass;

    public BlockChiselGrass(int i) {
        super(i);
        setCreativeTab(Chisel.tabChisel);

        carverHelper = new GrassCarvableHelper();
    }

    public BlockChiselGrass(String name, int i) {
        super(name == null ? i : Chisel.config.getBlock(name, i).getInt(i));

        carverHelper = new GrassCarvableHelper();

        setCreativeTab(Chisel.tabChisel);
    }

    @Override
    public CarvableVariation getVariation(int metadata) {

        return carverHelper.getVariation(metadata);
    }


    @Override
    public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
    }

    @Override
    public void registerIcons(IconRegister register) {
        carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(int blockId, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    public void updateTick(World world, int x, int y, int z, Random rand) {

        if (chiselDirt.blockID == Block.dirt.blockID&&world.getBlockMetadata(x, y, z)==0 ) {
            grass.updateTick(world, x, y, z, rand);
            return;
        }
        if (!world.isRemote&&Chisel.grassSpread) {
            if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
                int meta = world.getBlockMetadata(x, y, z);

                world.setBlock(x, y, z, chiselDirt.blockID);
                world.setBlockMetadataWithNotify(x, y, z, meta, 3);
            } else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                for (int l = 0; l < 4; ++l) {
                    int i = x + rand.nextInt(3) - 1;
                    int j = y + rand.nextInt(5) - 3;
                    int k = z + rand.nextInt(3) - 1;
                    boolean checked = (world.getBlockId(i, j, k) == chiselDirt.blockID);
                    if (checked && world.getBlockLightValue(i, j + 1, k) >= 4 && world.getBlockLightOpacity(i, j + 1, k) <= 2) {
                        int meta = world.getBlockMetadata(i, j, k);

                        world.setBlock(i, j, k, blockID);
                        world.setBlockMetadataWithNotify(i, j, k, meta, 3);

                    }
                }
            }
        }
    }

    //@Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        return carverHelper.getVariation(meta).getBlockTexture(world, x, y, z, side);
    }

    @Override
    public int getRenderType() {
        return Chisel.RenderCTMId;
    }

    @Override
    public int getBlockColor() {
        return grass.getBlockColor();
    }

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        return grass.colorMultiplier(par1IBlockAccess, par2, par3, par4);
    }

    @Override
    public int getRenderColor(int par1) {
        return grass.getRenderColor(par1);
    }

    @Override
    public CarvableHelper getHelper() {
        return carverHelper;
    }
}
