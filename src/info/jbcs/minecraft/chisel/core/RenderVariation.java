package info.jbcs.minecraft.chisel.core;

import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.IUVTransformation;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.blocks.BlockGlassCarvable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderVariation implements IUVTransformation {
    public String blockName;
    public String description;
    public int metadata;
    public int kind;

    public Block block;
    public int blockMeta;

    public String texture;

    public Icon icon;
    public Icon overlay;

    public Icon getBoundIcon() {
        return boundIcon;
    }

    public void setBoundIcon(Icon boundIcon) {
        this.boundIcon = boundIcon;
    }

    public Icon boundIcon;

    public boolean useCTM;
    boolean[] adj = new boolean[6];
    final public Vector3 axis = new Vector3();
    final public Vector3 midpoint = new Vector3();
    final static Vector3[] microAdjust=
            {new Vector3(0,0.001,0),new Vector3(0,-0.001,0),new Vector3(0,0,0.001)
                    ,new Vector3(0,0,-0.001),new Vector3(0.001,0,0),new Vector3(-0.001,0,0)};
    @SideOnly(Side.CLIENT)
    public void setup(Vertex5[] verts, int side, Vector3 pos, IBlockAccess world, Cuboid6 bounds) {
        if (world != null) {
            boundIcon = getBlockTexture(world, (int) pos.x, (int) pos.y, (int) pos.z, side);
        } else
            boundIcon = getIcon(side);
    }

    public boolean isTop(int side) {
        return side == 1;
    }

    public boolean isSide(int side) {
        return side > 1;
    }

    public boolean isBottom(int side) {
        return side == 0;
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public void transform(UV uv) {
        uv.u = boundIcon.getInterpolatedU(uv.u % 2 * 16);
        uv.v = boundIcon.getInterpolatedV(uv.v % 2 * 16);
    }

    @SideOnly(Side.CLIENT)
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix, int color, Cuboid6 bounds) {
        UV uv = new UV();
        Tessellator t = Tessellator.instance;
        int lim=4;
        int start=0;
        int change=1;
        int p=BlockGlassCarvable.pass;

        for (int i = start; i != lim; i+=change) {
            int s=p==1?(side%2==0)?side%6+1:side%6-1:side%6;
            if (CCRenderState.useNormals()) {
                Vector3 n = Rotation.axes[side% 6];
                t.setNormal((float) n.x, (float) n.y, (float) n.z);
            }
            Vertex5 vert = verts[i];
            if (lightMatrix != null) {

                LC lc = LC.compute(vert.vec, s);
                if (CCRenderState.useModelColours())
                    lightMatrix.setColour(t, lc, color);
                lightMatrix.setBrightness(t, lc);
            } else {
                if (CCRenderState.useModelColours())
                    CCRenderState.vertexColour(color);
            }

            transform(uv.set(vert.uv));
            if(p==1)
                t.addVertexWithUV(vert.vec.x + pos.x+microAdjust[side % 6].x,
                        vert.vec.y + pos.y+microAdjust[side % 6].y,
                        vert.vec.z + pos.z+microAdjust[side % 6].z, uv.u, uv.v);
            else
                t.addVertexWithUV(vert.vec.x + pos.x, vert.vec.y + pos.y, vert.vec.z + pos.z, uv.u, uv.v);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcon(String modName, Block block_,
                             IconRegister register) {
        if (block != null) {
            icon = block.getIcon(2, blockMeta);
        } else
            icon = getIconResource(modName + ":"
                    + texture, register);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconResource(String resource, IconRegister registry) {
        return registry.registerIcon(resource);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIndexedIcon(int index) {
        return icon;
    }


}

