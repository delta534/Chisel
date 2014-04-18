package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockGlassCarvable extends BlockGlass implements Carvable {
    public CarvableHelper carverHelper;
    public static int pass;
    public BlockGlassCarvable(int i) {
        super( Material.glass, false);

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
    public IIcon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
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
    public void getSubBlocks(Item blockId, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    @Override
    public RenderVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
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

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }
}
