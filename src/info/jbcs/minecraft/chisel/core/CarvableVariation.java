package info.jbcs.minecraft.chisel.core;

import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.IUVTransformation;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.proxyWorld;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class CarvableVariation implements IUVTransformation {
	public String					blockName;
	public String					description;
	public int						metadata;
	public int						kind;

	public Block					block;
	public int						blockMeta;

	public String					texture;

	public Icon					icon;
	public Icon					overlay;
    public Cuboid6 bounds;
    public Icon getBoundIcon() {
        return boundIcon;
    }

    public void setBounds(Cuboid6 bound)
    {
        bounds=bound;
    }

    public void setBoundIcon(Icon boundIcon) {
        this.boundIcon = boundIcon;
    }

    public Icon                 boundIcon;

	public boolean					useCTM;

	public void setup(Vertex5[] verts, int side, Vector3 pos,IBlockAccess world)
	{
        if(world!=null)
        {
            boundIcon=getBlockTexture(world,(int)pos.x,(int)pos.y,(int)pos.z,side);
        }
        else
            boundIcon=getIcon(side);
	}

    public boolean isTop(int side)
    {
        return side==1;
    }

    public boolean isSide(int side)
    {
        return side>1;
    }
    public boolean isBottom(int side)
    {
        return side==0;
    }


	public Icon getIcon(int side)
	{
		return icon;
	}
	public void transform(UV uv)
	{
		uv.u=boundIcon.getInterpolatedU(uv.u%2*16);
		uv.v=boundIcon.getInterpolatedV(uv.v%2*16);
	}
	public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
			LightMatrix lightMatrix,int color)
	{
		Vector3 vec=new Vector3();
		UV uv=new UV();
		Tessellator t=Tessellator.instance;
		for(int i=0;i<4;i++)
		{
			if(CCRenderState.useNormals())
			{
				Vector3 n = Rotation.axes[side%6];
				t.setNormal((float)n.x,(float)n.y,(float)n.z);
			}
			Vertex5 vert = verts[i];
			if(lightMatrix != null)
			{
				LC lc = LC.computeO(vert.vec, side);
				if(CCRenderState.useModelColours())
					lightMatrix.setColour(t, lc,color);
				lightMatrix.setBrightness(t, lc);
			}
			else 
			{
				if(CCRenderState.useModelColours())
					CCRenderState.vertexColour(color);
			}
			
			transform(uv.set(vert.uv));
			t.addVertexWithUV(vert.vec.x+pos.x, vert.vec.y+pos.y, vert.vec.z+pos.z, uv.u, uv.v);
		}
		return true;
	}
	public Icon getBlockTexture(IBlockAccess world,int x,int y,int z, int side)
    {
        return  getIcon(side);
    }


     public void registerIcon(String modName, Block block_,
                              IconRegister register)
     {
         if(block!=null)
         {
            icon=block.getIcon(2,blockMeta);
         }
         else
         icon = getIconResource(modName + ":"
                 + texture,register);
     }
    public Icon getIconResource(String resource,IconRegister registry)
    {
        return registry.registerIcon(resource);
    }
    public Icon getIndexedIcon(int index)
    {
         return  icon;
    }


}

