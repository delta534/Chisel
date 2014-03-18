package info.jbcs.minecraft.chisel.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockLightstoneCarvable extends BlockMarble {
    public BlockLightstoneCarvable(int i) {
        super(i, Material.glass);
    }

    @Override
    public int quantityDropped(Random random) {
        return Block.glowStone.quantityDropped(random);
    }

    @Override
    public int idDropped(int i, Random random, int a) {
        return Item.glowstone.itemID;
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }
}
