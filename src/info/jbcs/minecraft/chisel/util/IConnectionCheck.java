package info.jbcs.minecraft.chisel.util;

import net.minecraft.world.IBlockAccess;

/**
 * Created by Maxwell on 3/4/14.
 */
public interface IConnectionCheck {
    boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta);

    boolean opacityCheck(IBlockAccess world, double dx, double dy, double dz);

    boolean continueCheck();

    boolean contineOpaqueCheck();
}
