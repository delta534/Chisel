package info.jbcs.minecraft.chisel.modCompat;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import info.jbcs.minecraft.chisel.Chisel;
import net.minecraft.world.World;

public class ChiselMultiPart implements IPartFactory, IPartConverter {

    @Override
    public boolean canConvert(int blockID) {
        return blockID == Chisel.blockCarpetFloor.blockID;
    }

    @Override
    public TMultiPart convert(World world, BlockCoord pos) {
        int id = world.getBlockId(pos.x, pos.y, pos.z);
        int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
        if (id == Chisel.blockCarpetFloor.blockID)
            return new MultiPartCarpetTest(meta);

        return null;
    }

    @Override
    public TMultiPart createPart(String name, boolean client) {
        if (name.equals("Chisel_Carpet")) return new MultiPartCarpetTest();
        return null;
    }

}