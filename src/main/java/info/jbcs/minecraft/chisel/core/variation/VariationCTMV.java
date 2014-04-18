package info.jbcs.minecraft.chisel.core.variation;

import codechicken.lib.render.uv.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.render.Util;
import info.jbcs.minecraft.chisel.render.Util.RotationData;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMV extends VariationCTMH {
    public IIcon[] Icons;
    RotationData rotations;
    int rotation;
    int side;
    IBlockAccess world_;
    int topIndex = 1;
    int bottomIndex = 0;

    public VariationCTMV() {
        rotations = new RotationData();
        Icons = new IIcon[6];
        extention = "-ctmv";
        boundIcon = icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side) {
        if (side % 6 < 2)
            return icon;
        return seamsCtmVert.icons[0];
    }

    @Override
    public boolean isTop(int side) {
        return side == topIndex;
    }

    @Override
    public boolean isBottom(int side) {
        return side == bottomIndex;
    }

    @Override
    public boolean isSide(int side) {
        return side != topIndex && side != bottomIndex;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side < 2)
            return icon;

        int blockId = world.getBlockId(x, y, z);
        boolean topConnected = ConnectionCheckManager.checkConnection(world, x, y + 1, z, blockId, metadata);
        boolean botConnected = ConnectionCheckManager.checkConnection(world, x, y - 1, z, blockId, metadata);

        if (topConnected && botConnected)
            return seamsCtmVert.icons[2];
        if (topConnected)
            return seamsCtmVert.icons[3];
        if (botConnected)
            return seamsCtmVert.icons[1];
        return seamsCtmVert.icons[0];
    }

    double ucorr = 0;
    double vcorr = 0;

    @SideOnly(Side.CLIENT)
    @Override
    public void setup(Vertex5[] verts, int side, Vector3 pos,
                      IBlockAccess world, Cuboid6 bounds) {
        midpoint.set(bounds.center());
        rotations.clear();
        world_ = world;

        if (world != null) {
            ucorr = 0;
            vcorr = 0;
            double yneg = -1;
            double xneg = -1;
            double zneg = -1;
            double xpos = 1;
            double zpos = 1;
            double ypos = 1;

            if (midpoint.x != 0.5) {
                xneg = -.5;
                xpos = 0.5;
            }
            if (midpoint.y != 0.5) {
                yneg = -.5;
                ypos = 0.5;
            }
            if (midpoint.z != 0.5) {
                zneg = -.5;
                zpos = 0.5;
            }

            this.side = side;
            double x = pos.x + midpoint.x;
            double y = pos.y + midpoint.y;
            double z = pos.z + midpoint.z;
            int x1 = (int) (pos.x);
            int y1 = (int) (pos.y);
            int z1 = (int) (pos.z);

            int id = world.getBlockId(x1, y1, z1);

            boolean flag = (rotations == null || !(useCTM));
            if (!flag) {
                boolean yp = ConnectionCheckManager.checkConnection(world, x, y + ypos, z, id, metadata);
                boolean yn = ConnectionCheckManager.checkConnection(world, x, y + yneg, z, id, metadata);
                boolean xp = ConnectionCheckManager.checkConnection(world, x + xpos, y, z, id, metadata);
                boolean xn = ConnectionCheckManager.checkConnection(world, x + xneg, y, z, id, metadata);
                boolean zp = ConnectionCheckManager.checkConnection(world, x, y, z + zpos, id, metadata);
                boolean zn = ConnectionCheckManager.checkConnection(world, x, y, z + zneg, id, metadata);
                boolean yaxis = yp || yn;
                boolean xaxis = xp || xn;

                if (yaxis) {
                    Icons[0] = getIndexedIcon(4);
                    Icons[1] = getIndexedIcon(4);
                    bottomIndex = 0;
                    topIndex = 1;
                    if (yp && yn) {
                        Icons[2] = getIndexedIcon(2);
                    } else if (yp) {
                        Icons[2] = getIndexedIcon(3);
                        if (midpoint.y > .5) {
                            ucorr = 0;
                            vcorr = 0.5;
                        }
                    } else {
                        Icons[2] = getIndexedIcon(1);
                        if (midpoint.y < .5) {
                            ucorr = 0;
                            vcorr = -0.5;
                        }
                    }

                    Icons[3] = Icons[4] = Icons[5] = Icons[2];
                } else if (xaxis) {
                    if (Chisel.rotateVCTM) {
                        rotations.rotateZNeg = 2;
                        rotations.rotateZPos = 1;
                        rotations.rotateYNeg = 1;
                        rotations.rotateYPos = 1;
                        Icons[4] = getIndexedIcon(4);
                        Icons[5] = getIndexedIcon(4);
                        bottomIndex = 4;
                        topIndex = 5;
                        if (xp && xn) {
                            Icons[0] = getIndexedIcon(2);
                            ucorr = 0;
                        } else if (xp) {
                            Icons[0] = getIndexedIcon(3);
                            if (midpoint.x > .5) {
                                ucorr = 0;
                                ucorr = -0.5;
                            }
                        } else {
                            Icons[0] = getIndexedIcon(1);
                            if (midpoint.x < .5) {
                                ucorr = 0;
                                ucorr = 0.5;
                            }
                        }

                        Icons[1] = Icons[2] = Icons[3] = Icons[0];
                    }
                } else {


                    if (Chisel.rotateVCTM) {
                        if (zp && (ConnectionCheckManager.checkConnection(world, x, y + ypos, z + zpos, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x, y + yneg, z + zpos, id, metadata)))
                            zp = false;
                        if (zp && (ConnectionCheckManager.checkConnection(world, x + xpos, y, z + zpos, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x + xneg, y, z + zpos, id, metadata)))
                            zp = false;
                        if (zn && (ConnectionCheckManager.checkConnection(world, x, y + xpos, z + zneg, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x, y + yneg, z + zneg, id, metadata)))
                            zn = false;
                        if (zn && (ConnectionCheckManager.checkConnection(world, x + xpos, y, z + zneg, id, metadata)
                                || ConnectionCheckManager.checkConnection(world, x + xneg, y, z + zneg, id, metadata)))
                            zn = false;

                        if (zp || zn) {
                            rotations.rotateXPos = 1;
                            rotations.rotateXNeg = 2;
                            Icons[2] = getIndexedIcon(4);
                            Icons[3] = getIndexedIcon(4);
                            bottomIndex = 3;
                            topIndex = 2;
                            if (zp && zn) {
                                Icons[0] = getIndexedIcon(2);


                            } else if (zp) {
                                Icons[0] = getIndexedIcon(1);
                                if (midpoint.z > .5) {
                                    vcorr = -0.5;
                                    //vcorr=0;
                                }
                            } else {
                                Icons[0] = getIndexedIcon(3);
                                if (midpoint.z < .5) {
                                    vcorr = 0.5;
                                    //vcorr=0;
                                }
                            }

                            Icons[1] = Icons[4] = Icons[5] = Icons[0];
                        } else {

                            bottomIndex = 0;
                            topIndex = 1;
                            Icons[0] = Icons[1] = getIndexedIcon(4);
                            Icons[2] = Icons[3] = Icons[4] = Icons[5] = getIndexedIcon(0);
                        }
                    }
                }

            }

            switch (side % 6) {
                case 0:
                    rotation = rotations.rotateYNeg;// good
                    break;
                case 1:
                    rotation = rotations.rotateYPos;// good
                    break;
                case 2:
                    rotation = rotations.rotateZNeg;// good
                    break;
                case 3:
                    rotation = rotations.rotateZPos;// good
                    break;
                case 4:
                    rotation = rotations.rotateXNeg;// good
                    break;
                case 5:
                    rotation = rotations.rotateXPos;// good
                    break;
            }
            for (int i = 0; i < 4; i++) {
                UV texcoord = verts[i].uv;
                if (isSide(side)) {
                    texcoord.u += ucorr;
                    texcoord.v += vcorr;
                }
            }
        } else {
            bottomIndex = 0;
            topIndex = 1;
            Icons[0] = Icons[1] = getIndexedIcon(4);
            Icons[2] = Icons[3] = Icons[4] = Icons[5] = getIndexedIcon(0);
        }
        boundIcon = Icons[side % 6];

    }

    @Override
    public void transform(UV texcoord) {
        int i = (int) texcoord.u >> 1;
        if (useCTM && world_ != null) {

            Util.rotateUV(texcoord, i % 6, rotation);
        }
        texcoord.u = boundIcon.getInterpolatedU(texcoord.u % 2 * 16);
        texcoord.v = boundIcon.getInterpolatedV(texcoord.v % 2 * 16);
    }
}
