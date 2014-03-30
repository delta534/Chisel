package info.jbcs.minecraft.chisel.items;

import info.jbcs.minecraft.chisel.core.CarvingRegistry;

public class ItemChiselDiamond extends ItemChisel {

    public ItemChiselDiamond(int id, CarvingRegistry c) {
        super(id, c);
        this.setMaxDamage(-1);
    }

}
