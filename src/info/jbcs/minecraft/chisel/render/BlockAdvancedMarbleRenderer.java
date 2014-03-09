package info.jbcs.minecraft.chisel.render;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMX;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCModelLibrary;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.Vertex5;
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
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, metadata, renderer);	
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {

		int meta = world.getBlockMetadata(x, y, z);
		Vertex5 verts[]=new Vertex5[4];
		CarvableVariation var=((Carvable) block).getVariation(meta);
		Vector3 pos=new Vector3(x, y, z);
		lightmatrix.setPos(world, x, y, z);
		switch(var==null?0:var.useCTM?var.kind:0){
		case CarvableHelper.CTMX:
			rendererCTM.blockAccess=world;
			rendererCTM.renderMaxX=1.0;
			rendererCTM.renderMaxY=1.0;
			rendererCTM.renderMaxZ=1.0;

			test.submap=var.submap;
			test.submapSmall=var.submapSmall;
            test.useCTM=var.useCTM;
            test.icon=var.icon;
			rendererCTM.rendererOld=rendererOld;
			test.temp=rendererCTM;

			
			model.generateBlock(0,blockBounds);
			for(int i=0;i<6;i++)
			{

				
				for(int j=0;j<4;j++)
				{
                    verts[j]=model.verts[j+i*4];
				}
				test.setup(verts, i, pos, world);
				test.renderSide(verts, i, pos, lightmatrix.lightMatrix(), block.colorMultiplier(world,x,y,z));

				

			}
			return true;
		case CarvableHelper.CTMV:
			rendererColumn.blockAccess=world;
			rendererColumn.renderMaxX=1.0;
			rendererColumn.renderMaxY=1.0;
			rendererColumn.renderMaxZ=1.0;

			rendererColumn.submap=var.seamsCtmVert;        	
			rendererColumn.iconTop=var.iconTop;   

			return rendererColumn.renderStandardBlock(block,x,y,z);        	
		default:
			return rendererOld.renderStandardBlock(block,x,y,z);
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
