package info.jbcs.minecraft.chisel.core;

import java.util.List;

public interface Carvable {
	CarvableVariation getVariation(int metadata);
    CarvableHelper getHelper();
}
