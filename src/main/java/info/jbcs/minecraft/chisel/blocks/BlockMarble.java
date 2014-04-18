package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BlockMarble extends Block implements Carvable {
    public CarvableHelper carverHelper;

    public BlockMarble(int i) {
        this(null, i, Material.rock);
    }

    public BlockMarble(String name, int i) {
        this(name, i, Material.rock);
    }

    public BlockMarble(int i, Material m) {
        this(null, i, m);
    }

    public BlockMarble(String name, int i, Material m) {
        super(m);

        carverHelper = new CarvableHelper();

        setCreativeTab(Chisel.tabChisel);
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if(carverHelper!=null)
            if(carverHelper.getVariation(metadata)!=null)
                return carverHelper.getVariation(metadata).getIcon(side);
        return null;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        return carverHelper.getVariation(meta).getIcon(world, x, y, z, side);
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    @Override
    public int getRenderType() {
        return Chisel.RenderCTMId;
    }

    @Override
    public RenderVariation getVariation(int metadata) {

        return carverHelper.getVariation(metadata);
    }

    @Override
    public CarvableHelper getHelper() {
        return carverHelper;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        BlockGlassCarvable.pass=pass;
        return true;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
}
