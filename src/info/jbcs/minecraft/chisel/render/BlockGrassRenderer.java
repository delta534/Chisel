package info.jbcs.minecraft.chisel.render;

import org.lwjgl.opengl.GL11;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockGrassRenderer implements ISimpleBlockRenderingHandler  {

	static ChiselGrassRenderBlocks grassRenderer=new ChiselGrassRenderBlocks();
	public BlockGrassRenderer()
	{
		if(Chisel.RenderGrassID==0){
			Chisel.RenderGrassID = RenderingRegistry.getNextAvailableRenderId();
		int g=0;
		}
	}

	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, metadata, renderer);	
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		grassRenderer.rendererOld=renderer;
		int meta=world.getBlockId(x,y,z);
		grassRenderer.meta=meta;
		//rendererCTM=grassRenderer;
CarvableVariation var=((Carvable) block).getVariation(meta);

    	switch(var==null?0:var.useCTM?var.kind:0){
    	
    	case CarvableHelper.CTMX:
    		grassRenderer.kind=0;
    		grassRenderer.blockAccess=world;
            grassRenderer.renderMaxX=1.0;
            grassRenderer.renderMaxY=1.0;
            grassRenderer.renderMaxZ=1.0;
            
            //grassRenderer.submap=var.submap;
    		//grassRenderer.submapSmall=var.submapSmall;
    		
            return grassRenderer.renderStandardBlock(block,x,y,z);
        case CarvableHelper.CTMV:
        grassRenderer.kind=1;
        	grassRenderer.blockAccess=world;
        	grassRenderer.renderMaxX=1.0;
        	grassRenderer.renderMaxY=1.0;
            grassRenderer.renderMaxZ=1.0;
            
            //grassRenderer.submap=var.seamsCtmVert;
            //grassRenderer.iconTop=var.iconTop;
 
           return grassRenderer.renderStandardBlock(block,x,y,z);        	
    	default:
    		grassRenderer.kind=3;
    		return grassRenderer.renderStandardBlock(block,x,y,z);
    	}
		
		
	}


	@Override
	public int getRenderId() {
		return Chisel.RenderGrassID;
	}
	
}
