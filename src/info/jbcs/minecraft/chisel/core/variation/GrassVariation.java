package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.core.CarvableVariation;

import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class GrassVariation extends CarvableVariation {
    CarvableVariation grass;
    CarvableVariation dirt;

    GrassVariation(CarvableVariation vgrass,CarvableVariation vdirt)
    {
        grass=vgrass;
        dirt=vdirt;
        this.texture=grass.texture;
        this.description=grass.description;
        this.kind=vgrass.kind;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side) {
        if(side==1)
            return grass.getIcon(side);
        return  dirt.getIcon(side);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        if(side==1)
            return grass.getBlockTexture(world, x, y, z, side);
        return dirt.getBlockTexture(world, x, y, z, side);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIndexedIcon(int index) {
        return grass.getIndexedIcon(index);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos, IBlockAccess world,Cuboid6 bounds) {
        grass.setup(verts, side, pos, world,bounds);
        dirt.setup(verts, side, pos, world,bounds);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix,int color,Cuboid6 bounds) {

        if(grass.isTop(side))
        {
            Tessellator.instance.setColorOpaque_I(color>>8);
            grass.renderSide(verts, side, pos, lightMatrix, color,bounds);
        }
        else
        {
            Tessellator.instance.setColorOpaque_I(0xFFFFFFFF>>8);
            dirt.renderSide(verts, side, pos, lightMatrix, 0xFFFFFFFF,bounds);
            if(dirt.isSide(side))
            {
                //dummy.renderSide(verts, side, pos, lightMatrix, color);
                Tessellator.instance.setColorOpaque_I(color>>8);

                Icon temp=grass.getBoundIcon();
                grass.setBoundIcon(BlockGrass.getIconSideOverlay());
                if(bounds!=null)
                {
                    for(int i=0;i<4;i++)
                    {
                        verts[i].uv.v-=1-bounds.max.y;
                    }
                }
                grass.renderSide(verts, side, pos, lightMatrix, color,bounds);
                grass.setBoundIcon(temp);
            }
        }
        return  true;
    }
}
