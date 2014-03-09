package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.math.MathHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationV9 extends CarvableVariation {
    int size;
    int len;
    String ext;
    int defIndex;
    public TextureSubmap			variations9;

    public VariationV9(int size_,int defaultIndex)
    {
        size=size_;
        len= MathHelper.floor_double(Math.sqrt(size_));
        ext=String.format("-v%d",size);
        defIndex=defaultIndex;
    }

    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        variations9 = new TextureSubmap(
                getIconResource(modName + ":" + texture
                        + ext,register), len, len);
    }

    @Override
    public Icon getIcon(int side) {
        return variations9.icons[defIndex];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int index = x + y * 606731 + z * 571163 + side * 555491;
        if (index < 0)
            index = -index;

        return variations9.icons[index
                % size];
    }
}
