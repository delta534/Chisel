package info.jbcs.minecraft.chisel.core.variation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class VariationTopBottom extends VariationTop {
    IIcon iconBot;

    @Override
    public IIcon getIcon(int side) {
        if (side == 0)
            return iconBot;
        return super.getIcon(side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(String modName, Block block_, IIconRegister register) {

        if (block != null) {
            if (block instanceof BlockPane) {
                icon = block.getBlockTextureFromSide(2);
                iconTop = ((BlockPane) block)
                        .getSideTextureIndex();
                iconBot = ((BlockPane) block)
                        .getSideTextureIndex();
            } else {
                icon = block.getIcon(2, blockMeta);
                iconTop = block.getIcon(1, blockMeta);
                iconBot = block.getIcon(0, blockMeta);
            }
        } else {
            super.registerIcon(modName, block, register);
            iconBot = getIconResource(modName + ":"
                    + texture + "-bottom", register);
        }
    }
}
