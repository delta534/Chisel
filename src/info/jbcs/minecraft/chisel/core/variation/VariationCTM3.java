package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.CTM;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTM3 extends CarvableVariation {
    TextureSubmap seams[];
    public VariationCTM3()
    {
        seams= new TextureSubmap[3];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int tex = CTM.getTexture(world, x, y, z, side);

        int row = tex / 16;
        int col = tex % 16;

        return seams[col / 4].icons[col % 4 + row * 4];
    }

    @Override
    public Icon getIcon(int side) {
        return seams[0].icons[0];
    }

    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        seams[0] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm1",register), 4, 4);
        seams[1] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm2",register), 4, 4);
        seams[2] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm3",register), 4, 4);
    }
}
