package info.jbcs.minecraft.chisel.render;

import codechicken.lib.render.UV;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.common.FMLLog;
import info.jbcs.minecraft.chisel.blocks.BlockChiselGrass;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleCarpet;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.chisel.util.IChiselCheck;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.bouncycastle.asn1.cmp.CertOrEncCert;

public class CTM {
    public static final int left=0;
    public  static final int right=1;
    public  static final int below=2;
    public  static final int above=3;
    public  static final int upperleft=0;
    public  static final int upperright=1;
    public  static  final int lowerleft=2;
    public  static final int lowerright=3;
    public static final int defaultIcon=4;
    public static final int[][] mappings={
            {0,1,4,5}, //upper left
            {2,3,6,7},  //upper right
            {8,9,12,13},  //lower left
            {10,11,14,15} ,   //lower right

            {16,17,18,19}    //flipped lower right

    };

    static int [][]submaps = { { 16, 17, 18, 19 }, { 16, 9, 18, 13 },
            { 8, 9, 12, 13 }, { 8, 17, 12, 19 }, { 16, 9, 6, 15 },
            { 8, 17, 14, 7 }, { 2, 11, 6, 15 }, { 8, 9, 14, 15 },
            { 10, 1, 14, 15 }, { 10, 11, 14, 5 }, { 0, 11, 4, 15 },
            { 0, 1, 14, 15 }, {}, {}, {}, {}, { 16, 17, 6, 7 },
            { 16, 9, 6, 5 }, { 8, 9, 4, 5 }, { 8, 17, 4, 7 },
            { 2, 11, 18, 13 }, { 10, 3, 12, 19 }, { 10, 11, 12, 13 },
            { 10, 3, 14, 7 }, { 0, 11, 14, 15 }, { 10, 11, 4, 15 },
            { 10, 11, 4, 5 }, { 10, 1, 14, 5 }, {}, {}, {}, {}, { 2, 3, 6, 7 },
            { 2, 1, 6, 5 }, { 0, 1, 4, 5 }, { 0, 3, 4, 7 }, { 2, 11, 6, 5 },
            { 8, 9, 4, 15 }, { 2, 1, 6, 15 }, { 8, 9, 14, 5 }, { 0, 1, 4, 15 },
            { 0, 1, 14, 5 }, { 10, 1, 4, 15 }, { 0, 11, 14, 5 }, {}, {}, {},
            {}, { 2, 3, 18, 19 }, { 2, 1, 18, 13 }, { 0, 1, 12, 13 },
            { 0, 3, 12, 19 }, { 10, 1, 12, 13 }, { 0, 3, 14, 7 },
            { 0, 11, 12, 13 }, { 10, 3, 4, 7 }, { 0, 11, 4, 5 },
            { 10, 1, 4, 5 }, { 10, 11, 14, 15 }, { 0, 1, 4, 5 }, {}, {}, {},
            {}, };



    public static int[] getSubmapIndices(IBlockAccess world, int x, int y,
                                         int z, int side) {

        int index = getTexture(world, x, y, z, side);

        return submaps[index];
    }

    static Vector3 []octRegionOffsets= {
            new Vector3(.25,.25,.25),
            new Vector3(.75,.25,.25),
            new Vector3(.25,.75,.25),
            new Vector3(.75,.75,.25),
            new Vector3(.25,.25,.75),
            new Vector3(.75,.25,.75),
            new Vector3(.25,.75,.75),
            new Vector3(.75,.75,.75)
    };
    static final double xneg=-0.5;
    static final double yneg=-0.5;
    static final double zneg=-0.5;
    static final double xpos=0.5;
    static final double ypos=0.5;
    static final double zpos=0.5;
    static UV []cornerOffsets= {new UV(-0.5,-0.5), new UV(0.5,-0.5),new UV(.5,.5),new UV(-0.5,0.5)};
    static Vector3 offset=new Vector3();
    public static int getTexture(IBlockAccess world, int x, int y, int z,
                                 int side,int index,Vector3 offset_)
    {
        int texture=0;
        if (world == null)
            return 0;

        offset.set(offset_);


            offset.add((x), (y), (z));

        int blockId = world.getBlockId(x, y, z);
        int blockMetadata = world.getBlockMetadata(x, y, z);
        boolean b[] = new boolean[4];
        boolean b2[] =new boolean[4];

        switch (side)
        {
            case 0:

                b[left]=isConnected(world, offset.x +xneg, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +xpos, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x, offset.y, offset.z +zpos, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y, offset.z +zneg, side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x+xneg , offset.y , offset.z+zneg , side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x+xpos , offset.y , offset.z+zneg , side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x+xneg , offset.y , offset.z+zpos , side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x+xpos , offset.y , offset.z+zpos , side, blockId,
                        blockMetadata);
                break  ;
            case 1:

                b[left]=isConnected(world, offset.x +xneg, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +xpos, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y, offset.z +zpos, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y, offset.z +zneg, side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x+xneg , offset.y , offset.z+zneg , side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x+xpos , offset.y , offset.z+zneg , side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x+xneg , offset.y , offset.z+zpos , side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x+xpos , offset.y , offset.z+zpos , side, blockId,
                        blockMetadata);
                break  ;
            case 2:

                b[left]=isConnected(world, offset.x +xpos, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +xneg, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y +yneg, offset.z , side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +ypos, offset.z , side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x+xpos , offset.y+ypos, offset.z , side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x+xneg , offset.y+ypos , offset.z , side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x+xpos , offset.y+yneg , offset.z , side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x+xneg, offset.y+yneg , offset.z , side, blockId,
                        blockMetadata);
                break;
            case 3:

                b[left]=isConnected(world, offset.x +xneg, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +xpos, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y +yneg, offset.z , side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +ypos, offset.z , side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x+xneg , offset.y+ypos, offset.z , side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x+xpos , offset.y+ypos , offset.z , side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x+xneg , offset.y+yneg , offset.z , side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x+xpos, offset.y+yneg , offset.z , side, blockId,
                        blockMetadata);
                break;
            case 4:

                b[left]=isConnected(world, offset.x , offset.y, offset.z +zneg, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x , offset.y, offset.z +zpos, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y +yneg, offset.z, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y+ypos, offset.z, side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x , offset.y+ypos, offset.z +zneg, side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x, offset.y+ypos , offset.z +zpos, side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x , offset.y+yneg , offset.z +zneg, side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x, offset.y+yneg , offset.z +zpos, side, blockId,
                        blockMetadata);
                break;
            case 5:

                b[left]=isConnected(world, offset.x , offset.y, offset.z +zpos, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x , offset.y, offset.z +zneg, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y +yneg, offset.z, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +ypos, offset.z, side, blockId,
                        blockMetadata);
                b2[upperleft]=isConnected(world, offset.x , offset.y+ypos, offset.z+zpos , side, blockId,
                        blockMetadata);
                b2[upperright]=isConnected(world, offset.x , offset.y+ypos , offset.z+zneg , side, blockId,
                        blockMetadata);
                b2[lowerleft]=isConnected(world, offset.x , offset.y+yneg , offset.z+zpos , side, blockId,
                        blockMetadata);
                b2[lowerright]=isConnected(world, offset.x, offset.y+yneg , offset.z+zneg, side, blockId,
                        blockMetadata);
                break;
        }
        if(!b[left]&&!b[right]&&!b[below]&&!b[above])   //0
            texture=mappings[defaultIcon][index];
        else if(b[left]&&!b[right]&&!b[below]&&!b[above])//1
        {

            if(index==lowerleft||index==lowerright)
                texture=mappings[defaultIcon][lowerright];
            else
                texture=mappings[defaultIcon][upperright];
        }
        else if(!b[left]&&b[right]&&!b[below]&&!b[above])//2
        {
            if(index==lowerleft||index==lowerright)
                texture=mappings[defaultIcon][lowerleft];
            else
                texture=mappings[defaultIcon][upperleft];
        }
        else if(!b[left]&&!b[right]&&b[below]&&!b[above])//3
        {
            if(index==upperleft||index==lowerleft)
                texture=mappings[defaultIcon][upperleft];
            else
                texture=mappings[defaultIcon][upperright];
        }
        else if(!b[left]&&!b[right]&&!b[below]&&b[above])//4
        {
            if(index==upperleft||index==lowerleft)
                texture=mappings[defaultIcon][lowerleft];
            else
                texture=mappings[defaultIcon][lowerright];
        }
        else if(!b[left]&&!b[right]&&b[below]&&b[above]) //5
        {

            texture=mappings[upperright][index];
        }
        else if(b[left]&&b[right]&&!b[below]&&!b[above])  //6
        {

            texture=mappings[lowerleft][index];
        }

        else if(!b[left]&&b[right]&&!b[below]&&b[above]) //7
        {

            texture=mappings[defaultIcon][lowerleft];
        }
        else if(b[left]&&!b[right]&&!b[below]&&b[above]) //8
        {

            texture=mappings[defaultIcon][lowerright];
        }
        else if(!b[left]&&b[right]&&b[below]&&!b[above]) //9
        {

            texture=mappings[defaultIcon][upperleft];
        }

        else if(b[left]&&!b[right]&&b[below]&&!b[above]) //10
        {

            texture=mappings[defaultIcon][upperright];
        }
        else if(b[left]&&b[right]&&b[below]&&b[above])  //11
        {
            texture=cornerCheck(b2,index,texture);
            if(cornerCount==0)
            {
                texture=mappings[upperleft][index];
            }
        }
        else if(!b[left]&&b[right]&&b[below]&&b[above])  //12
        {


                int i=(index&2);

                texture=mappings[upperright][i];

        }

        else if(b[left]&&!b[right]&&b[below]&&b[above])  //13
        {


                int i=(index&2)+1;

                texture=mappings[upperright][i];

        }

        else if(b[left]&&b[right]&&!b[below]&&b[above])  //14
        {


                texture=mappings[lowerleft][index%2+2];

        }

        else if(b[left]&&b[right]&&b[below]&&!b[above])  //15
        {

                texture=mappings[lowerleft][index%2];
        }
        return texture;
    }
    static int cornerCount;
    public static int cornerCheck(boolean b2[],int index,int tex)
    {
        cornerCount=0;
        for(int i=0;i<4;i++)
        {
            //index=(index+i)%4;
            boolean b=b2[i];
            if(!b)
            {
                tex=mappings[lowerright][i];
                cornerCount++;
            }
        }



        return tex;
    }
    public static int getTexture(IBlockAccess world, int x, int y, int z,
                                 int side) {
        if (world == null)
            return 0;

        int texture = 0;
        int blockId = world.getBlockId(x, y, z);
        int blockMetadata = world.getBlockMetadata(x, y, z);
        boolean b[] = new boolean[6];
        if (side <= 1) {
            b[left] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[right] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[below]  = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[above]  = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
        } else if (side == 2) {
            b[left] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[right] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[below]  = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[above]  = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 3) {
            b[left] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[right] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[below]  = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[above]  = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 4) {
            b[left] = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
            b[right] = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[below]  = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[above]  = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 5) {
            b[left] = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[right] = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
            b[below]  = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[above]  = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        }
        if (b[left] & !b[right] & !b[below]  & !b[above] )
            texture = 3;//(1,4)
        else if (!b[left] & b[right] & !b[below]  & !b[above] )
            texture = 1;//(1,2)
        else if (!b[left] & !b[right] & b[below]  & !b[above] )
            texture = 4;//(1,5)
        else if (!b[left] & !b[right] & !b[below]  & b[above] )
            texture = 12;//(1,13)
        else if (b[left] & b[right] & !b[below]  & !b[above] )
            texture = 2; //(1,3)
        else if (!b[left] & !b[right] & b[below]  & b[above] )
            texture = 8; //(1,9)
        else if (b[left] & !b[right] & b[below]  & !b[above] )
            texture = 7; //(1,8)
        else if (b[left] & !b[right] & !b[below]  & b[above] )
            texture = 15; //(1,16)
        else if (!b[left] & b[right] & b[below]  & !b[above] )
            texture = 5; //(1,6)
        else if (!b[left] & b[right] & !b[below]  & b[above] )
            texture = 13; //(1,14)
        else if (!b[left] & b[right] & b[below]  & b[above] )
            texture = 9; //(1,10)
        else if (b[left] & !b[right] & b[below]  & b[above] )
            texture = 11; //(1,12)
        else if (b[left] & b[right] & !b[below]  & b[above] )
            texture = 14; //(1,15)
        else if (b[left] & b[right] & b[below]  & !b[above] )
            texture = 6; //(1,7)
        else if (b[left] & b[right] & b[below] & b[above] )
            texture = 10;  //(1,11)

        boolean b2[] = new boolean[6];
        if (side <= 1) {
            b2[upperleft] = !isConnected(world, x + 1, y, z + 1, side, blockId,
                    blockMetadata);
            b2[upperright] = !isConnected(world, x - 1, y, z + 1, side, blockId,
                    blockMetadata);
            b2[lowerleft] = !isConnected(world, x + 1, y, z - 1, side, blockId,
                    blockMetadata);
            b2[lowerright] = !isConnected(world, x - 1, y, z - 1, side, blockId,
                    blockMetadata);
        } else if (side == 2) {
            b2[upperleft] = !isConnected(world, x - 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[upperright] = !isConnected(world, x + 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[lowerleft] = !isConnected(world, x - 1, y + 1, z, side, blockId,
                    blockMetadata);
            b2[lowerright] = !isConnected(world, x + 1, y + 1, z, side, blockId,
                    blockMetadata);
        } else if (side == 3) {
            b2[upperleft] = !isConnected(world, x + 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[upperright] = !isConnected(world, x - 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[lowerleft] = !isConnected(world, x + 1, y + 1, z, side, blockId,
                    blockMetadata);
            b2[lowerright] = !isConnected(world, x - 1, y + 1, z, side, blockId,
                    blockMetadata);
        } else if (side == 4) {
            b2[upperleft] = !isConnected(world, x, y - 1, z + 1, side, blockId,
                    blockMetadata);
            b2[upperright] = !isConnected(world, x, y - 1, z - 1, side, blockId,
                    blockMetadata);
            b2[lowerleft] = !isConnected(world, x, y + 1, z + 1, side, blockId,
                    blockMetadata);
            b2[lowerright] = !isConnected(world, x, y + 1, z - 1, side, blockId,
                    blockMetadata);
        } else if (side == 5) {
            b2[upperleft] = !isConnected(world, x, y - 1, z - 1, side, blockId,
                    blockMetadata);
            b2[upperright] = !isConnected(world, x, y - 1, z + 1, side, blockId,
                    blockMetadata);
            b2[lowerleft] = !isConnected(world, x, y + 1, z - 1, side, blockId,
                    blockMetadata);
            b2[lowerright] = !isConnected(world, x, y + 1, z + 1, side, blockId,
                    blockMetadata);
        }

        if (texture == 5 && b2[upperleft])
            texture = 16; //(2,1)
        if (texture == 7 && b2[upperright])
            texture = 17;  //(2,2)
        if (texture == 13 && b2[lowerleft])
            texture = 20;  //(2,5)
        if (texture == 15 && b2[lowerright])
            texture = 21;  //(2,6)

        if (texture == 6 && b2[upperleft] && b2[upperright])
            texture = 19;       //(2,4)
        if (texture == 9 && b2[upperleft] && b2[lowerleft])
            texture = 18;   //(2,3)
        if (texture == 11 && b2[lowerright] && b2[upperright])
            texture = 23;  //(2,8)
        if (texture == 14 && b2[lowerright] && b2[lowerleft])
            texture = 22;  //(2,7)

        if (texture == 6 && !b2[upperleft] && b2[upperright])
            texture = 27;  //(2,12)
        if (texture == 9 && b2[upperleft] && !b2[lowerleft])
            texture = 26;  //(2,11)
        if (texture == 11 && !b2[lowerright] && b2[upperright])
            texture = 29;  //(2,14)
        if (texture == 14 && b2[lowerright] && !b2[lowerleft])
            texture = 28;  //(2,13)

        if (texture == 6 && b2[upperleft] && !b2[upperright])
            texture = 25;  //(2,10)
        if (texture == 9 && !b2[upperleft] && b2[lowerleft])
            texture = 24; //(2,9)
        if (texture == 11 && b2[lowerright] && !b2[upperright])
            texture = 31; //(2,16)
        if (texture == 14 && !b2[lowerright] && b2[lowerleft])
            texture = 30; //(2,15)

        if (texture == 10 && b2[upperleft] && b2[upperright] && b2[lowerleft] && b2[lowerright])
            texture = 46; //(3,15)

        if (texture == 10 && !b2[upperleft] && b2[upperright] && b2[lowerleft] && b2[lowerright])
            texture = 33;  //(3,2)
        if (texture == 10 && b2[upperleft] && !b2[upperright] && b2[lowerleft] && b2[lowerright])
            texture = 37; //(3,6)
        if (texture == 10 && b2[upperleft] && b2[upperright] && !b2[lowerleft] && b2[lowerright])
            texture = 32;     //(3,1)
        if (texture == 10 && b2[upperleft] && b2[upperright] && b2[lowerleft] && !b2[lowerright])
            texture = 36;    //(3,5)

        if (texture == 10 && b2[upperleft] && b2[upperright] && !b2[lowerleft] && !b2[lowerright])
            texture = 35;    //(3,4)
        if (texture == 10 && !b2[upperleft] && !b2[upperright] && b2[lowerleft] && b2[lowerright])
            texture = 38;    //(3,7)
        if (texture == 10 && !b2[upperleft] && b2[upperright] && !b2[lowerleft] && b2[lowerright])
            texture = 39;    //(3,8)
        if (texture == 10 && b2[upperleft] && !b2[upperright] && b2[lowerleft] && !b2[lowerright])
            texture = 34;    //(3,3)

        if (texture == 10 && b2[upperleft] && !b2[upperright] && !b2[lowerleft] && b2[lowerright])
            texture = 42; //(3,11)
        if (texture == 10 && !b2[upperleft] && b2[upperright] && b2[lowerleft] && !b2[lowerright])
            texture = 43; //(3,12)

        if (texture == 10 && b2[upperleft] && !b2[upperright] && !b2[lowerleft] && !b2[lowerright])
            texture = 40; //(3,9)
        if (texture == 10 && !b2[upperleft] && b2[upperright] && !b2[lowerleft] && !b2[lowerright])
            texture = 41; //(3,10)
        if (texture == 10 && !b2[upperleft] && !b2[upperright] && b2[lowerleft] && !b2[lowerright])
            texture = 44; //(3,13)
        if (texture == 10 && !b2[upperleft] && !b2[upperright] && !b2[lowerleft] && b2[lowerright])
            texture = 45; //(3,14)
        return texture;
    }

    private static boolean isConnected(IBlockAccess world, double dx, double dy, double dz,
                                       int side, int id, int meta) {
 // I need to figure out reason for the second check.
        int x=(int)dx,y=(int)dy,z=(int)dz;
        int x2 = x, y2 = y, z2 = z;
        switch (side) {
            case 0:
                y2--;
                break;
            case 1:
                y2++;
                break;
            case 2:
                z2--;
                break;
            case 3:
                z2++;
                break;
            case 4:
                x2--;
                break;
            case 5:
                x2++;
                break;
        }
        return (ConnectionCheckManager.checkConnection(world, dx, dy, dz, id, meta));
    }

}
