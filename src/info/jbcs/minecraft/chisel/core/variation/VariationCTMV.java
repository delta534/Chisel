package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.render.Util;
import info.jbcs.minecraft.chisel.render.Util.RotationData;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMV extends VariationCTMH {
    public Icon [] Icons;
    RotationData rotations;
    int rotation;
    int side;
    IBlockAccess world_;
    int topIndex=1;
    int bottomIndex=0;
    public VariationCTMV()
    {
        rotations=new RotationData();
        Icons=new Icon[6];
        extention="-ctmv";
        boundIcon=icon;
    }

    @Override
    public Icon getIcon(int side) {
        if(side <2)
            return icon;
        return seamsCtmVert.icons[0];
    }

    @Override
    public boolean isTop(int side) {
        return side==topIndex;
    }
    @Override
    public boolean isBottom(int side) {
        return side==bottomIndex;
    }
    @Override
    public boolean isSide(int side) {
        return side!=topIndex&&side!=bottomIndex;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        if (side < 2)
            return icon;

        int blockId = world.getBlockId(x, y, z);
        boolean topConnected = ConnectionCheckManager.checkConnection(world,x, y + 1, z,blockId,metadata);
        boolean botConnected = ConnectionCheckManager.checkConnection(world,x, y - 1, z,blockId,metadata);

        if (topConnected && botConnected)
            return seamsCtmVert.icons[2];
        if (topConnected )
            return seamsCtmVert.icons[3];
        if (botConnected)
            return seamsCtmVert.icons[1];
        return seamsCtmVert.icons[0];
    }
    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos,
                      IBlockAccess world)
    {
        rotations.clear();
        world_=world;
        if(world!=null)
        {
            this.side=side;
            int x=(int)pos.x;
            int y=(int)pos.y;
            int z=(int)pos.z;
            int id=world.getBlockId(x,y,z);

            boolean flag = (rotations==null|| !(useCTM));
            if(!flag)
            {
                boolean yp = ConnectionCheckManager.checkConnection(world, x, y + 1, z, id, metadata);
                boolean yn = ConnectionCheckManager.checkConnection(world, x, y - 1, z, id, metadata);
                if((yp||yn))
                {
                    Icons[0] = getIndexedIcon(4);
                    Icons[1] = getIndexedIcon(4);
                    bottomIndex=0;
                    topIndex=1;
                    if (yp && yn)
                        Icons[2] = getIndexedIcon(2);
                    else if (yp)
                        Icons[2] = getIndexedIcon(3);
                    else
                        Icons[2] = getIndexedIcon(1);

                    Icons[3] = Icons[4] = Icons[5] = Icons[2];
                }
                else
                {
                    boolean xp = ConnectionCheckManager.checkConnection(world, x + 1, y, z, id, metadata);
                    boolean xn = ConnectionCheckManager.checkConnection(world, x - 1, y, z, id, metadata);

                    if (xp || xn) {
                        rotations.rotateZNeg=2;
                        rotations.rotateZPos=1;
                        rotations.rotateYNeg=1;
                        rotations.rotateYPos=1;
                        Icons[4] = getIndexedIcon(4);
                        Icons[5] = getIndexedIcon(4);
                        bottomIndex=4;
                        topIndex=5;
                        if (xp && xn)
                            Icons[0] = getIndexedIcon(2);
                        else if (xp)
                            Icons[0] = getIndexedIcon(3);
                        else
                            Icons[0] = getIndexedIcon(1);

                        Icons[1] = Icons[2] = Icons[3] = Icons[0];
                    }
                    else
                    {
                        boolean zp = ConnectionCheckManager.checkConnection(world, x, y, z + 1, id, metadata);
                        boolean zn = ConnectionCheckManager.checkConnection(world, x, y, z - 1, id, metadata);

                        if (zp  && (ConnectionCheckManager.checkConnection(world, x, y + 1, z + 1, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x, y - 1, z + 1, id, metadata)))
                            zp = false;
                        if (zp  && (ConnectionCheckManager.checkConnection(world, x + 1, y, z + 1, id, metadata)
                                ||ConnectionCheckManager.checkConnection(world, x - 1, y, z + 1, id, metadata)))
                            zp = false;
                        if (zn  && (ConnectionCheckManager.checkConnection(world, x, y + 1, z - 1, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x, y - 1, z - 1, id, metadata)))
                            zn = false;
                        if (zn  && (ConnectionCheckManager.checkConnection(world, x + 1, y, z - 1, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x - 1, y, z - 1, id, metadata)))
                            zn = false;

                        if (zp || zn) {
                            rotations.rotateXPos = 1;
                            rotations.rotateXNeg = 2;
                            Icons[2] = getIndexedIcon(4);
                            Icons[3] = getIndexedIcon(4);
                            bottomIndex=3;
                            topIndex=2;
                            if (zp && zn)
                                Icons[0] = getIndexedIcon(2);
                            else if (zp)
                                Icons[0] = getIndexedIcon(1);
                            else
                                Icons[0] = getIndexedIcon(3);

                            Icons[1] = Icons[4] = Icons[5] = Icons[0];
                        } else {
                            bottomIndex=0;
                            topIndex=1;
                            Icons[0] = Icons[1] = getIndexedIcon(4);
                            Icons[2] = Icons[3] = Icons[4] = Icons[5] = getIndexedIcon(0);
                        }

                    }

                }

                switch (side % 6) {
                    case 0:
                        rotation =rotations.rotateYNeg;// good
                        break;
                    case 1:
                        rotation =rotations.rotateYPos;// good
                        break;
                    case 2:
                        rotation =rotations.rotateZNeg;// good
                        break;
                    case 3:
                        rotation =rotations.rotateZPos;// good
                        break;
                    case 4:
                        rotation =rotations.rotateXNeg;// good
                        break;
                    case 5:
                        rotation =rotations.rotateXPos;// good
                        break;
                }
                setBoundIcon(Icons[side%6]);
            }
            else
                setBoundIcon(getIcon(side));
        }
    }
    @Override
    public void transform(UV texcoord) {
            //int i = (int) texcoord.u >> 1;
        if (useCTM&&world_!=null) {
            Util.rotateUV(texcoord, side%6, rotation);
        }
        if(boundIcon==null)
            boundIcon=icon;
        texcoord.u = boundIcon.getInterpolatedU(texcoord.u % 2 * 16);
        texcoord.v = boundIcon.getInterpolatedV(texcoord.v % 2 * 16);
    }
}
