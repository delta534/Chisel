package info.jbcs.minecraft.chisel.core.variation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.CTM;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTM3 extends RenderVariation {
    TextureSubmap seams[];

    public VariationCTM3() {
        seams = new TextureSubmap[3];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int tex = CTM.getTexture(world, x, y, z, side);

        int row = (tex & 15);
        int col = (tex & 48) >> 4;

        return seams[col].icons[row];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side) {
        return seams[0].icons[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        seams[0] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm1", register), 4, 4);
        seams[1] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm2", register), 4, 4);
        seams[2] = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm3", register), 4, 4);
    }
}
