package info.jbcs.minecraft.chisel.render;

import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import org.lwjgl.opengl.GL11;

import info.jbcs.minecraft.chisel.blocks.BlockChiselGrass;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class ChiselGrassRenderBlocks extends RenderBlocksCTM {

	
	float r;
	float g;
	float b;
	int meta;
	public Icon iconTop;
	int kind;
	RenderBlocks renderer;
	 public Icon sides[]=new Icon[6];
	 boolean connected(IBlockAccess world, int x,int y,int z,int id,int meta){
			return ConnectionCheckManager.checkConnection(world, x, y, z, id, meta);
			
		}
	@Override
	public boolean renderStandardBlock(Block block, int x, int y, int z)
	{
		
		if(kind==1)
			renderer=this;
		else
			renderer=this.rendererOld;
		int l = block.colorMultiplier(rendererOld.blockAccess, x, y, z);
		r = (float)(l >> 16 & 255) / 255.0F;
		g = (float)(l >> 8 & 255) / 255.0F;
		b= (float)(l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable)
		{
			float f3 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
			float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
			float f5 = (r * 30.0F + b * 70.0F) / 100.0F;
			r = f3;
			r = f4;
			r = f5;
		}
		blockAccess=rendererOld.blockAccess;
		switch(kind)
		{
		case 0:
			return super.renderStandardBlockCTM(block, x, y, z);
		case 1:
			int metadata=blockAccess.getBlockMetadata(x, y, z);
			int id=block.blockID;
			boolean yp=connected(blockAccess,x,y+1,z,id,metadata);
			boolean yn=connected(blockAccess,x,y-1,z,id,metadata);
			
			if(yp || yn){
				sides[0]=iconTop;
				sides[1]=iconTop;
				
				if(yp && yn)
					sides[2]=submap.icons[2];
				else if(yp)
					sides[2]=submap.icons[3];
				else
					sides[2]=submap.icons[1];
				
				sides[3]=sides[4]=sides[5]=sides[2];
			} else{
				boolean xp=connected(blockAccess,x+1,y,z,id,metadata);
				boolean xn=connected(blockAccess,x-1,y,z,id,metadata);
				
				if(xp && (connected(blockAccess,x+1,y+1,z,id,metadata) || connected(blockAccess,x+1,y-1,z,id,metadata)))
					xp=false;
				if(xn && (connected(blockAccess,x-1,y+1,z,id,metadata) || connected(blockAccess,x-1,y-1,z,id,metadata)))
					xn=false;
				
				if(xp || xn){
		        	uvRotateEast = 2;
		            uvRotateWest = 1;
		            uvRotateTop = 1;
		            uvRotateBottom = 1;
		            
					sides[4]=iconTop;
					sides[5]=iconTop;
					
					if(xp && xn)
						sides[0]=submap.icons[2];
					else if(xp)
						sides[0]=submap.icons[3];
					else
						sides[0]=submap.icons[1];
					
					sides[1]=sides[2]=sides[3]=sides[0];
				} else{
					boolean zp=connected(blockAccess,x,y,z+1,id,metadata);
					boolean zn=connected(blockAccess,x,y,z-1,id,metadata);
					
					if(zp && (connected(blockAccess,x,y+1,z+1,id,metadata) || connected(blockAccess,x,y-1,z+1,id,metadata)))
						zp=false;
					if(zp && (connected(blockAccess,x+1,y,z+1,id,metadata) || connected(blockAccess,x-1,y,z+1,id,metadata)))
						zp=false;
					if(zn && (connected(blockAccess,x,y+1,z-1,id,metadata) || connected(blockAccess,x,y-1,z-1,id,metadata)))
						zn=false;
					if(zn && (connected(blockAccess,x+1,y,z-1,id,metadata) || connected(blockAccess,x-1,y,z-1,id,metadata)))
						zn=false;
					
					if(zp || zn){
			        	uvRotateSouth = 1;
			            uvRotateNorth = 2;
			            
						sides[2]=iconTop;
						sides[3]=iconTop;
						
						if(zp && zn)
							sides[0]=submap.icons[2];
						else if(zp)
							sides[0]=submap.icons[1];
						else
							sides[0]=submap.icons[3];
						
						sides[1]=sides[4]=sides[5]=sides[0];
					} else{
						sides[0]=sides[1]=iconTop;
						sides[2]=sides[3]=sides[4]=sides[5]=submap.icons[0];
					}
				}
			}
			

	        boolean flag = super.renderStandardBlock(block, x, y, z);
	        
	        uvRotateSouth = 0;
	        uvRotateEast = 0;
	        uvRotateWest = 0;
	        uvRotateNorth = 0;
	        uvRotateTop = 0;
	        uvRotateBottom = 0;

			return flag;
		default:
			return super.renderStandardBlock(block, x, y, z);
		}
	}
	@Override
	public void renderFaceXNeg(Block block, double x, double y, double z, Icon icon){

		
		if( Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0)
		{
			renderer.renderFaceXNeg(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				renderer.colorRedTopLeft *= r;
				renderer.colorRedBottomLeft *= r;
				renderer.colorRedBottomRight *= r;
				renderer.colorRedTopRight *= r;
				renderer.colorGreenTopLeft *= g;
				renderer.colorGreenBottomLeft *= g;
				renderer.colorGreenBottomRight *= g;
				renderer.colorGreenTopRight *= g;
				renderer.colorBlueTopLeft *= b;
				renderer.colorBlueBottomLeft *= b;
				renderer.colorBlueBottomRight *= b;
				renderer.colorBlueTopRight *= b;
				BlockChiselGrass bl=(BlockChiselGrass)block; 
				renderer.renderFaceXNeg(block, x,y,z, bl.getIconSideOverlay());
			}

		}
		else
		{
			Tessellator tessellator = Tessellator.instance;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			float f7 = f4 * r;
			float f8 = f4 * g;
			float f9 = f4 * b;
			float f10 = f3;
			float f11 = f5;
			float f12 = f6;
			float f13 = f3;
			float f14 = f5;
			float f15 = f6;
			float f16 = f3;
			float f17 = f5;
			float f18 = f6;
			renderer.renderFaceXNeg(block,x,y,z,icon);
			

			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				BlockChiselGrass bl=(BlockChiselGrass)block; 
				tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
				renderer.renderFaceXNeg(block, x,y,z, bl.getIconSideOverlay(meta));
			}
		}
	}
	@Override
	public void renderFaceYPos(Block block, double x, double y, double z, Icon icon){
		this.rendererOld.renderFaceYPos(block, x, y, z, icon);
	}
	@Override
	public void renderFaceXPos(Block block, double x, double y, double z, Icon icon){
		if( Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0)
		{
			renderer.renderFaceXPos(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				renderer.colorRedTopLeft *= r;
				renderer.colorRedBottomLeft *= r;
				renderer.colorRedBottomRight *= r;
				renderer.colorRedTopRight *= r;
				renderer.colorGreenTopLeft *= g;
				renderer.colorGreenBottomLeft *= g;
				renderer.colorGreenBottomRight *= g;
				renderer.colorGreenTopRight *= g;
				renderer.colorBlueTopLeft *= b;
				renderer.colorBlueBottomLeft *= b;
				renderer.colorBlueBottomRight *= b;
				renderer.colorBlueTopRight *= b;
				BlockChiselGrass bl=(BlockChiselGrass)block; 

				renderer.renderFaceXPos(block, x,y,z, bl.getIconSideOverlay(meta));
			}

		}
		else
		{
			Tessellator tessellator = Tessellator.instance;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			float f7 = f4 * r;
			float f8 = f4 * g;
			float f9 = f4 * b;
			float f10 = f3;
			float f11 = f5;
			float f12 = f6;
			float f13 = f3;
			float f14 = f5;
			float f15 = f6;
			float f16 = f3;
			float f17 = f5;
			float f18 = f6;
			renderer.renderFaceXPos(block,x,y,z,icon);
			

			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				BlockChiselGrass bl=(BlockChiselGrass)block; 
				tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
				renderer.renderFaceXPos(block, x,y,z, bl.getIconSideOverlay(meta));
			}
		}
	}

	@Override
	public void renderFaceZNeg(Block block, double x, double y, double z, Icon icon){
		if( Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0)
		{
			renderer.renderFaceZNeg(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				renderer.colorRedTopLeft *= r;
				renderer.colorRedBottomLeft *= r;
				renderer.colorRedBottomRight *= r;
				renderer.colorRedTopRight *= r;
				renderer.colorGreenTopLeft *= g;
				renderer.colorGreenBottomLeft *= g;
				renderer.colorGreenBottomRight *= g;
				renderer.colorGreenTopRight *= g;
				renderer.colorBlueTopLeft *= b;
				renderer.colorBlueBottomLeft *= b;
				renderer.colorBlueBottomRight *= b;
				renderer.colorBlueTopRight *= b;
				BlockChiselGrass bl=(BlockChiselGrass)block; 

				renderer.renderFaceXNeg(block, x,y,z, bl.getIconSideOverlay(meta));
			}

		}
		else
		{
			Tessellator tessellator = Tessellator.instance;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			float f7 = f4 * r;
			float f8 = f4 * g;
			float f9 = f4 * b;
			float f10 = f3;
			float f11 = f5;
			float f12 = f6;
			float f13 = f3;
			float f14 = f5;
			float f15 = f6;
			float f16 = f3;
			float f17 = f5;
			float f18 = f6;
			renderer.renderFaceZNeg(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
				BlockChiselGrass bl=(BlockChiselGrass)block; 

				renderer.renderFaceZNeg(block, x,y,z, bl.getIconSideOverlay(meta));
			}
		}
	}

	@Override
	public void renderFaceZPos(Block block, double x, double y, double z, Icon icon){
		if( Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0)
		{
			renderer.renderFaceZPos(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				renderer.colorRedTopLeft *= r;
				renderer.colorRedBottomLeft *= r;
				renderer.colorRedBottomRight *= r;
				renderer.colorRedTopRight *= r;
				renderer.colorGreenTopLeft *= g;
				renderer.colorGreenBottomLeft *= g;
				renderer.colorGreenBottomRight *= g;
				renderer.colorGreenTopRight *= g;
				renderer.colorBlueTopLeft *= b;
				renderer.colorBlueBottomLeft *= b;
				renderer.colorBlueBottomRight *= b;
				renderer.colorBlueTopRight *= b;
				BlockChiselGrass bl=(BlockChiselGrass)block; 

				renderer.renderFaceZPos(block, x,y,z, bl.getIconSideOverlay(meta));
			}

		}
		else
		{
			Tessellator tessellator = Tessellator.instance;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			float f7 = f4 * r;
			float f8 = f4 * g;
			float f9 = f4 * b;
			float f10 = f3;
			float f11 = f5;
			float f12 = f6;
			float f13 = f3;
			float f14 = f5;
			float f15 = f6;
			float f16 = f3;
			float f17 = f5;
			float f18 = f6;
			renderer.renderFaceZPos(block,x,y,z,icon);
			if (fancyGrass && !this.hasOverrideBlockTexture()&&block instanceof BlockChiselGrass)
			{
				BlockChiselGrass bl=(BlockChiselGrass)block; 

				tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
				renderer.renderFaceZPos(block, x,y,z, bl.getIconSideOverlay(meta));
			}
		}
	}
	@Override
	public void renderFaceYNeg(Block block, double x, double y, double z, Icon icon){
		renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.5F;
		renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.5F;
		renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.5F;
		renderer.renderFaceYNeg(block,x,y,z,icon);
	}
}
