package info.jbcs.minecraft.chisel.util;

import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;


public class ConnectionCheckManager {

    private static final ArrayList<IConnectionCheck> checks = new ArrayList<IConnectionCheck>();

    static {
        addCheck(new IConnectionCheck() {
            boolean stop;

            @Override
            public boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta) {
                int x = MathHelper.floor_double(dx);
                int y = MathHelper.floor_double(dy);
                int z = MathHelper.floor_double(dz);
                TileEntity t = world.getTileEntity(x, y, z);
                if (t instanceof IChiselCheck) {
                    stop = ((IChiselCheck) t).ContainsEquivalentBlock(id, meta);
                    return stop;

                }

                return false;
            }

            @Override
            public boolean continueCheck() {
                return !stop;
            }

            boolean stopOpac;

            @Override
            public boolean opacityCheck(IBlockAccess world, double dx, double dy, double dz) {
                return false;
            }

            @Override
            public boolean contineOpaqueCheck() {
                return true;
            }
        });
    }

    public static void addCheck(IConnectionCheck check) {
        checks.add(check);
    }

    public static boolean earlystop;

    public static boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta) {

        earlystop = false;
        for (IConnectionCheck check : checks) {
            boolean res = check.checkConnection(world, dx, dy, dz, id, meta);
            if (!check.continueCheck()) {
                earlystop = true;
                return res;
            }
        }
        int x = MathHelper.floor_double(dx);
        int y = MathHelper.floor_double(dy);
        int z = MathHelper.floor_double(dz);
        int id_ = world.getBlockId(x, y, z);
        int meta_ = world.getBlockMetadata(x, y, z);

        return id == id_ && meta == meta_;
    }

    public static boolean checkOpacity(IBlockAccess world, double dx, double dy, double dz) {
        for (IConnectionCheck check : checks) {
            boolean res = check.opacityCheck(world, dx, dy, dz);
            if (!check.contineOpaqueCheck()) {
                return res;
            }
        }
        int x = MathHelper.floor_double(dx);
        int y = MathHelper.floor_double(dy);
        int z = MathHelper.floor_double(dz);
        int id_ = world.getBlockId(x, y, z);
        if (Block.blocksList[id_] != null)
            return Block.blocksList[id_].isOpaqueCube();
        else
            return false;
    }


}
