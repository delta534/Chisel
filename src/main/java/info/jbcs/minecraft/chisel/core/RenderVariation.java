package info.jbcs.minecraft.chisel.core;

import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.UVTransformation;
import codechicken.lib.render.uv.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.render.uv.UV;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.IrreversibleTransformationException;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.blocks.BlockGlassCarvable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;

import javax.swing.*;

public class RenderVariation extends UVTransformation {
    public String blockName;
    public String description;
    public int metadata;
    public int kind;

    public Block block;
    public int blockMeta;

    public String texture;

    public IIcon icon;
    public IIcon overlay;

    public IIcon getBoundIcon() {
        return boundIcon;
    }

    public void setBoundIcon(IIcon boundIcon) {
        this.boundIcon = boundIcon;
    }

    public IIcon boundIcon;

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
            boundIcon = getIcon(world, (int) pos.x, (int) pos.y, (int) pos.z, side);
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
    public IIcon getIcon(int side) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public void apply(UV uv) {
        uv.u = boundIcon.getInterpolatedU(uv.u % 2 * 16);
        uv.v = boundIcon.getInterpolatedV(uv.v % 2 * 16);
    }
    @Override
    public UVTransformation inverse() {
        throw new IrreversibleTransformationException(this);
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

            apply(uv.set(vert.uv));
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
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcon(String modName, Block block_,
                             IIconRegister register) {
        if (block != null) {
            icon = block.getIcon(2, blockMeta);
        } else
            icon = getIconResource(modName + ":"
                    + texture, register);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconResource(String resource, IIconRegister registry) {
        return registry.registerIcon(resource);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIndexedIcon(int index) {
        return icon;
    }


}

