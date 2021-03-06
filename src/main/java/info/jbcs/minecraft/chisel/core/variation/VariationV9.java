package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.math.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class VariationV9 extends RenderVariation {
    int size;
    int len;
    String ext;
    int defIndex;
    public TextureSubmap variations9;

    public VariationV9(int size_, int defaultIndex) {
        size = size_;
        len = MathHelper.floor_double(Math.sqrt(size_));
        ext = String.format("-v%d", size);
        defIndex = defaultIndex;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(String modName, Block block, IIconRegister register) {
        variations9 = new TextureSubmap(
                getIconResource(modName + ":" + texture
                        + ext, register), len, len);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side) {
        return variations9.icons[defIndex];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int index = x + y * 606731 + z * 571163 + side * 555491;
        if (index < 0)
            index = -index;

        return variations9.icons[index
                % size];
    }
}
