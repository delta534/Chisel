package info.jbcs.minecraft.chisel.core;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.variation.*;
import info.jbcs.minecraft.chisel.blocks.BlockChiselGrass;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleSlab;
import info.jbcs.minecraft.chisel.items.ItemCarvable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CarvableHelper {
    static final String modName = "chisel";
    public static final ArrayList<Block> chiselBlocks = new ArrayList<Block>();

    public static final int NORMAL = 0;
    public static final int TOPSIDE = 1;
    public static final int TOPBOTSIDE = 2;
    public static final int CTM3 = 3;
    public static final int CTMV = 4;
    public static final int CTMH = 5;
    public static final int V9 = 6;
    public static final int V4 = 7;
    public static final int CTMX = 8;

    public CarvableHelper() {
    }

    public ArrayList<CarvableVariation> variations = new ArrayList<CarvableVariation>();
    CarvableVariation[] map = new CarvableVariation[16];
    public boolean forbidChiseling = false;
    public String blockName;

    public void addVariation(String description, int metadata, Block bb) {
        addVariation(description, metadata, null, bb, 0);
    }

    public void addVariation(String description, int metadata, Block bb,
            int blockMeta) {
        addVariation(description, metadata, null, bb, blockMeta);
    }

    public void addVariation(String description, int metadata, String texture) {
        addVariation(description, metadata, texture, null, 0);
    }
    public void addVariation(String description, int metadata, String texture,
            Block block, int blockMeta) {
        if (variations.size() > 15)
            return;

        if (blockName == null && block != null)
            blockName = block.getLocalizedName();
        else if (blockName == null && description != null)
            blockName = description;

        CarvableVariation variation;

        if (texture != null) {

            String path = "/assets/" + modName + "/textures/blocks/"
                    + texture;

            boolean any = Chisel.class.getResource(path + ".png") != null;
            boolean ctm3 = Chisel.class.getResource(path + "-ctm1.png") != null
                    && Chisel.class.getResource(path + "-ctm2.png") != null
                    && Chisel.class.getResource(path + "-ctm3.png") != null;
            boolean ctmv = Chisel.class.getResource(path + "-ctmv.png") != null;
            boolean ctmh = Chisel.class.getResource(path + "-ctmh.png") != null;
            boolean side = Chisel.class.getResource(path + "-side.png") != null;
            boolean top = Chisel.class.getResource(path + "-top.png") != null;
            boolean bot = Chisel.class.getResource(path + "-bottom.png") != null;
            boolean v9 = Chisel.class.getResource(path + "-v9.png") != null;
            boolean v4 = Chisel.class.getResource(path + "-v4.png") != null;
            boolean ctmx = Chisel.class.getResource(path + "-ctm.png") != null;
            boolean grass = Chisel.class.getResource(path + "-side_snow.png") != null;
            if (ctm3) {
                variation=new VariationCTM3();
                variation.kind = 3;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (ctmh && top) {
                variation=new VariationCTMH();
                variation.kind = 5;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (ctmv && top) {
                variation=new VariationCTMV();
                variation.kind = CTMV;
                variation.useCTM = Chisel.getCTMOption(description);

            } else if (bot && top && side) {
                variation=new VariationTopBottom();
                variation.kind = 2;
            } else if (top && side) {
                variation=new VariationTop();
                variation.kind = 1;
            } else if (v9) {
                variation=new VariationV9(9,4);
                variation.kind = V9;
            } else if (v4) {
                variation=new VariationV9(4,0);
                variation.kind = V4;
            } else if (any && ctmx) {
                variation=new VariationCTMX();
                variation.kind = CTMX;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (any) {
                variation=new CarvableVariation();
                variation.kind = 0;
            } else {
                throw new RuntimeException(
                        "No valid textures found for chisel block variation '"
                                + description + "' (" + texture + ")");
            }
            variation.texture = texture;
        } else {
            variation =new VariationTopBottom();
            variation.block = block;
            variation.kind = 2;
            variation.blockMeta = blockMeta;
        }

        variation.description = description;
        variation.metadata = metadata;
        variation.blockName = blockName;
        variations.add(variation);
        map[metadata] = variation;
    }

    public CarvableVariation getVariation(int metadata) {
        if (metadata < 0 || metadata > 15)
            metadata = 0;

        CarvableVariation variation = map[metadata];
        if (variation == null)
            return null;

        return variation;
    }

    public void register(Block block, String name) {
        register(block, name, ItemCarvable.class);
    }

    public void register(Block block, String name, Class cl) {
        block.setUnlocalizedName(name);

        Item.itemsList[block.blockID] = null;
        GameRegistry.registerBlock(block, cl, name);

        if (block instanceof BlockMarbleSlab) {
            BlockMarbleSlab slab = (BlockMarbleSlab) block;
            GameRegistry.registerBlock(slab.top, cl, name + ".top");
        }

        for (CarvableVariation variation : variations) {
            registerVariation(name, variation, block, variation.metadata);

            if (block instanceof BlockMarbleSlab
                    && ((BlockMarbleSlab) block).isBottom) {
                BlockMarbleSlab slab = (BlockMarbleSlab) block;
                MinecraftForge.setBlockHarvestLevel(slab.top,
                        variation.metadata, "chisel", 0);

                slab.top.setHardness(slab.blockHardness).setResistance(
                        slab.blockResistance);

                if (!forbidChiseling) {
                    Carving.chisel.addVariation(name + ".top",
                            slab.top.blockID, variation.metadata, 0);
                    Carving.chisel.setGroupClass(name + ".top", name);
                }
            }
        }

        chiselBlocks.add(block);
    }

    public void registerVariation(String name, CarvableVariation variation,
            Block block, int blockMeta) {

        LanguageRegistry.addName(new ItemStack(block.blockID, 1, blockMeta),
                Chisel.blockDescriptions ? variation.blockName
                        : variation.description);

        if (forbidChiseling)
            return;

        if (variation.block == null) {
            Carving.chisel.addVariation(name, block.blockID, blockMeta,
                    variation.metadata);
            MinecraftForge.setBlockHarvestLevel(block, blockMeta, "chisel", 0);
        } else {
            Carving.chisel.addVariation(name, variation.block.blockID,
                    variation.blockMeta, variation.metadata);
            MinecraftForge.setBlockHarvestLevel(variation.block,
                    variation.blockMeta, "chisel", 0);
        }
    }

    public void registerIcons(String modName, Block block, IconRegister register) {
        for (CarvableVariation variation : variations) {
            if(variation.block!=null)
                variation.block.registerIcons(register);
            variation.registerIcon(modName, block, register);
        }
    }

    public void registerSubBlocks(Block block, CreativeTabs tabs, List list) {
        registerSubBlocks(block, tabs, list,!Chisel.overrideVanillaBlocks);
    }
    public void registerSubBlocks(Block block, CreativeTabs tabs, List list,boolean override)
    {
        for (CarvableVariation var:variations)
        {
            if(var.block!=null&&override)
                continue;
            list.add(new ItemStack(block.blockID,1,var.metadata));
        }
    }

    public void setBlockHarvestLevel(Block block, String tool, int level) {
        for (CarvableVariation variation : variations) {
            MinecraftForge.setBlockHarvestLevel(block, variation.metadata,
                    tool, level);
        }
    }



    public void setBlockName(String name) {
        blockName = name;
    }

}
