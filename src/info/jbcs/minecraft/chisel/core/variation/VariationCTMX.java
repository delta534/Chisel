package info.jbcs.minecraft.chisel.core.variation;


import cpw.mods.fml.common.FMLLog;
import info.jbcs.minecraft.utilities.Subdivider;
import net.minecraft.client.renderer.Tessellator;
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
import info.jbcs.minecraft.chisel.util.QuadDivider;

public class VariationCTMX extends CarvableVariation {
    private final QuadDivider subDivider = new QuadDivider();
    int [] regionRemap={2,3,0,1};
    public RenderBlocksCTM temp = new RenderBlocksCTM();



    public int ti=0;
    private int m_side;
    private Vector3 loc;
    private IBlockAccess w;
    private int region;
    private Vector3 midpoint;
    private  boolean runTest=true;
    Icon getIcon(int side, int index) {

//        int j = CTM.getSubmapIndices(w, (int) loc.x, (int) loc.y, (int) loc.z,
//              side)[index];

        if(runTest)
        {
            runTest=false;
            FMLLog.info("Data start");
            FMLLog.info(midpoint.toString());
            FMLLog.info("Data end");
        }
        int j=CTM.getTexture(w, (int) loc.x, (int) loc.y, (int) loc.z,side,index,midpoint);
       // j=CTM.mappings[3][index];
        return  j >= 16 ? submapSmall.icons[j-16] : submap.icons[j];
        //return submap.icons[15];

    }

    @Override
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix, int color) {

        Vertex5 []data = null;
        for (int j = 0; j < Subdivider.numResults; j++) {

            Subdivider.Result res=Subdivider.getResult(j);
                         CCRenderState.useModelColours(false);
            CCRenderState.useNormals(false);
            Tessellator.instance.setColorOpaque(255, 255, 255);
            setRegion(res.iconIndex);
            midpoint=res.offset;
            data=res.verts_;
            if (data != null) {
                super.renderSide(data, side % 6, pos, lightMatrix, color);
            }

        }
        return true;

    }
    int octRegion;
    void setOctRegion(int i){octRegion=i;}
    void setRegion(int i) {
        region = i;
    }


    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos,
                      IBlockAccess world) {
        this.subDivider.setup(verts, side);
        Subdivider.setup(verts,side);
        w = world;
        loc = pos;
        m_side = side % 6;


    }




    @Override
    public void transform(UV uv) {
        Icon i = getIcon(m_side, region);
        double diffu = i.getMaxU() - i.getMinU();
        double diffv = i.getMaxV() - i.getMinV();
        uv.u = i.getMinU()+diffu*uv.u;
        uv.v = i.getMinV()+diffv*uv.v;

    }

    @Override
    public String toString() {
        return this.blockName+":"+Integer.toString(this.blockMeta);
    }
}
