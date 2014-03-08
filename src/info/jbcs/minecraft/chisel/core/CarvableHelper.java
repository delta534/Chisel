package info.jbcs.minecraft.chisel.core;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.modCompat.ChiselModCompatibility;
import info.jbcs.minecraft.chisel.blocks.BlockChiselGrass;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleSlab;
import info.jbcs.minecraft.chisel.core.CarvableVariation.CarvableVariationCTM;
import info.jbcs.minecraft.chisel.items.ItemCarvable;
import info.jbcs.minecraft.chisel.modCompat.ChiselMicroMaterial;
import info.jbcs.minecraft.chisel.render.CTM;
import info.jbcs.minecraft.chisel.render.TextureSubmap;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.utilities.GeneralClient;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
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
    public static final int GRASS = 9;

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
    public Icon getIconResource(String resource,IconRegister registry)
    {
        return registry.registerIcon(resource);
    }
    public void addVariation(String description, int metadata, String texture,
            Block block, int blockMeta) {
        if (variations.size() > 15)
            return;

        if (blockName == null && block != null)
            blockName = block.getLocalizedName();
        else if (blockName == null && description != null)
            blockName = description;

        CarvableVariation variation = new CarvableVariation();
        variation.description = description;
        variation.metadata = metadata;
        variation.blockName = blockName;
        variation.useCTM = true;

        if (texture != null) {
            variation.texture = texture;

            String path = "/assets/" + modName + "/textures/blocks/"
                    + variation.texture;

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
                variation.kind = 3;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (ctmh && top) {
                variation.kind = 5;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (ctmv && top) {
                variation.kind = CTMV;
                variation.useCTM = Chisel.getCTMOption(description);

            } else if (bot && top && side) {
                variation.kind = 2;
            } else if (top && side) {
                variation.kind = 1;
            } else if (v9) {
                variation.kind = V9;
            } else if (v4) {
                variation.kind = V4;
            } else if (any && ctmx) {
                variation.kind = CTMX;
                variation.useCTM = Chisel.getCTMOption(description);
            } else if (any) {
                variation.kind = 0;
            } else {
                throw new RuntimeException(
                        "No valid textures found for chisel block variation '"
                                + description + "' (" + variation.texture + ")");
            }
        } else {
            variation.block = block;
            variation.kind = 2;
            variation.blockMeta = blockMeta;
        }
        int i = 0;
        if (variation.kind > 2)
            i++;
        variations.add(variation);
        map[metadata] = variation;
    }

    public CarvableVariation getVariation(int metadata) {
        if (metadata < 0 || metadata > 15)
            metadata = 0;

        CarvableVariation variation = map[metadata];
        if (variation == null)
            return null;

        // if(variation.kind!=CTMX)
        // return null;

        return variation;
    }
    public static Icon getIcon(int side, int metadata,CarvableVariation variation) {
        if (metadata < 0 || metadata > 15)
            metadata = 0;
        if (variation == null)
            return GeneralClient.getMissingIcon();

        switch (variation.kind) {
        case 0:
            return variation.icon;
        case 1:
            if (side == 0 || side == 1)
                return variation.iconTop;
            else
                return variation.icon;
        case 2:
            if (side == 1)
                return variation.iconTop;
            else if (side == 0)
                return variation.iconBot;
            else
                return variation.icon;
        case 3:
            return variation.ctm.seams[0].icons[0];
        case 4:
            if (side < 2)
                return variation.iconTop;
            else
                return variation.seamsCtmVert.icons[0];
        case 5:
            if (side < 2)
                return variation.iconTop;
            else
                return variation.seamsCtmVert.icons[0];
        case V9:
            return variation.variations9.icons[4];
        case V4:
            return variation.variations9.icons[0];
        case CTMX:
            return variation.icon;
        }

        return GeneralClient.getMissingIcon();
    }
    public Icon getIcon(int side, int metadata) {
        if (metadata < 0 || metadata > 15)
            metadata = 0;

        CarvableVariation variation = map[metadata];
        if (variation == null)
            return GeneralClient.getMissingIcon();

        switch (variation.kind) {
        case 0:
            return variation.icon;
        case 1:
            if (side == 0 || side == 1)
                return variation.iconTop;
            else
                return variation.icon;
        case 2:
            if (side == 1)
                return variation.iconTop;
            else if (side == 0)
                return variation.iconBot;
            else
                return variation.icon;
        case 3:
            return variation.ctm.seams[0].icons[0];
        case 4:
            if (side < 2)
                return variation.iconTop;
            else
                return variation.seamsCtmVert.icons[0];
        case 5:
            if (side < 2)
                return variation.iconTop;
            else
                return variation.seamsCtmVert.icons[0];
        case V9:
            return variation.variations9.icons[4];
        case V4:
            return variation.variations9.icons[0];
        case CTMX:
            return variation.icon;
        }

        return GeneralClient.getMissingIcon();
    }

    public static Icon getBlockTexture(IBlockAccess world, int x, int y, int z,
            int side, CarvableVariation variation) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata < 0 || metadata > 15)
            metadata = 0;
        if (variation == null)
            return GeneralClient.getMissingIcon();
        if (!variation.useCTM) {
            Block b = Block.blocksList[world.getBlockId(x, y, z)];
            return b.getIcon(side, metadata);
        } else {
            switch (variation.kind) {
            case 0:
            case 1:
            case 2:
                return getIcon(side, metadata,variation);
            case 3:
                int tex = CTM.getTexture(world, x, y, z, side);

                int row = tex / 16;
                int col = tex % 16;

                return variation.ctm.seams[col / 4].icons[col % 4 + row * 4];
            case 4:
                if (side < 2)
                    return variation.iconTop;

                int blockId = world.getBlockId(x, y, z);
                boolean topConnected = blockId == world.getBlockId(x, y + 1, z)
                        && metadata == world.getBlockMetadata(x, y + 1, z);
                boolean botConnected = blockId == world.getBlockId(x, y - 1, z)
                        && metadata == world.getBlockMetadata(x, y - 1, z);

                if (topConnected && botConnected)
                    return variation.seamsCtmVert.icons[2];
                if (topConnected && !botConnected)
                    return variation.seamsCtmVert.icons[3];
                if (!topConnected && botConnected)
                    return variation.seamsCtmVert.icons[1];
                return variation.seamsCtmVert.icons[0];
            case 5:
                if (side < 2)
                    return variation.iconTop;

                int id = world.getBlockId(x, y, z);

                boolean p;
                boolean n;
                boolean reverse = side == 2 || side == 4;

                if (side < 4) {
                    p = isSame(world, x - 1, y, z, id, metadata);
                    n = isSame(world, x + 1, y, z, id, metadata);
                } else {
                    p = isSame(world, x, y, z + 1, id, metadata);
                    n = isSame(world, x, y, z - 1, id, metadata);
                }

                if (p && n)
                    return variation.seamsCtmVert.icons[1];
                else if (p)
                    return variation.seamsCtmVert.icons[reverse ? 2 : 3];
                else if (n)
                    return variation.seamsCtmVert.icons[reverse ? 3 : 2];
                return variation.seamsCtmVert.icons[0];
            case V9:
            case V4:
                int index = x + y * 606731 + z * 571163 + side * 555491;
                if (index < 0)
                    index = -index;

                return variation.variations9.icons[index
                        % ((variation.kind == V9) ? 9 : 4)];
            case CTMX:
                return variation.icon;
            }
        }
        return GeneralClient.getMissingIcon();
    }

    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z,
            int side) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata < 0 || metadata > 15)
            metadata = 0;

        CarvableVariation variation = map[metadata];
        if (variation == null)
            return GeneralClient.getMissingIcon();
        if (!variation.useCTM) {
            Block b = Block.blocksList[world.getBlockId(x, y, z)];
            return b.getIcon(side, metadata);
        } else {
            switch (variation.kind) {
            case 0:
            case 1:
            case 2:
                return getIcon(side, metadata);
            case 3:
                int tex = CTM.getTexture(world, x, y, z, side);

                int row = tex / 16;
                int col = tex % 16;

                return variation.ctm.seams[col / 4].icons[col % 4 + row * 4];
            case 4:
                if (side < 2)
                    return variation.iconTop;

                int blockId = world.getBlockId(x, y, z);
                boolean topConnected = blockId == world.getBlockId(x, y + 1, z)
                        && metadata == world.getBlockMetadata(x, y + 1, z);
                boolean botConnected = blockId == world.getBlockId(x, y - 1, z)
                        && metadata == world.getBlockMetadata(x, y - 1, z);

                if (topConnected && botConnected)
                    return variation.seamsCtmVert.icons[2];
                if (topConnected && !botConnected)
                    return variation.seamsCtmVert.icons[3];
                if (!topConnected && botConnected)
                    return variation.seamsCtmVert.icons[1];
                return variation.seamsCtmVert.icons[0];
            case 5:
                if (side < 2)
                    return variation.iconTop;

                int id = world.getBlockId(x, y, z);

                boolean p;
                boolean n;
                boolean reverse = side == 2 || side == 4;

                if (side < 4) {
                    p = isSame(world, x - 1, y, z, id, metadata);
                    n = isSame(world, x + 1, y, z, id, metadata);
                } else {
                    p = isSame(world, x, y, z + 1, id, metadata);
                    n = isSame(world, x, y, z - 1, id, metadata);
                }

                if (p && n)
                    return variation.seamsCtmVert.icons[1];
                else if (p)
                    return variation.seamsCtmVert.icons[reverse ? 2 : 3];
                else if (n)
                    return variation.seamsCtmVert.icons[reverse ? 3 : 2];
                return variation.seamsCtmVert.icons[0];
            case V9:
            case V4:
                int index = x + y * 606731 + z * 571163 + side * 555491;
                if (index < 0)
                    index = -index;

                return variation.variations9.icons[index
                        % ((variation.kind == V9) ? 9 : 4)];
            case CTMX:
                return variation.icon;
            }
        }
        return GeneralClient.getMissingIcon();
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
    public void registerVariationIcon(String modName, Block block,
            IconRegister register, CarvableVariation variation) {
        this.registerVariationIcon(modName, block, register, variation,"");
    }
    public void registerVariationIcon(String modName, Block block,
            IconRegister register, CarvableVariation variation,String append) {
        switch (variation.kind) {
        case 0:
            variation.icon = getIconResource(modName + ":"
                    + variation.texture+append,register);
            break;
        case 1:
                variation.icon = getIconResource(modName + ":"
                        + variation.texture + "-side"+append,register);
                variation.iconTop = getIconResource(modName + ":"
                        + variation.texture + "-top"+append,register);
            break;
        case 2:
            variation.icon = getIconResource(modName + ":"
                    + variation.texture + "-side"+append,register);
            variation.iconTop = getIconResource(modName + ":"
                    + variation.texture + "-top"+append,register);
            variation.iconBot = getIconResource(modName + ":"
                    + variation.texture + "-bottom"+append,register);
            break;
        case 3:
            CarvableVariationCTM ctm = new CarvableVariationCTM();
            ctm.seams[0] = new TextureSubmap(getIconResource(modName
                    + ":" + variation.texture + "-ctm1"+append,register), 4, 4);
            ctm.seams[1] = new TextureSubmap(getIconResource(modName
                    + ":" + variation.texture + "-ctm2"+append,register), 4, 4);
            ctm.seams[2] = new TextureSubmap(getIconResource(modName
                    + ":" + variation.texture + "-ctm3"+append,register), 4, 4);
            variation.ctm = ctm;
            break;
        case 4:
            variation.seamsCtmVert = new TextureSubmap(
                    getIconResource(modName + ":" + variation.texture
                            + "-ctmv"+append,register), 2, 2);
            variation.iconTop = getIconResource(modName + ":"
                    + variation.texture + "-top"+append,register);
            break;
        case 5:
            variation.seamsCtmVert = new TextureSubmap(
                    getIconResource(modName + ":" + variation.texture
                            + "-ctmh"+append,register), 2, 2);
            variation.iconTop = getIconResource(modName + ":"
                    + variation.texture + "-top"+append,register);
            break;
        case V9:
            variation.variations9 = new TextureSubmap(
                    getIconResource(modName + ":" + variation.texture
                            + "-v9"+append,register), 3, 3);
            break;
        case V4:
            variation.variations9 = new TextureSubmap(
                    getIconResource(modName + ":" + variation.texture
                            + "-v4"+append,register), 2, 2);
            break;
        case CTMX:
            variation.icon = getIconResource(modName + ":"
                    + variation.texture+append,register);
            variation.submap = new TextureSubmap(getIconResource(modName
                    + ":" + variation.texture + "-ctm"+append,register), 4, 4);

            variation.submapSmall = new TextureSubmap(variation.icon, 2, 2);
            break;
        }
    }

    public void registerIcons(String modName, Block block, IconRegister register) {
        for (CarvableVariation variation : variations) {
            if (variation.block != null) {
                variation.block.registerIcons(register);

                if (variation.block instanceof BlockPane) {
                    variation.icon = variation.block.getBlockTextureFromSide(2);
                    variation.iconTop = ((BlockPane) variation.block)
                            .getSideTextureIndex();
                    variation.iconBot = ((BlockPane) variation.block)
                            .getSideTextureIndex();
                } else {
                    switch (variation.kind) {
                    case 0:
                        variation.icon = variation.block.getIcon(2,
                                variation.blockMeta);
                        break;
                    case 1:
                        variation.icon = variation.block.getIcon(2,
                                variation.blockMeta);
                        variation.iconTop = variation.block.getIcon(0,
                                variation.blockMeta);
                        break;
                    case 2:
                        variation.icon = variation.block.getIcon(2,
                                variation.blockMeta);
                        variation.iconTop = variation.block.getIcon(1,
                                variation.blockMeta);
                        variation.iconBot = variation.block.getIcon(0,
                                variation.blockMeta);
                        break;
                    }
                }
            } else {
                registerVariationIcon(modName, block, register, variation);
            }
        }
    }

    public void registerSubBlocks(Block block, CreativeTabs tabs, List list) {
        for (CarvableVariation variation : variations) {
            if ((!Chisel.overrideVanillaBlocks) && variation.block != null)
                continue;

            list.add(new ItemStack(block.blockID, 1, variation.metadata));
        }
    }

    public void setBlockHarvestLevel(Block block, String tool, int level) {
        for (CarvableVariation variation : variations) {
            MinecraftForge.setBlockHarvestLevel(block, variation.metadata,
                    tool, level);
        }
    }

    public static boolean isSame(IBlockAccess world, int x, int y, int z, int id,
            int meta) {
//        ConnectionCheckManager.checkConnection(world, x, y, z, id, meta);
        if(BlockChiselGrass.DirtGrassCheck(world, x, y, z, id, meta))
            return true;
        return world.getBlockId(x, y, z) == id
                && world.getBlockMetadata(x, y, z) == meta;
    }

    public void setBlockName(String name) {
        blockName = name;
    }

}
