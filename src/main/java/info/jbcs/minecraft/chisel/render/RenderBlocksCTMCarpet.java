package info.jbcs.minecraft.chisel.render;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class RenderBlocksCTMCarpet extends RenderBlocksCTM {
    public RenderBlocksCTMCarpet() {
        super();
    }

    @Override
    public void resetVertices() {
        super.resetVertices();

        for (int i = 0; i < Y.length; i++) {
            Y[i] /= 16;
        }
    }


    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
        rendererOld.renderFaceXNeg(block, 0, 0, 0, submapSmall.icon);
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
        rendererOld.renderFaceXPos(block, 0, 0, 0, submapSmall.icon);
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
        rendererOld.renderFaceZNeg(block, 0, 0, 0, submapSmall.icon);
    }

    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
        rendererOld.renderFaceZPos(block, 0, 0, 0, submapSmall.icon);
    }

}
