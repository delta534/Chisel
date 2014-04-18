package info.jbcs.minecraft.chisel.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleStairs;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockMarbleStairsRenderer implements ISimpleBlockRenderingHandler {
    public static int id;

    public BlockMarbleStairsRenderer() {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
        Drawing.drawBlock(block, meta, renderer);
        renderer.setRenderBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
        Drawing.drawBlock(block, meta, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block blck, int modelId, RenderBlocks renderer) {
        if (blck == null || !(blck instanceof BlockMarbleStairs))
            return false;
        BlockMarbleStairs block = (BlockMarbleStairs) blck;

        renderer.renderBlockStairs(block, x, y, z);

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

    public int getRenderBlockPass() {
        return 1;
    }

}
