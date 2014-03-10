package info.jbcs.minecraft.chisel.core.variation;


import cpw.mods.fml.common.FMLLog;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
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

public class VariationCTMX extends CarvableVariation {

    public RenderBlocksCTM temp = new RenderBlocksCTM();


    public TextureSubmap submap;
    public TextureSubmap submapSmall;
    private Vector3 loc;
    private IBlockAccess w;
    private Vector3 midpoint;
    private int side;
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

    @Override
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix, int color) {

        Vertex5 []data = null;
        if(useCTM&&w!=null)
        {
            for (int j = 0; j < Subdivider.numResults; j++) {

                Subdivider.Result res=Subdivider.getResult(j);
                CCRenderState.useModelColours(false);
                CCRenderState.useNormals(false);
                Tessellator.instance.setColorOpaque(255, 255, 255);
                midpoint=res.offset;
                //bindIcon(side%6,res.iconIndex);
                region=res.iconIndex;
                data=res.verts_;
                if (data != null) {
                    super.renderSide(data, side % 6, pos, lightMatrix, color);
                }

            }
        }
        else
            super.renderSide(verts, side, pos, lightMatrix, color);
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
