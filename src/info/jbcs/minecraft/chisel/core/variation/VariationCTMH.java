package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMH extends CarvableVariation {
    public TextureSubmap			seamsCtmVert;

    protected String extention;
    public VariationCTMH()
    {
        extention="-ctmh";

    }

    @Override
    public Icon getIcon(int side) {
        if (side<2)
            return super.getIcon(side);
        return seamsCtmVert.icons[0];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        if (side < 2)
            return icon;

        int id = world.getBlockId(x, y, z);

        boolean p;
        boolean n;
        boolean reverse = side == 2 || side == 4;

        if (side < 4) {
            p = ConnectionCheckManager.checkConnection(world, x - 1, y, z, id, metadata);
            n = ConnectionCheckManager.checkConnection(world, x + 1, y, z, id, metadata);
        } else {
            p = ConnectionCheckManager.checkConnection(world, x, y, z + 1, id, metadata);
            n = ConnectionCheckManager.checkConnection(world, x, y, z - 1, id, metadata);
        }

        if (p && n)
            return seamsCtmVert.icons[1];
        else if (p)
            return seamsCtmVert.icons[reverse ? 2 : 3];
        else if (n)
            return seamsCtmVert.icons[reverse ? 3 : 2];
        return seamsCtmVert.icons[0];
    }

    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        seamsCtmVert = new TextureSubmap(
                getIconResource(modName + ":" + texture
                        + extention,register), 2, 2);
        icon = getIconResource(modName + ":"
                + texture + "-top",register);
    }
    @Override
    public Icon getIndexedIcon(int index) {
        if(index>=4) return icon;

        return seamsCtmVert.icons[index];
    }
}
