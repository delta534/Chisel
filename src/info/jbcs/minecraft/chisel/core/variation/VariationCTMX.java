package info.jbcs.minecraft.chisel.core.variation;


import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import cpw.mods.fml.common.FMLLog;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.chisel.util.Subdivider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import codechicken.lib.lighting.LightMatrix;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.CTM;
import info.jbcs.minecraft.chisel.render.RenderBlocksCTM;

import java.util.Vector;

public class VariationCTMX extends CarvableVariation {

    public RenderBlocksCTM temp = new RenderBlocksCTM();


    public TextureSubmap submap;
    public TextureSubmap submapSmall;
    private Vector3 loc;
    private IBlockAccess w;    private int side;
    int region;
    void bindIcon(int side, int index) {

        if(useCTM&&w!=null)
        {
            int j=CTM.getTexture(w, (int) loc.x, (int) loc.y, (int) loc.z,side,index,midpoint);
            boundIcon=  j >= 16 ? submapSmall.icons[j-16] : submap.icons[j];
        }
        else
        {
            boundIcon= icon;
        }

    }
    final static Vector3[] filters={new Vector3(1,0,1),new Vector3(1,1,0),new Vector3(0,1,1)};
    final static Vector3[] filters2={new Vector3(0,1,0),new Vector3(0,0,1),new Vector3(1,0,0)};
    @Override
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix,int color,Cuboid6 bounds) {

        Vertex5 []data = null;
        Vector3 vec=bounds.center();
        if(useCTM&&w!=null)
        {
            for (int j = 0; j < Subdivider.numResults; j++) {

                Subdivider.Result res=Subdivider.getResult(j);
                CCRenderState.useNormals(false);
                midpoint.set(vec).multiply(filters2[side/2]).add(res.offset.multiply(filters[side/2]));
                region=res.iconIndex;
                data=res.verts_;
                axis.set(Rotation.axes[side % 6]).multiply(vec).multiply(2);
                axis.add(vec);
                axis.add(pos);
                int meta=w.getBlockMetadata((int)pos.x,(int)pos.y,(int)pos.z);
                int id=w.getBlockId((int)pos.x,(int)pos.y,(int)pos.z);
                if (data != null){
                   boolean x= !ConnectionCheckManager.checkConnection(w,axis.x,axis.y,axis.z,id,meta);
                    super.renderSide(data, side % 6, pos, lightMatrix, color,bounds);
                }
            }
        }
        else
        {
                super.renderSide(verts, side, pos, lightMatrix, color,bounds);
        }
        return true;

    }


    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos,
                      IBlockAccess world) {
        Subdivider.setup(verts,side);
        w = world;
        loc = pos;
        this.side=side;

    }

    @Override
    public void transform(UV uv) {
        bindIcon(side,region);
        Icon i = boundIcon;
        double diffu = i.getMaxU() - i.getMinU();
        double diffv = i.getMaxV() - i.getMinV();
        uv.u = i.getMinU()+diffu*uv.u;
        uv.v = i.getMinV()+diffv*uv.v;
    }

    @Override
    public String toString() {
        return this.blockName+":"+Integer.toString(this.blockMeta);
    }

    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        icon = getIconResource(modName + ":"
                + texture,register);
        submap = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm",register), 4, 4);
        submapSmall = new TextureSubmap(icon, 2, 2);
    }
}
