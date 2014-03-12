package info.jbcs.minecraft.chisel.render;

import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.Chisel;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class BlockCarpetRenderer extends BlockAdvancedMarbleRenderer {

    public BlockCarpetRenderer() {
        super();

        Chisel.RenderCarpetId = RenderingRegistry.getNextAvailableRenderId();

        rendererCTM = new RenderBlocksCTMCarpet();

        blockBounds.max.y=0.0625;
        model.generateBlock(0,blockBounds);


    }

    @Override
    public int getRenderId() {
        return Chisel.RenderCarpetId;
    }
}
