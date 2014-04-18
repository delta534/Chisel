package info.jbcs.minecraft.chisel.core;

public class CarvingVariation implements Comparable {
    public CarvingVariation(int id, int metadata, int ord) {
        order = ord;
        blockId = id;
        meta = metadata;
        damage = metadata;
    }

    @Override
    public int compareTo(Object a) {
        return order - ((CarvingVariation) a).order;
    }

    public int order;
    public int blockId;
    public int meta;
    public int damage;
}
