package info.jbcs.minecraft.chisel.core.variation;


import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.CTM;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.chisel.util.Subdivider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMX extends RenderVariation {
    public TextureSubmap submap;
    public TextureSubmap submapSmall;
    private Vector3 loc;
    private IBlockAccess w;
    private int side;
    int region;

    @SideOnly(Side.CLIENT)
    void bindIcon(int side, int index) {

        if (useCTM && w != null) {
            int j = CTM.getTexture(w, (int) loc.x, (int) loc.y, (int) loc.z, side, index, midpoint);
            boundIcon = j >= 16 ? submapSmall.icons[j - 16] : submap.icons[j];
        } else {
            boundIcon = icon;
        }

    }

    final static Vector3[] filters = {new Vector3(1, 0, 1),new Vector3(1, 0, 1),  new Vector3(1, 1, 0), new Vector3(1, 1, 0), new Vector3(0, 1, 1), new Vector3(0, 1, 1)};
    final static Vector3[] offsets = {new Vector3(0, -1, 0),new Vector3(0, 1, 0), new Vector3(0, 0, -1),new Vector3(0, 0, 1), new Vector3(-1, 0, 0), new Vector3(1, 0, 0)};
    final Vector3 store=new Vector3();
    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderSide(Vertex5[] verts, int side, Vector3 pos,
                              LightMatrix lightMatrix, int color, Cuboid6 bounds) {

        Vertex5[] data = null;
        Vector3 vec = bounds.max.copy();
        vec.sub(bounds.min).add(0.0001).multiply(offsets[side%6]).multiply(0.5);

        if (useCTM && w != null) {
            for (int j = 0; j < Subdivider.numResults; j++) {

                Subdivider.Result res = Subdivider.getResult(j);
                CCRenderState.useNormals(false);
                midpoint.set(res.offset);
                region = res.iconIndex;
                data = res.verts_;
                axis.set(vec);
                axis.add(pos);
                axis.add(midpoint);
                int meta = w.getBlockMetadata((int) pos.x, (int) pos.y, (int) pos.z);
                int id = w.getBlockId((int) pos.x, (int) pos.y, (int) pos.z);

                if (data != null) {
                    boolean x = !ConnectionCheckManager.checkConnection(w, axis.x, axis.y, axis.z, id, meta);
                    if(x)
                    {
                    super.renderSide(data, side % 6, pos, lightMatrix, color, bounds);
                    }
                }
            }
        } else {
            super.renderSide(verts, side, pos, lightMatrix, color, bounds);
        }
        return true;

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos,
                      IBlockAccess world, Cuboid6 bounds) {
        Subdivider.setup(verts, side);
        w = world;
        loc = pos;
        this.side = side;

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void transform(UV uv) {
        bindIcon(side, region);
        Icon i = boundIcon;
        double diffu = i.getMaxU() - i.getMinU();
        double diffv = i.getMaxV() - i.getMinV();
        uv.u = i.getMinU() + diffu * uv.u;
        uv.v = i.getMinV() + diffv * uv.v;
    }

    @Override
    public String toString() {
        return this.blockName + ":" + Integer.toString(this.blockMeta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        icon = getIconResource(modName + ":"
                + texture, register);
        submap = new TextureSubmap(getIconResource(modName
                + ":" + texture + "-ctm", register), 4, 4);
        submapSmall = new TextureSubmap(icon, 2, 2);
    }
}
