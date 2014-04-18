package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockMarbleWall extends BlockWall {
    CarvableHelper carverHelper;

    public BlockMarbleWall(int id, Block block) {
        super(id, block);

        carverHelper = new CarvableHelper();

        setCreativeTab(Chisel.tabChisel);
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
    public void registerIcons(IIconRegister register) {
        carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(int blockId, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    @Override
    public String getUnlocalizedName() {
        return "Chisel" + carverHelper.blockName;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        BlockGlassCarvable.pass=pass;
        return true;
    }
    @Override
    public int getRenderBlockPass()
    {
        return 0;
    }

}
