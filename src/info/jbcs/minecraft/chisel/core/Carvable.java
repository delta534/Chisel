package info.jbcs.minecraft.chisel.core;

public interface Carvable {
    RenderVariation getVariation(int metadata);

    CarvableHelper getHelper();
}
