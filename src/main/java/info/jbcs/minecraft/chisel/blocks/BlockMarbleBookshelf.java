package info.jbcs.minecraft.chisel.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockMarbleBookshelf extends BlockMarble {

    public BlockMarbleBookshelf(int i) {
        super(i);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (side < 2)
            return Block.planks.getBlockTextureFromSide(side);
        return super.getIcon(side, metadata);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z,
                                int side) {
        if (side < 2)
            return Block.planks.getBlockTextureFromSide(side);
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 3;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return Item.book.itemID;
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        return Block.bookShelf.getBlockDropped(world, x, y, z, metadata, fortune);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);
        if (blockID != Block.bookShelf.blockID) {
            for (int i = x - 2; i <= x + 2; ++i) {
                for (int j = z - 2; j <= z + 2; ++j) {
                    if (i > x - 2 && i < x + 2 && j == x - 1) {
                        j = z + 2;
                    }

                    if (rand.nextInt(16) == 0) {
                        for (int k = y - 2; k < y + 1; ++k)
                            if (world.getBlockId(i, k, j) == Block.enchantmentTable.blockID) {

                                if (!world.isAirBlock((i - x) / 2 + x, k,
                                        (j - z) / 2 + z)) {
                                    break;
                                }
                                world.spawnParticle(
                                        "enchantmenttable",
                                        (double) i + 0.5D,
                                        (double) k + 2.0D,
                                        (double) j + 0.5D,
                                        -(double) ((float) (i - x) + rand
                                                .nextFloat()) + 0.5D,
                                        -(double) ((float) (k - y)
                                                + rand.nextFloat() + 1.0F),
                                        -(double) ((float) (j - z) + rand
                                                .nextFloat()) + 0.5D);
                            }
                    }
                }
            }
        }
    }

}
