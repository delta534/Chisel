package info.jbcs.minecraft.chisel.items;

import info.jbcs.minecraft.chisel.core.Carving;

public class ItemChiselDiamond extends ItemChisel {

    public ItemChiselDiamond(int id, Carving c) {
        super(id, c);
        this.setMaxDamage(-1);
    }

}
