package info.jbcs.minecraft.chisel.render;

import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleSlab;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMX;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockAdvancedMarbleRenderer implements ISimpleBlockRenderingHandler {
    RenderBlocksCTM rendererCTM = new RenderBlocksCTM();
    RenderBlocksColumn rendererColumn = new RenderBlocksColumn();
    VariationCTMX test;
    LazyLightMatrix lightmatrix = new LazyLightMatrix();
    CCModel model;
    Vertex5 verts[] = new Vertex5[4];
    Vector3 pos = new Vector3();

    public BlockAdvancedMarbleRenderer() {
        if (Chisel.RenderCTMId == 0) {
            Chisel.RenderCTMId = RenderingRegistry.getNextAvailableRenderId();
        }
        model = CCModel.quadModel(24);

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        //GL11.glPushMatrix();
        block.setBlockBoundsForItemRender();
        Cuboid6 blockBounds = new Cuboid6(
                block.getBlockBoundsMinX(),
                block.getBlockBoundsMinY(),
                block.getBlockBoundsMinZ(),
                block.getBlockBoundsMaxX(),
                block.getBlockBoundsMaxY(),
                block.getBlockBoundsMaxZ());
        model.generateBlock(0, blockBounds);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        RenderVariation var = ((Carvable) block).getVariation(metadata);
        pos.set(0, 0, 0);
        CCRenderState.reset();
        TextureUtils.bindAtlas(0);
        CCRenderState.useNormals(true);
        CCRenderState.useModelColours(true);
        CCRenderState.pullLightmap();

        for (int i = 0; i < 6; i++) {

            CCRenderState.startDrawing(7);
            for (int j = 0; j < 4; j++) {
                verts[j] = model.verts[j + i * 4];
            }
            var.setup(verts, i, pos, null, blockBounds);
            var.renderSide(verts, i, pos, null, block.getRenderColor(i) << 8 | 0xFF, blockBounds);
            CCRenderState.draw();


        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        //GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {

            block.setBlockBoundsBasedOnState(world, x, y, z);
            Cuboid6 blockBounds = new Cuboid6(
                    block.getBlockBoundsMinX(),
                    block.getBlockBoundsMinY(),
                    block.getBlockBoundsMinZ(),
                    block.getBlockBoundsMaxX(),
                    block.getBlockBoundsMaxY(),
                    block.getBlockBoundsMaxZ());
            model.generateBlock(0, blockBounds);
            if (block instanceof BlockMarbleSlab)
                x = x + 0;
            int meta = world.getBlockMetadata(x, y, z);
            RenderVariation var = ((Carvable) block).getVariation(meta);
            CCRenderState.reset();
            CCRenderState.useModelColours(true);
            pos.set(x, y, z);
            lightmatrix.setPos(world, x, y, z);

            switch (var == null ? -1 : var.kind) {
                case -1:
                    return rendererOld.renderStandardBlock(block, x, y, z);
                default:
                    rendererCTM.blockAccess = world;
                    rendererCTM.renderMaxX = 1.0;
                    rendererCTM.renderMaxY = 1.0;
                    rendererCTM.renderMaxZ = 1.0;
                    for (int i = 0; i < 6; i++) {


                        for (int j = 3; j > -1 ; j--) {
                            verts[j] = model.verts[j + i * 4];
                        }
                        //Tessellator.instance.setBrightness(world.getLightBrightnessForSkyBlocks(x,y,z,15));
                        //CCRenderState.pullLightmap();

                        var.setup(verts, i, pos, world, blockBounds);
                        var.renderSide(verts, i, pos, lightmatrix.lightMatrix(), block.colorMultiplier(world, x, y, z) << 8 | 0xFF, blockBounds);


                    }
            }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {

        return true;
    }

    @Override
    public int getRenderId() {
        return Chisel.RenderCTMId;
    }

}
