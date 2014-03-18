package info.jbcs.minecraft.chisel.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.blocks.BlockSnakestone;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockSnakeStoneRenderer implements ISimpleBlockRenderingHandler {
    public static int id;

    public BlockSnakeStoneRenderer() {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
        setRenderRotate(renderer, (BlockSnakestone) block, meta);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Drawing.drawBlock(block, meta, renderer);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        restoreRendererRotate(renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);

        setRenderRotate(renderer, (BlockSnakestone) block, meta);
        boolean didRender = renderer.renderStandardBlock(block, x, y, z);
        restoreRendererRotate(renderer);

        return didRender;
    }

    private void restoreRendererRotate(RenderBlocks renderer) {
        renderer.uvRotateSouth = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
    }

    private void setRenderRotate(RenderBlocks renderer, BlockSnakestone block, int meta) {
        int type = meta & 0xC;
        int orient = meta & 0x3;


        if (type == 0) {
            switch (orient) {
                case 0:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 2;
                    break;
                case 1:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 1;
                    break;
                case 2:
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 3;
                    break;
                case 3:
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 0;
            }

        } else if ((type == 4) || (type == 8)) {
            switch (orient) {
                case 0:
                    renderer.uvRotateTop = block.flipTopTextures ? 1 : 2;
                    renderer.uvRotateBottom = 1;
                    renderer.uvRotateWest = 2;
                    break;
                case 1:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 1;
                    renderer.uvRotateEast = 2;
                    break;
                case 2:
                    renderer.uvRotateTop = block.flipTopTextures ? 0 : 3;
                    renderer.uvRotateBottom = 0;
                    renderer.uvRotateSouth = 2;
                    break;
                case 3:
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 0;
                    renderer.uvRotateNorth = 2;
            }

        } else if (type == 12) {
            switch (orient) {
                case 0:
                    renderer.uvRotateTop = 0;
                    renderer.uvRotateBottom = 0;
                    break;
                case 1:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 1;
                    break;
                case 2:
                    renderer.uvRotateNorth = 2;
                    renderer.uvRotateSouth = 2;
                    renderer.uvRotateEast = 2;
                    renderer.uvRotateWest = 2;
            }
        }
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }
}