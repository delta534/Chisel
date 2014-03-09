package info.jbcs.minecraft.chisel.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;


public class ConnectionCheckManager {

    private static final ArrayList<IConnectionCheck> checks=new ArrayList<IConnectionCheck>();
    static
    {
        addCheck(new IConnectionCheck() {
            boolean stop;
            @Override
            public boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta) {
                int x=(int)dx;
                int y=(int)dy;
                int z=(int)dz;
                int id_=world.getBlockId(x,y,z);
                int meta_=world.getBlockMetadata(x,y,z);
                stop=id==id_&&meta==meta_;
                return stop;
            }

            @Override
            public boolean continueCheck() {
                return !stop;
            }
        });
        addCheck(new IConnectionCheck() {
            boolean stop;
            @Override
            public boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta) {
                int x=(int)dx;
                int y=(int)dy;
                int z=(int)dz;
                TileEntity t=world.getBlockTileEntity(x, y, z);
                if(t instanceof IChiselCheck)
                {
                    stop= ((IChiselCheck)t).ContainsEquivalentBlock(id,meta);
                    return stop;

                }
                return  false;
            }

            @Override
            public boolean continueCheck() {
                return !stop;
            }
        });
    }
    public static void addCheck(IConnectionCheck check)
    {
        checks.add(check);
    }
    public  static boolean earlystop;
    public static boolean checkConnection(IBlockAccess world,double dx,double dy,double dz,int id,int meta)
    {
        earlystop=false;
        for(IConnectionCheck check:checks)
        {
            boolean res=check.checkConnection(world, dx, dy, dz, id, meta);
            if(!check.continueCheck())
            {
                earlystop=true;
                return res;
            }
        }

        return false;
    }


}
