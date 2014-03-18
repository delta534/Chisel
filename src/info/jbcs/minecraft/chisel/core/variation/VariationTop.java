package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.core.CarvableVariation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class VariationTop extends CarvableVariation {
    public Icon iconTop;

    @Override
    public Icon getIcon(int side) {
        if (side == 0 || side == 1)
            return iconTop;
        else
            return icon;
    }

    @Override
    public void registerIcon(String modName, Block block_, IconRegister register) {
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
