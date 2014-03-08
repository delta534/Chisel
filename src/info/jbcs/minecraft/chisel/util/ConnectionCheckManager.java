package info.jbcs.minecraft.chisel.util;

import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;


public class ConnectionCheckManager {

    private static final ArrayList<IConnectionCheck> checks=new ArrayList<IConnectionCheck>();
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
