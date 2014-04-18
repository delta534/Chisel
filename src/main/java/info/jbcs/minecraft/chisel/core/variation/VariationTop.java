package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.core.RenderVariation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class VariationTop extends RenderVariation {
    public IIcon iconTop;

    @Override
    public IIcon getIcon(int side) {
        if (side == 0 || side == 1)
            return iconTop;
        else
            return icon;
    }

    @Override
    public void registerIcon(String modName, Block block_, IIconRegister register) {
        if (block != null) {
            icon = block.getIcon(2, blockMeta);
            iconTop = block.getIcon(0, blockMeta);
        } else {
            icon = getIconResource(modName + ":"
                    + texture + "-side", register);
            iconTop = getIconResource(modName + ":"
                    + texture + "-top", register);
        }
    }
}
