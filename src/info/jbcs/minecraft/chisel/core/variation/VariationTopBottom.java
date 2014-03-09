package info.jbcs.minecraft.chisel.core.variation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class VariationTopBottom extends  VariationTop {
    Icon iconBot;
    @Override
    public Icon getIcon(int side) {
        if(side==0)
            return iconBot;
        return super.getIcon(side);
    }

    @Override
    public void registerIcon(String modName, Block block_, IconRegister register) {

        if(block!=null)
        {   if (block instanceof BlockPane) {
            icon = block.getBlockTextureFromSide(2);
            iconTop = ((BlockPane) block)
                    .getSideTextureIndex();
            iconBot = ((BlockPane) block)
                    .getSideTextureIndex();
        }
            else
        {
            icon=block.getIcon(2,blockMeta);
            iconTop=block.getIcon(1,blockMeta);
            iconBot=block.getIcon(0,blockMeta);
        }
        }
        else
        {
            super.registerIcon(modName, block, register);
        iconBot = getIconResource(modName + ":"
                + texture + "-bottom",register);
        }
    }
}
