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
    static final int[][] texMappings={
            {16,18,19,17}

    };
    static final int [][] offsetVecs= {
            {            }
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
    static UV []cornerOffsets= {new UV(-0.5,-0.5), new UV(0.5,-0.5),new UV(.5,.5),new UV(-0.5,0.5)};
    static Vector3 offset=new Vector3();
    public static int getTexture(IBlockAccess world, int x, int y, int z,
                                 int side,int index,Vector3 offset_)
    {
        int texture=0;
        if (world == null)
            return 0;

        offset.set(offset_).add(x,y,z);
        int blockId = world.getBlockId(x, y, z);
        int blockMetadata = world.getBlockMetadata(x, y, z);
        boolean b[] = new boolean[4];
        boolean b2[] =new boolean[4];
        switch (side)
        {
            case 0:
                b[left]=isConnected(world, offset.x -0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y, offset.z +0.5, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y, offset.z -0.5, side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                    b2[i]=isConnected(world, offset.x+cornerOffsets[i].u , y, offset.z+cornerOffsets[i].v, side, blockId,
                            blockMetadata);
                }
                break  ;
            case 1:
                b[left]=isConnected(world, offset.x-0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y, offset.z +0.5, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y, offset.z -0.5, side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                b2[i]=isConnected(world, offset.x+cornerOffsets[i].u , offset.y, offset.z+cornerOffsets[i].v, side, blockId,
                        blockMetadata);
            }
                break  ;
            case 2:
                b[left]=isConnected(world, offset.x +0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x -0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y-0.5, offset.z , side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +0.5, offset.z , side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                    b2[i]=isConnected(world, offset.x+cornerOffsets[i].u , offset.y+cornerOffsets[i].v , offset.z , side, blockId,
                            blockMetadata);
                }
                break;
            case 3:
                b[left]=isConnected(world, offset.x-0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x +0.5, offset.y, offset.z, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y-0.5, offset.z , side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +0.5, offset.z , side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                    b2[i]=isConnected(world, offset.x+cornerOffsets[i].u , offset.y+cornerOffsets[i].v , offset.z , side, blockId,
                            blockMetadata);
                }
                break;
            case 4:
                b[left]=isConnected(world, offset.x , offset.y, offset.z -0.5, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x , offset.y, offset.z +0.5, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y-0.5, offset.z, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y+0.5, offset.z, side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                    b2[i]=isConnected(world, offset.x , offset.y+cornerOffsets[i].u , offset.z+cornerOffsets[i].v , side, blockId,
                            blockMetadata);
                }
                break;
            case 5:

                b[left]=isConnected(world, offset.x , offset.y, offset.z +0.5, side, blockId,
                        blockMetadata);
                b[right]=isConnected(world, offset.x , offset.y, offset.z -0.5, side, blockId,
                        blockMetadata);
                b[below]=isConnected(world, offset.x , offset.y-0.5, offset.z, side, blockId,
                        blockMetadata);
                b[above]=isConnected(world, offset.x , offset.y +0.5, offset.z, side, blockId,
                        blockMetadata);
                for(int i=0;i<4;i++)
                {
                    b2[i]=isConnected(world, offset.x , offset.y+cornerOffsets[i].u , offset.z+cornerOffsets[i].v , side, blockId,
                            blockMetadata);
                }
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

            texture=cornerCheck(b2,index,texture);
            if(cornerCount>=1)
            {
                texture=mappings[upperright][index%2];
            }
        }

        else if(b[left]&&!b[right]&&b[below]&&b[above])  //13
        {

            texture=cornerCheck(b2,index,texture);
            if(cornerCount>=1)
            {
                texture=mappings[upperright][index%2+2];
            }
        }

        else if(b[left]&&b[right]&&!b[below]&&b[above])  //14
        {

            texture=cornerCheck(b2,index,texture);
            if(cornerCount>=1)
            {
                texture=mappings[lowerleft][index%2+2];
            }
        }

        else if(b[left]&&b[right]&&b[below]&&!b[above])  //15
        {

            texture=cornerCheck(b2,index,texture);
            if(cornerCount>=1)
            {
                texture=mappings[lowerleft][index%2];
            }
        }
        cornerCount=0;
        return texture;
    }
    static int cornerCount;
    public static int cornerCheck(boolean b2[],int index,int tex)
    {
        for(boolean b:b2)
        {
            if(!b)
            {
                tex=mappings[lowerright][index];
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
            b[0] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[right] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[2] = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[3] = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
        } else if (side == 2) {
            b[0] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[1] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[2] = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[3] = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 3) {
            b[0] = isConnected(world, x - 1, y, z, side, blockId, blockMetadata);
            b[right] = isConnected(world, x + 1, y, z, side, blockId, blockMetadata);
            b[2] = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[3] = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 4) {
            b[0] = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
            b[1] = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[2] = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[3] = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        } else if (side == 5) {
            b[0] = isConnected(world, x, y, z + 1, side, blockId, blockMetadata);
            b[1] = isConnected(world, x, y, z - 1, side, blockId, blockMetadata);
            b[2] = isConnected(world, x, y - 1, z, side, blockId, blockMetadata);
            b[3] = isConnected(world, x, y + 1, z, side, blockId, blockMetadata);
        }
        if (b[0] & !b[1] & !b[2] & !b[3])
            texture = 3;
        else if (!b[0] & b[1] & !b[2] & !b[3])
            texture = 1;
        else if (!b[0] & !b[1] & b[2] & !b[3])
            texture = 16;
        else if (!b[0] & !b[1] & !b[2] & b[3])
            texture = 48;
        else if (b[0] & b[1] & !b[2] & !b[3])
            texture = 2;
        else if (!b[0] & !b[1] & b[2] & b[3])
            texture = 32;
        else if (b[0] & !b[1] & b[2] & !b[3])
            texture = 19;
        else if (b[0] & !b[1] & !b[2] & b[3])
            texture = 51;
        else if (!b[0] & b[1] & b[2] & !b[3])
            texture = 17;
        else if (!b[0] & b[1] & !b[2] & b[3])
            texture = 49;
        else if (!b[0] & b[1] & b[2] & b[3])
            texture = 33;
        else if (b[0] & !b[1] & b[2] & b[3])
            texture = 35;
        else if (b[0] & b[1] & !b[2] & b[3])
            texture = 50;
        else if (b[0] & b[1] & b[2] & !b[3])
            texture = 18;
        else if (b[0] & b[1] & b[2] & b[3])
            texture = 34;

        boolean b2[] = new boolean[6];
        if (side <= 1) {
            b2[0] = !isConnected(world, x + 1, y, z + 1, side, blockId,
                    blockMetadata);
            b2[1] = !isConnected(world, x - 1, y, z + 1, side, blockId,
                    blockMetadata);
            b2[2] = !isConnected(world, x + 1, y, z - 1, side, blockId,
                    blockMetadata);
            b2[3] = !isConnected(world, x - 1, y, z - 1, side, blockId,
                    blockMetadata);
        } else if (side == 2) {
            b2[0] = !isConnected(world, x - 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[1] = !isConnected(world, x + 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[2] = !isConnected(world, x - 1, y + 1, z, side, blockId,
                    blockMetadata);
            b2[3] = !isConnected(world, x + 1, y + 1, z, side, blockId,
                    blockMetadata);
        } else if (side == 3) {
            b2[0] = !isConnected(world, x + 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[1] = !isConnected(world, x - 1, y - 1, z, side, blockId,
                    blockMetadata);
            b2[2] = !isConnected(world, x + 1, y + 1, z, side, blockId,
                    blockMetadata);
            b2[3] = !isConnected(world, x - 1, y + 1, z, side, blockId,
                    blockMetadata);
        } else if (side == 4) {
            b2[0] = !isConnected(world, x, y - 1, z + 1, side, blockId,
                    blockMetadata);
            b2[1] = !isConnected(world, x, y - 1, z - 1, side, blockId,
                    blockMetadata);
            b2[2] = !isConnected(world, x, y + 1, z + 1, side, blockId,
                    blockMetadata);
            b2[3] = !isConnected(world, x, y + 1, z - 1, side, blockId,
                    blockMetadata);
        } else if (side == 5) {
            b2[0] = !isConnected(world, x, y - 1, z - 1, side, blockId,
                    blockMetadata);
            b2[1] = !isConnected(world, x, y - 1, z + 1, side, blockId,
                    blockMetadata);
            b2[2] = !isConnected(world, x, y + 1, z - 1, side, blockId,
                    blockMetadata);
            b2[3] = !isConnected(world, x, y + 1, z + 1, side, blockId,
                    blockMetadata);
        }

        if (texture == 17 && b2[0])
            texture = 4;
        if (texture == 19 && b2[1])
            texture = 5;
        if (texture == 49 && b2[2])
            texture = 20;
        if (texture == 51 && b2[3])
            texture = 21;

        if (texture == 18 && b2[0] && b2[1])
            texture = 7;
        if (texture == 33 && b2[0] && b2[2])
            texture = 6;
        if (texture == 35 && b2[3] && b2[1])
            texture = 23;
        if (texture == 50 && b2[3] && b2[2])
            texture = 22;

        if (texture == 18 && !b2[0] && b2[1])
            texture = 39;
        if (texture == 33 && b2[0] && !b2[2])
            texture = 38;
        if (texture == 35 && !b2[3] && b2[1])
            texture = 53;
        if (texture == 50 && b2[3] && !b2[2])
            texture = 52;

        if (texture == 18 && b2[0] && !b2[1])
            texture = 37;
        if (texture == 33 && !b2[0] && b2[2])
            texture = 36;
        if (texture == 35 && b2[3] && !b2[1])
            texture = 55;
        if (texture == 50 && !b2[3] && b2[2])
            texture = 54;

        if (texture == 34 && b2[0] && b2[1] && b2[2] && b2[3])
            texture = 58;

        if (texture == 34 && !b2[0] && b2[1] && b2[2] && b2[3])
            texture = 9;
        if (texture == 34 && b2[0] && !b2[1] && b2[2] && b2[3])
            texture = 25;
        if (texture == 34 && b2[0] && b2[1] && !b2[2] && b2[3])
            texture = 8;
        if (texture == 34 && b2[0] && b2[1] && b2[2] && !b2[3])
            texture = 24;

        if (texture == 34 && b2[0] && b2[1] && !b2[2] && !b2[3])
            texture = 11;
        if (texture == 34 && !b2[0] && !b2[1] && b2[2] && b2[3])
            texture = 26;
        if (texture == 34 && !b2[0] && b2[1] && !b2[2] && b2[3])
            texture = 27;
        if (texture == 34 && b2[0] && !b2[1] && b2[2] && !b2[3])
            texture = 10;

        if (texture == 34 && b2[0] && !b2[1] && !b2[2] && b2[3])
            texture = 42;
        if (texture == 34 && !b2[0] && b2[1] && b2[2] && !b2[3])
            texture = 43;

        if (texture == 34 && b2[0] && !b2[1] && !b2[2] && !b2[3])
            texture = 40;
        if (texture == 34 && !b2[0] && b2[1] && !b2[2] && !b2[3])
            texture = 41;
        if (texture == 34 && !b2[0] && !b2[1] && b2[2] && !b2[3])
            texture = 56;
        if (texture == 34 && !b2[0] && !b2[1] && !b2[2] && b2[3])
            texture = 57;
        return texture;
    }

    private static boolean isConnected(IBlockAccess world, double dx, double dy, double dz,
                                       int side, int id, int meta) {
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
        boolean cres=(ConnectionCheckManager.checkConnection(world, dx, dy, dz, id, meta));
        if(cres||ConnectionCheckManager.earlystop)
            return  cres;
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if( CarvableHelper.isSame(world, x, y, z, id, meta))
            return true;
        if (te instanceof IChiselCheck)
            return ((IChiselCheck) te).ContainsEquivalentBlock(id, meta);
        if (Block.blocksList[id] instanceof BlockMarbleCarpet)
            return world.getBlockId(x, y, z) == id
                    && world.getBlockMetadata(x, y, z) == meta;
        else
            return world.getBlockId(x, y, z) == id
                    && world.getBlockMetadata(x, y, z) == meta
                    && (world.getBlockId(x2, y2, z2) != id || world
                    .getBlockMetadata(x2, y2, z2) != meta);

    }

}
