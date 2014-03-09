package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class VariationCTMV extends VariationCTMH {
    public VariationCTMV()
    {
        extention="-ctmv";
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
        if (topConnected && !botConnected)
            return seamsCtmVert.icons[3];
        if (!topConnected && botConnected)
            return seamsCtmVert.icons[1];
        return seamsCtmVert.icons[0];
    }


}
