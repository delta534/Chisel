package info.jbcs.minecraft.chisel.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.items.ItemCarvable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class BlockMarbleStairsMaker {
    public CarvableHelper carverHelper;
    BlockMarbleStairs blocks[];

    int idStart;
    Block blockBase;
    String blockName;

    public BlockMarbleStairsMaker(String name, int id, Block base) {
        carverHelper = new CarvableHelper();

        blockName = name;
        idStart = id;
        blockBase = base;
    }

    public void create() {
        create(null);
    }

    public void create(BlockMarbleStairsMakerCreator creator) {
        blocks = new BlockMarbleStairs[carverHelper.variations.size() / 2];

        for (int i = 0; i < blocks.length; i++) {
            String n = blockName + "." + i;
            blocks[i] = creator == null ?
                    new BlockMarbleStairs(n, idStart + i, blockBase, i * 2, carverHelper, i) :
                    creator.create(n, idStart + i, blockBase, i * 2, carverHelper, i);

            blocks[i].setUnlocalizedName("chisel." + blockName);
            GameRegistry.registerBlock(blocks[i], ItemCarvable.class, n);

            for (int meta = 0; meta < 2 && i * 2 + meta < carverHelper.variations.size(); meta++) {
                RenderVariation variation = carverHelper.variations.get(i * 2 + meta);
                variation.description = "tile.chisel." + carverHelper.blockName + "." + (i * 2 + meta) + ".description";
                for (int j = 0; j < 8; j++)
                    carverHelper.registerVariation(blockName + ".orientation." + j, variation, blocks[i], j + meta * 8);

                CraftingManager.getInstance().addRecipe(new ItemStack(blocks[i], 4, meta * 8), new Object[]{"*  ", "** ", "***", '*', new ItemStack(blockBase, 1, i * 2 + meta)});
            }

            CarvableHelper.chiselBlocks.add(blocks[i]);
        }

    }

}
