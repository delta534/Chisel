package info.jbcs.minecraft.chisel.render;

import codechicken.lib.render.*;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMV;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMX;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockAdvancedMarbleRenderer implements ISimpleBlockRenderingHandler {
    RenderBlocksCTM rendererCTM=new RenderBlocksCTM();
    RenderBlocksColumn rendererColumn=new RenderBlocksColumn();
    VariationCTMX test;
    LazyLightMatrix lightmatrix=new LazyLightMatrix();
    CCModel model;
    Cuboid6 blockBounds;
    Vertex5 verts[]=new Vertex5[4];
    Vector3 pos=new Vector3();
    public BlockAdvancedMarbleRenderer() {
        blockBounds=new Cuboid6(
                0,0,0,
                1,1,1);
        if(Chisel.RenderCTMId==0){
            Chisel.RenderCTMId = RenderingRegistry.getNextAvailableRenderId();
        }
        test=new VariationCTMX();
        test.temp=rendererCTM;
        test.temp.resetVertices();
        model=CCModel.quadModel(24);
        model.generateBlock(0,blockBounds);

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        //GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        CarvableVariation var=((Carvable) block).getVariation(metadata);
        pos.set(0,0,0);
        CCRenderState.reset();
        TextureUtils.bindAtlas(0);
        CCRenderState.useNormals(true);
        CCRenderState.useModelColours(true);
        CCRenderState.pullLightmap();

        for(int i=0;i<6;i++)
        {

            CCRenderState.startDrawing(7);
            for(int j=0;j<4;j++)
            {
                verts[j]=model.verts[j+i*4];
            }
            var.setup(verts,i,pos,null);
            var.renderSide(verts, i, pos, null, block.getRenderColor(i) << 8 | 0xFF,blockBounds);
            CCRenderState.draw();


        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        //GL11.glPopMatrix();
    }
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {

        int meta = world.getBlockMetadata(x, y, z);
        CarvableVariation var=((Carvable) block).getVariation(meta);
        pos.set(x, y, z);
        lightmatrix.setPos(world, x, y, z);

        switch(var==null?-1:var.kind){
            case -1:
                return rendererOld.renderStandardBlock(block,x,y,z);
            default:
                rendererCTM.blockAccess=world;
                rendererCTM.renderMaxX=1.0;
                rendererCTM.renderMaxY=1.0;
                rendererCTM.renderMaxZ=1.0;


                Tessellator.instance.setColorOpaque(255,255,255);

                for(int i=0;i<6;i++)
                {


                    for(int j=0;j<4;j++)
                    {
                        verts[j]=model.verts[j+i*4];
                    }
                    var.setup(verts, i, pos, world);
                    var.renderSide(verts, i, pos, lightmatrix.lightMatrix(), block.colorMultiplier(world,x,y,z)<<8|0xFF,blockBounds);



                }
                return true;
        }
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
