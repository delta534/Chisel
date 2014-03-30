package info.jbcs.minecraft.chisel.core.variation;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.blocks.BlockMarble;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;

public class GrassCarvableHelper extends CarvableHelper {
    public static BlockMarble chiselDirt = Chisel.blockDirt;
    GrassVariation[] grasses = new GrassVariation[16];
    Boolean[] tested = new Boolean[16];

    public GrassCarvableHelper() {
        super();
        for (int i = 0; i < 16; i++)
            tested[i] = false;
    }

    @Override
    public RenderVariation getVariation(int metadata) {
        if (tested[metadata] == false) {
            RenderVariation cv = chiselDirt.getVariation(metadata);
            if (cv == null)
                cv = chiselDirt.getVariation(0);
            RenderVariation cvg = super.getVariation(metadata);
            if (cvg != null)
                grasses[metadata] = new GrassVariation(cvg, cv);
            tested[metadata] = true;
        }
        return grasses[metadata];
    }
}

