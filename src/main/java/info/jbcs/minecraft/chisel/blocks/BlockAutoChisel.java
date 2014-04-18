package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.tiles.TileEntityAutoChisel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAutoChisel extends BlockContainer {
    IIcon icon;

    public BlockAutoChisel(int i) {
        super( Material.wood);
        this.setBlockName("chisel.autoChisel");

    }

    @Override
    public IIcon getIcon(int side, int i) {
        if (side < 2)
            return Blocks.planks.getIcon(0, 0);
        return icon;
    }

    @Override
    public TileEntity createNewTileEntity(World world,int i) {
        return new TileEntityAutoChisel();
    }

    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int metadata, float what, float these, float are) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }
        //code to open gui explained later
        player.openGui(Chisel.instance, 1, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (par5 != this) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityAutoChisel) {
                TileEntityAutoChisel tile = (TileEntityAutoChisel) te;

                boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
                if ((flag || par5 !=null && par5.canProvidePower()) && par5 != this) {
                    tile.setActive(flag);
                    world.markBlockForUpdate(x, y, z);
                }
            }
        }

    }

    private void dropItems(World world, int x, int y, int z) {

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityAutoChisel)) {
            return;
        }
        TileEntityAutoChisel tile = (TileEntityAutoChisel) tileEntity;
        for (int i = 0; i < 3; i++) {
            dropInWorld(world, x, y, z, tile.items[i]);
        }
        for (ItemStack stack : tile.processed) {
            dropInWorld(world, x, y, z, stack);
        }

    }

    private void dropInWorld(World world, int x, int y, int z, ItemStack item) {
        Random rand = world.rand;
        if (item != null && item.stackSize > 0) {
            float rx = rand.nextFloat() * 0.8F + 0.1F;
            float ry = rand.nextFloat() * 0.8F + 0.1F;
            float rz = rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world,
                    x + rx, y + ry, z + rz,
                    item.copy());

            float factor = 0.05F;
            entityItem.motionX = rand.nextGaussian() * factor;
            entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = rand.nextGaussian() * factor;
            world.spawnEntityInWorld(entityItem);
            item.stackSize = 0;
        }

    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icon = register.registerIcon("Chisel:autoChisel");


    }


}
