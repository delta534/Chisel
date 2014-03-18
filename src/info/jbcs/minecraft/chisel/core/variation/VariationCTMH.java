package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import com.sun.swing.internal.plaf.metal.resources.metal_it;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.core.CarvableVariation;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMH extends CarvableVariation {
    public TextureSubmap			seamsCtmVert;

    protected String extention;
    public VariationCTMH()
    {
        extention="-ctmh";

    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side) {
        if (side<2)
            return super.getIcon(side);
        return seamsCtmVert.icons[0];
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos, IBlockAccess world, Cuboid6 bounds) {
        super.setup(verts, side, pos, world, bounds);
        midpoint.set(bounds.center());

    }
    double ucoor=0;
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        ucoor=0;
        if (side < 2)
            return icon;

        double xneg=-1;
        double zneg=-1;
        double xpos=1;
        double zpos=1;

        if(midpoint.x!=0.5)
        {
            xneg=-.5;
            xpos=0.5;
        }
        if(midpoint.z!=0.5)
        {
            zneg=-.5;
            zpos=0.5;
        }


        double dx=x + midpoint.x;
        double dy=y+ midpoint.y;
        double dz=z + midpoint.z;


        int id=world.getBlockId(x,y,z);


        boolean left;
        boolean right;
        boolean lt;
        boolean gt;
        boolean reverse = side == 2 || side == 4;

        if (side < 4) {
            left = ConnectionCheckManager.checkConnection(world, dx +xneg, dy, dz, id, metadata);
            right = ConnectionCheckManager.checkConnection(world, dx +xpos, dy, dz, id, metadata);
            lt= midpoint.x>.5;
            gt=midpoint.x<0.5;
        } else {
            left = ConnectionCheckManager.checkConnection(world, dx, dy, dz+zpos, id, metadata);
            right = ConnectionCheckManager.checkConnection(world, dx, dy, dz+zneg, id, metadata);
            lt= midpoint.z<.5;
            gt=midpoint.z>0.5;
        }

        if (left && right)
            return seamsCtmVert.icons[1];
        else if (left)
        {
            if(gt)
                 ucoor=reverse?-0.5:0.5;
            return seamsCtmVert.icons[reverse ? 2 : 3];

        }
        else if (right)
        {
            if(lt)
                ucoor=reverse?0.5:-0.5;
            return seamsCtmVert.icons[reverse ? 3 : 2];
        }
        return seamsCtmVert.icons[0];
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(String modName, Block block, IconRegister register) {
        seamsCtmVert = new TextureSubmap(
                getIconResource(modName + ":" + texture
                        + extention,register), 2, 2);
        icon = getIconResource(modName + ":"
                + texture + "-top",register);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIndexedIcon(int index) {
        if(index>=4) return icon;

        return seamsCtmVert.icons[index];
    }

    @Override
    public void transform(UV uv) {
        uv.u+=ucoor;
        super.transform(uv);
    }
}
