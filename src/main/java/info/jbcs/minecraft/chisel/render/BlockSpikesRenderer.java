package info.jbcs.minecraft.chisel.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockSpikesRenderer implements ISimpleBlockRenderingHandler {
    public static int id;

    public BlockSpikesRenderer() {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
        Drawing.drawBlock(block, meta, renderer);
    }

    void drawSpike(IIcon icon, double r, double h, double x, double y, double z, double dx, double dz) {
        Tessellator tessellator = Tessellator.instance;

        double u0 = icon.getMinU();
        double u1 = icon.getMaxU();
        double v0 = icon.getMaxV();
        double v1 = icon.getMinV();

        double tx = x + dx;
        double ty = y + h;
        double tz = z + dz;

        tessellator.addVertexWithUV(x + r, y + 0, z + r, u0, v0);
        tessellator.addVertexWithUV(x + r, y + 0, z - r, u1, v0);
        tessellator.addVertexWithUV(tx, ty, tz, u1, v1);
        tessellator.addVertexWithUV(tx, ty, tz, u0, v1);

        tessellator.addVertexWithUV(x - r, y + 0, z + r, u1, v0);
        tessellator.addVertexWithUV(x + r, y + 0, z + r, u0, v0);
        tessellator.addVertexWithUV(tx, ty, tz, u1, v1);
        tessellator.addVertexWithUV(tx, ty, tz, u0, v1);


        tessellator.addVertexWithUV(x + r, y + 0, z - r, u1, v0);
        tessellator.addVertexWithUV(x - r, y + 0, z - r, u0, v0);
        tessellator.addVertexWithUV(tx, ty, tz, u1, v1);
        tessellator.addVertexWithUV(tx, ty, tz, u0, v1);

        tessellator.addVertexWithUV(x - r, y + 0, z - r, u1, v0);
        tessellator.addVertexWithUV(x - r, y + 0, z + r, u0, v0);
        tessellator.addVertexWithUV(tx, ty, tz, u1, v1);
        tessellator.addVertexWithUV(tx, ty, tz, u0, v1);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);

        renderer.renderStandardBlock(block, x, y, z);

        Tessellator tessellator = Tessellator.instance;
        IIcon icon = Chisel.blockSpiketrap.iconSpike;
//		IIcon icon=Chisel.blockSpiketrap.iconBase;

        double h = 0.85;
        double d = 1.5 / 16;
        double r = d / 2;


        double tx = x + 0.25;
        double ty = y + h;
        double tz = z + 0.25;
        double skew = 0.5;

        //       tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.addTranslation(0, 1.0f / 16, 0);

        Random rand = new Random();
        for (int xx = 0; xx < 4; xx++) {
            for (int yy = 0; yy < 4; yy++) {
//				float sx=1.0f/16+rand.nextFloat()*13.0f/16;
//				float sz=1.0f/16+rand.nextFloat()*13.0f/16;
                float sx = (2.0f + (12.0f - (float) d) * xx / 3) / 16;
                float sz = (2.0f + (12.0f - (float) d) * yy / 3) / 16;


                tessellator.addTranslation(sx, 0, sz);
                drawSpike(icon, r, 0.5 + rand.nextDouble() * 0.35, x, y, z, rand.nextDouble() * skew - skew / 2, rand.nextDouble() * skew - skew / 2);
                tessellator.addTranslation(-sx, 0, -sz);
            }
        }

        tessellator.addTranslation(0, -1.0f / 16, 0);


        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }
}