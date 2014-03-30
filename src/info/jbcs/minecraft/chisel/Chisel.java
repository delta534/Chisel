package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.blocks.*;
import info.jbcs.minecraft.chisel.core.CarvingData;
import info.jbcs.minecraft.chisel.core.CarvingRegistry;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.gui.*;
import info.jbcs.minecraft.chisel.handlers.Packets;
import info.jbcs.minecraft.chisel.handlers.TinyChiselPacketHandler;
import info.jbcs.minecraft.chisel.items.*;
import info.jbcs.minecraft.chisel.modCompat.ChiselModCompatibility;
import info.jbcs.minecraft.chisel.proxy.Proxy;
import info.jbcs.minecraft.chisel.tiles.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.util.StepSoundEx;
import info.jbcs.minecraft.utilities.General;
import info.jbcs.minecraft.utilities.packets.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.util.ArrayList;

@Mod(modid = "chisel", name = "Chisel", version = "1.5.0", dependencies = "required-after:Autoutils")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, tinyPacketHandler = TinyChiselPacketHandler.class)
public class Chisel {
    public static ChiselModCompatibility modcompat;
    public static ItemChisel chisel;
    public static ItemChisel diamondChisel;

    public static Item itemIceshard;
    public static ItemCloudInABottle itemCloudInABottle;
    public static ItemBallOMoss itemBallOMoss;
    public static boolean hardMode;
    public static BlockMarble blockMarble;
    public static BlockMarble blockMarblePillar;
    public static BlockMarble blockLimestone;
    public static BlockMarbleSlab blockMarbleSlab;
    public static BlockMarbleSlab blockLimestoneSlab;
    public static BlockMarbleSlab blockMarblePillarSlab;
    public static BlockMarble blockCobblestone;
    public static BlockMarbleWall cobblestoneWall;
    public static BlockGlassCarvable blockGlass;
    public static BlockMarblePane blockPaneGlass;
    public static BlockMarble blockSandstone;
    public static BlockMarble blockSandstoneScribbles;
    public static BlockConcrete blockConcrete;
    public static BlockRoadLine blockRoadLine;
    public static BlockMarble blockIron;
    public static BlockMarble blockGold;
    public static BlockMarble blockDiamond;
    public static BlockLightstoneCarvable blockLightstone;
    public static BlockMarble blockLapis;
    public static BlockMarble blockEmerald;
    public static BlockMarble blockNetherBrick;
    public static BlockMarble blockNetherrack;
    public static BlockMarble blockCobblestoneMossy;
    public static BlockMarble stoneBrick;
    public static BlockMarbleStairs blockMarbleStairs;
    public static BlockMarbleStairs blockLimestoneStairs;
    public static BlockMarblePane blockPaneIron;
    public static BlockMarbleIce blockIce;
    public static BlockMarbleIce blockIcePillar;
    public static BlockMarbleIceStairs blockIceStairs;
    public static BlockMarble[] blockPlanks = new BlockMarble[4];
    public static BlockMarble blockObsidian;
    public static BlockPoweredMarble blockRedstone;
    public static BlockHolystone blockHolystone;
    public static BlockLavastone blockLavastone;
    public static BlockMarble blockFft;
    public static BlockMarble blockCarpet;
    public static BlockMarbleCarpet blockCarpetFloor;
    public static BlockMarble blockBookshelf;
    public static BlockMarble blockTyrian;
    public static BlockMarble blockDirt;
    public static BlockCloud blockCloud;
    public static BlockMarble blockTemple;
    public static BlockMarble blockTempleMossy;
    public static BlockMarble blockFactory;
    public static BlockChiselGrass blockGrass;
    public static BlockSnakestone blockSnakestone;
    public static BlockSnakestone blockSandSnakestone;
    public static BlockSnakestoneObsidian blockObsidianSnakestone;
    public static BlockAutoChisel blockAutoChisel;
    public static BlockSpikes blockSpiketrap;

    public static CreativeTabs tabChisel;

    public static int[] blockChiselGroups = new int[4096];

    public static boolean configExists;
    public static boolean overrideVanillaBlocks;
    public static boolean disableOverriding;
    public static boolean dropIceShards;
    public static boolean oldPillars;
    public static boolean disableCTM;
    public static double concreteVelocity;
    public static boolean oreDic;
    public static int particlesTickrate;
    public static boolean blockDescriptions;
    public static boolean rotateVCTM;
    public static boolean flipRecipe;
    public static boolean grassSpread;
    public static boolean disableMicroBlock;
    public static final StepSound soundHolystoneFootstep = new StepSoundEx(
            "chisel:holystone", "chisel:holystone", "chisel:holystone", 1.0f);
    public static final StepSound soundTempleFootstep = new StepSoundEx(
            "dig.stone", "chisel:temple-footstep", "dig.stone", 1.0f);
    public static final StepSound soundMetalFootstep = new StepSoundEx(
            "chisel:metal", "chisel:metal", "chisel:metal", 1.0f);

    public static Configuration config;

    public static int RenderEldritchId = 0;
    public static int RenderCTMId = 0;
    public static int RenderCarpetId = 0;
    @Instance("chisel")
    public static Chisel instance;

    @SidedProxy(clientSide = "info.jbcs.minecraft.chisel.proxy.ProxyClient", serverSide = "info.jbcs.minecraft.chisel.proxy.Proxy")
    public static Proxy proxy;

    public static boolean getCTMOption(String name) {
        if (disableCTM)
            return false;
        return config.get("Allow CTM", name, true).getBoolean(true);

    }

    public static boolean IsEnabled(String name) {
        return !config.get("Disable Blocks",name,false,"Set to true to disable the block").getBoolean(false);
    }

    public static boolean IsEnabled(Block refBlock) {
        return IsEnabled(refBlock.getUnlocalizedName());
    }


    public void walk(File root, ArrayList<File> fl) {
        File[] list = root.listFiles();
        if (list == null)
            return;

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f, fl);
            } else {
                fl.add(f);
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        configExists = configFile.exists();
        config = new Configuration(configFile);
        config.load();
        disableMicroBlock = config.get("general", "Disable Microblock support", false, "Set to true to disable microblock support").getBoolean(false);
        flipRecipe = config.get("general", "Flip Chisel Recipe", false, "Flips the Recipie for mod compatiblity").getBoolean(false);
        oreDic = config.get("General", "Add to ore dictionary", true,
                "Adds the variations of vanilla blocks to the ore dictionary if possible").getBoolean(true);
        hardMode = config.get("General", "Hardmode", false,
                "If true,chisels now have limited uses and no longer instantly break chisel blocks").getBoolean(false);
        rotateVCTM = config.get("General", "Rotate vertical connected textures", true,
                "Set to true to allow vertically connected textures to rotate and connect on the horizonal if connected on the horizontal").getBoolean(true);
        grassSpread = config.get("General", "Chisel Grass Spread", true,
                "Should Chisel Grass spread to Chisel Dirt").getBoolean(true);
        chisel = (ItemChisel) new ItemChisel(config.getItem("chisel", 7811)
                .getInt(), CarvingRegistry.chisel).setTextureName("chisel:chisel")
                .setUnlocalizedName("chisel.chisel")
                .setCreativeTab(CreativeTabs.tabTools);
        if (hardMode) {
            diamondChisel = (ItemChisel) new ItemChiselDiamond(config.getItem(
                    "diamond_chisel", 7812).getInt(), CarvingRegistry.chisel)
                    .setTextureName("chisel:Diamond_chisel")
                    .setUnlocalizedName("chisel.diamondchisel")
                    .setCreativeTab(CreativeTabs.tabTools);
        }
        itemCloudInABottle = (ItemCloudInABottle) new ItemCloudInABottle(config
                .getItem("cloudInABottle", 7813).getInt())
                .setUnlocalizedName("chisel.cloudinabottle")
                .setTextureName("Chisel:cloudinabottle")
                .setCreativeTab(CreativeTabs.tabTools);

        itemBallOMoss = (ItemBallOMoss) new ItemBallOMoss(config.getItem(
                "ballOMoss", 7814).getInt())
                .setUnlocalizedName("chisel.ballomoss")
                .setTextureName("chisel:ballomoss")
                .setCreativeTab(CreativeTabs.tabTools);

        dropIceShards = config.get("general", "ice drops ice shards", true,
                "Reworks ice blocks to drop Ice shards when destroyed")
                .getBoolean(true);
        concreteVelocity = config
                .get("general",
                        "concrete velocity",
                        0.45,
                        "Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.")
                .getDouble(0.45);
        particlesTickrate = config.get("general", "particle tickrate", 1,
                "Particle tick rate. Greater value = less particles. A value of -1 will turn off particles.")
                .getInt(1);
        oldPillars = config.get("general", "old pillar graphics", false,
                "Use old pillar textures").getBoolean(false);
        disableCTM = config.get("general", "disable connected textures", false,
                "Disable connected textures").getBoolean(false);
        blockDescriptions = config
                .get("general",
                        "use block descriptions in tooltips",
                        true,
                        "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.")
                .getBoolean(true);
        overrideVanillaBlocks = config
                .get("general",
                        "override vanilla blocks",
                        false,
                        "If true, will override vanilla blocks with their chisel variations. If false, will create one new chiseled block for each vanilla block that can be chiseled.")
                .getBoolean(true);
        disableOverriding = config
                .get("general",
                        "disable overriding blocks",
                        false,
                        "Some blocks, like bookshelves and ice will not get overridden using \"override vanilla blocks\" setting. Set this to true to disable all overriding.")
                .getBoolean(false);

        if (config
                .get("general",
                        "use custom creative tab",
                        true,
                        "Add a new tab in creative mode and put all blocks that work with chisel there.")
                .getBoolean(true)) {
            tabChisel = new CreativeTabs("chisel.tabChisel") {
                @Override
                public ItemStack getIconItemStack() {
                    return new ItemStack(chisel, 1, 0);
                }
            };


        } else {
            tabChisel = CreativeTabs.tabBlock;
        }

        if (dropIceShards) {
            itemIceshard = new Item(config.getItem("iceshard", 7812).getInt())
                    .setCreativeTab(CreativeTabs.tabMaterials)
                    .setUnlocalizedName("chisel.iceshard")
                    .setTextureName("chisel:iceshard");
        }
        if (IsEnabled("blockMarblePillar"))
            blockMarblePillar = (BlockMarble) new BlockMarble(
                    "blockMarblePillar", 2765).setHardness(2.0F)
                    .setResistance(10F).setStepSound(Block.soundStoneFootstep);
        if (IsEnabled("blockMarble"))
            blockMarble = (BlockMarble) new BlockMarble("blockMarble", 2761)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled("blockLimestone"))
            blockLimestone = (BlockMarble) new BlockMarble("blockLimestone", 2762)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled(Block.cobblestone))
            blockCobblestone = (BlockMarble) new BlockMarble(getBlock(
                    Block.cobblestone, 2787)).setHardness(2.0F).setResistance(10F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled(Block.glass))
            blockGlass = (BlockGlassCarvable) new BlockGlassCarvable(getBlock(
                    Block.glass, 2788)).setHardness(0.3F).setStepSound(
                    Block.soundGlassFootstep);
        if (IsEnabled(Block.sandStone))

            blockSandstone = (BlockMarble) new BlockMarble(getBlock(
                    Block.sandStone, 2789)).setStepSound(Block.soundStoneFootstep)
                    .setHardness(0.8F);
        if (IsEnabled("sandSnakestone"))
            blockSandSnakestone = (BlockSnakestone) new BlockSnakestone(getBlock(
                    "sandSnakestone", 2784), "Chisel:snakestone/sandsnake/")
                    .setUnlocalizedName("chisel.sandSnakestone");
        if (IsEnabled("blockSanstoneScribbles"))
            blockSandstoneScribbles = (BlockMarble) new BlockMarble(
                    "sandstoneScribbles", 2780).setStepSound(
                    Block.soundStoneFootstep).setHardness(0.8F);
        if (IsEnabled("chisel.concrete")) {
            blockConcrete = (BlockConcrete) new BlockConcrete("chisel.concrete", 2781)
                    .setStepSound(Block.soundStoneFootstep).setHardness(0.5F);

            blockRoadLine = (BlockRoadLine) new BlockRoadLine("roadLine", 2782)
                    .setStepSound(Block.soundStoneFootstep).setHardness(0.01F)
                    .setUnlocalizedName("chisel.roadLine");
        }
        if (IsEnabled(Block.blockIron))
            blockIron = (BlockMarble) new BlockMarble(getBlock(Block.blockIron,
                    2790)).setHardness(5F).setResistance(10F)
                    .setStepSound(Block.soundMetalFootstep);
        if (IsEnabled(Block.blockGold)) blockGold = (BlockMarble) new BlockMarble(getBlock(Block.blockGold,
                2804)).setHardness(3F).setResistance(10F)
                .setStepSound(Block.soundMetalFootstep);
        if (IsEnabled(Block.blockDiamond))
            blockDiamond = (BlockMarble) new BlockMarble(getBlock(
                    Block.blockDiamond, 2792)).setHardness(5F).setResistance(10F)
                    .setStepSound(Block.soundMetalFootstep);
        if (IsEnabled(Block.glowStone))
            blockLightstone = (BlockLightstoneCarvable) new BlockLightstoneCarvable(
                    getBlock(Block.glowStone, 2793)).setHardness(0.3F)
                    .setLightValue(1.0F).setStepSound(Block.soundGlassFootstep);
        if (IsEnabled(Block.blockLapis))
            blockLapis = (BlockMarble) new BlockMarble(getBlock(Block.blockLapis,
                    2794)).setHardness(3F).setResistance(5F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled(Block.blockEmerald))
            blockEmerald = (BlockMarble) new BlockMarble(getBlock(
                    Block.blockEmerald, 2795)).setHardness(5.0F)
                    .setResistance(10.0F).setStepSound(Block.soundMetalFootstep);
        if (IsEnabled(Block.netherBrick))
            blockNetherBrick = (BlockMarble) new BlockMarble(getBlock(
                    Block.netherBrick, 2796)).setHardness(2.0F)
                    .setResistance(10.0F).setStepSound(Block.soundStoneFootstep);
        if (IsEnabled(Block.netherrack))
            blockNetherrack = (BlockMarble) new BlockMarble(getBlock(
                    Block.netherrack, 2797)).setHardness(0.4F).setStepSound(
                    Block.soundStoneFootstep);
        if (IsEnabled(Block.cobblestoneMossy))
            blockCobblestoneMossy = (BlockMarble) new BlockMarble(getBlock(
                    Block.cobblestoneMossy, 2798)).setHardness(2.0F)
                    .setResistance(10.0F).setStepSound(Block.soundStoneFootstep);
        if (IsEnabled(Block.stoneBrick))

            stoneBrick = (BlockMarble) new BlockMarble(getBlock(Block.stoneBrick,
                    2799)).setHardness(1.5F).setResistance(10.0F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled("snakestone"))
            blockSnakestone = (BlockSnakestone) new BlockSnakestone(getBlock(
                    "snakestone", 2783), "Chisel:snakestone/snake/")
                    .setUnlocalizedName("chisel.snakestone");
        if (IsEnabled(Block.ice))

            blockIce = (BlockMarbleIce) new BlockMarbleIce(getBlockForce(Block.ice,
                    2800)).setHardness(0.5F).setLightOpacity(3)
                    .setStepSound(Block.soundGlassFootstep).setUnlocalizedName("chisel.ice");
        if (IsEnabled("icePillar"))

            blockIcePillar = (BlockMarbleIce) new BlockMarbleIce(getBlock(
                    "icePillar", 2775)).setHardness(0.5F).setLightOpacity(3)
                    .setStepSound(Block.soundGlassFootstep).setUnlocalizedName("chisel.icePillar");

        for (int i = 0; i < 4; i++) {
            String n = plank_names[i];
            String u = plank_ucnames[i];

            if (IsEnabled("wood-" + n))
                blockPlanks[i] = (BlockMarble) (new BlockMarble("wood-" + n,
                        2777 + i)).setHardness(2.0F).setResistance(5.0F)
                        .setStepSound(Block.soundWoodFootstep).setUnlocalizedName("chisel.wood." + u);
        }
        if (IsEnabled(Block.obsidian))

            blockObsidian = (BlockMarble) new BlockMarble(getBlock(Block.obsidian,
                    2801)).setHardness(50.0F).setResistance(2000.0F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled("snakestoneObsidian"))
            blockObsidianSnakestone = (BlockSnakestoneObsidian) new BlockSnakestoneObsidian(
                    getBlock("snakestoneObsidian", 2785),
                    "Chisel:snakestone/obsidian/")
                    .setUnlocalizedName("chisel.obsidianSnakestone").setHardness(50.0F)
                    .setResistance(2000.0F);
        if (IsEnabled(Block.blockRedstone))
            blockRedstone = (BlockPoweredMarble) (new BlockPoweredMarble(getBlock(
                    Block.blockRedstone, 2832), Material.iron)).setHardness(5.0F)
                    .setResistance(10.0F).setStepSound(Block.soundMetalFootstep);
        if (IsEnabled("chisel.holystone"))
            blockHolystone = (BlockHolystone) new BlockHolystone("chisel.holystone", 2833,
                    Material.rock).setHardness(2.0F).setResistance(10F)
                    .setStepSound(soundHolystoneFootstep);
        if (IsEnabled("chisel.lavastone"))

            blockLavastone = (BlockLavastone) new BlockLavastone("chisel.lavastone", 2834,
                    Material.rock, "lava_flow").setHardness(2.0F)
                    .setResistance(10F);
        if (IsEnabled("chisel.fft"))

            blockFft = (BlockMarble) new BlockMarble("chisel.fft", 2835, Material.rock)
                    .setHardness(2.0F).setResistance(10F);
        if (IsEnabled("carpet"))

            blockCarpet = (BlockMarble) new BlockMarble("carpet", 2836,
                    Material.cloth).setHardness(2.0F).setResistance(10F)
                    .setStepSound(Block.soundClothFootstep);
        if (IsEnabled("carpetFloor"))

            blockCarpetFloor = (BlockMarbleCarpet) new BlockMarbleCarpet(
                    "carpetFloor", 2844, Material.cloth).setHardness(2.0F)
                    .setResistance(10F).setStepSound(Block.soundClothFootstep);

        blockBookshelf = (BlockMarble) new BlockMarbleBookshelf(getBlock(
                Block.bookShelf, 2837)).setHardness(1.5F).setStepSound(
                Block.soundWoodFootstep);
        if (IsEnabled("tyrian"))

            blockTyrian = (BlockMarble) new BlockMarble("tyrian", 2838,
                    Material.iron).setHardness(5.0F).setResistance(10.0F)
                    .setStepSound(Block.soundMetalFootstep);
        if (IsEnabled(Block.dirt))

            blockDirt = (BlockMarble) new BlockMarble(getBlock(Block.dirt, 2839),
                    Material.ground).setHardness(0.5F).setStepSound(
                    Block.soundGravelFootstep);
        if (IsEnabled(Block.grass))

            blockGrass = (BlockChiselGrass) new BlockChiselGrass(getBlock(Block.grass, 2850)).setHardness(0.5F).setStepSound(
                    Block.soundGravelFootstep);
        if (IsEnabled("temple"))

            blockTemple = (BlockMarble) new BlockEldritch("temple", 2840)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(soundTempleFootstep);
        if (IsEnabled("templeMossy"))

            blockTempleMossy = (BlockMarble) new BlockEldritch("templeMossy", 2841)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(soundTempleFootstep);
        if (IsEnabled("cloud"))

            blockCloud = (BlockCloud) new BlockCloud("cloud", 2842)
                    .setHardness(0.2F).setLightOpacity(3)
                    .setStepSound(Block.soundClothFootstep);
        if (IsEnabled("factory"))

            blockFactory = (BlockMarble) new BlockMarble("factory", 2843)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(soundMetalFootstep);
        addPanes = config.get("general", "Add panes", true,
                "Changes pane rendering algorithm a bit.").getBoolean(true);
        if (addPanes) {
            if(IsEnabled(Block.fenceIron))
                blockPaneIron = (BlockMarblePane) new BlockMarblePane(getBlock(
                        Block.fenceIron, 2802), Material.iron, true).setHardness(
                        0.3F).setStepSound(Block.soundMetalFootstep);
            if(IsEnabled(Block.thinGlass))
                blockPaneGlass = (BlockMarblePane) new BlockMarblePane(getBlock(
                        Block.thinGlass, 2803), Material.glass, false).setHardness(
                        0.3F).setStepSound(Block.soundGlassFootstep);
        }
        if (IsEnabled("iceStairs"))

            makerIceStairs = new BlockMarbleStairsMaker(
                    "iceStairs", 2824, Block.ice);
        if (IsEnabled("blockLimestoneSlab"))

            blockLimestoneSlab = (BlockMarbleSlab) new BlockMarbleSlab(
                    "blockLimestoneSlab", 2764, 2807, blockLimestone).setHardness(
                    2.0F).setResistance(10F);

        if (IsEnabled("blockLimestoneStairs"))
            makerLimestoneStairs = new BlockMarbleStairsMaker(
                    "blockLimestoneStairs", 2816, blockLimestone);
        if (IsEnabled("blockMarblePillarSlab"))

            blockMarblePillarSlab = (BlockMarbleSlab) new BlockMarbleSlab(
                    "blockMarblePillarSlab", 2766, 2806, blockMarblePillar)
                    .setHardness(2.0F).setResistance(10F)
                    .setStepSound(Block.soundStoneFootstep);
        if (IsEnabled("blockMarbleStairs"))

            makerMarbleStairs = new BlockMarbleStairsMaker(
                    "blockMarbleStairs", 2808, blockMarble);
        if (IsEnabled("blockMarbleSlab"))

            blockMarbleSlab = (BlockMarbleSlab) new BlockMarbleSlab(
                    "blockMarbleSlab", 2763, 2805, blockMarble).setHardness(2.0F)
                    .setResistance(10F);

        blockAutoChisel = (BlockAutoChisel) new BlockAutoChisel(2844)
                .setHardness(2.0F).setResistance(10F);
        if (oreDic) {
            initOreDict();
        }

        GameRegistry.registerItem(itemCloudInABottle, "CloudInABottle", "chisel");
        GameRegistry.registerItem(chisel, "ItemChisel", "chisel");
        GameRegistry.registerItem(itemBallOMoss, "BallOMoss", "chisel");
        if (dropIceShards)
            GameRegistry.registerItem(itemIceshard, "IceShard", "chisel");
        //paste
        config.save();
        proxy.preInit();
    }

    static void initOreDict() {
        //this is all I know of so far that has ore dictionary names from various mods
        if (blockCobblestone != null)
            OreDictionary.registerOre("cobblestone", new ItemStack(blockCobblestone, 0, OreDictionary.WILDCARD_VALUE));
        if (blockGlass != null)
            OreDictionary.registerOre("glass", new ItemStack(blockGlass, 0, OreDictionary.WILDCARD_VALUE));
        if (blockSandstone != null)
            OreDictionary.registerOre("sandstone", new ItemStack(blockSandstone, 0, OreDictionary.WILDCARD_VALUE));
        if (blockIron != null)
            OreDictionary.registerOre("blockIron", new ItemStack(blockIron, 0, OreDictionary.WILDCARD_VALUE));
        if (blockGold != null)
            OreDictionary.registerOre("blockGold", new ItemStack(blockGold, 0, OreDictionary.WILDCARD_VALUE));
        if (blockDiamond != null)
            OreDictionary.registerOre("blockDiamond", new ItemStack(blockDiamond, 0, OreDictionary.WILDCARD_VALUE));
        if (blockLightstone != null)
            OreDictionary.registerOre("blockGlowstone", new ItemStack(blockLightstone, 0, OreDictionary.WILDCARD_VALUE));
        if (blockLapis != null)
            OreDictionary.registerOre("blockLapis", new ItemStack(blockLapis, 0, OreDictionary.WILDCARD_VALUE));
        if (blockEmerald != null)
            OreDictionary.registerOre("blockEmerald", new ItemStack(blockEmerald, 0, OreDictionary.WILDCARD_VALUE));
        if (blockCobblestoneMossy != null)
            OreDictionary.registerOre("stoneMossy", new ItemStack(blockCobblestoneMossy, 0, OreDictionary.WILDCARD_VALUE));
        if (blockIce != null)
            OreDictionary.registerOre("blockIce", new ItemStack(blockIce, 0, OreDictionary.WILDCARD_VALUE));
        for (int i = 0; i < 4; i++)
            if (blockPlanks[i] != null)
                OreDictionary.registerOre("plankWood", new ItemStack(blockPlanks[i], 0, OreDictionary.WILDCARD_VALUE));
        if (blockRedstone != null)
            OreDictionary.registerOre("blockRedstone", new ItemStack(blockRedstone, 0, OreDictionary.WILDCARD_VALUE));
    }


    static public BlockMarbleStairsMaker makerMarbleStairs;
    static public BlockMarbleStairsMaker makerLimestoneStairs;
    static public BlockMarbleStairsMaker makerIceStairs;
    static boolean addPanes = false;
    static String[] plank_names = {"oak", "spruce", "birch", "jungle"};
    static String[] plank_ucnames = {"Oak", "Spruce", "Birch", "Jungle"};

    int getBlock(String name, int id) {
        return config.getBlock(name, id).getInt(id);
    }

    int getBlock(Block block, int id) {
        if (disableOverriding || !overrideVanillaBlocks)
            return config.getBlock(General.getUnlocalizedName(block), id)
                    .getInt(id);

        Block.blocksList[block.blockID] = null;
        return block.blockID;
    }

    int getBlockForce(Block block, int id) {
        if (disableOverriding)
            return config.getBlock(General.getUnlocalizedName(block), id)
                    .getInt(id);

        Block.blocksList[block.blockID] = null;
        return block.blockID;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {


        /* LanguageRegistry.addName(dchisel, "Diamond Chisel"); */

        // needle = (ItemChisel) new
        // ItemChisel(config.getItem("needle",7816).getInt(),CarvingRegistry.needle).setTextureName("chisel:needle").setUnlocalizedName("needle").setCreativeTab(CreativeTabs.tabTools);
        // LanguageRegistry.addName(needle, "Needle");


        //LanguageRegistry.addName(itemCloudInABottle, "Cloud in a bottle");
        EntityRegistry.registerModEntity(EntityCloudInABottle.class,
                "CloudInABottle", 1, this, 40, 1, true);


        EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2,
                this, 40, 1, true);


        if (dropIceShards) {

            // itemIceshard=new
            // Item(2582).setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("Chisel:iceshard").func_111206_d("Chisel:iceshard");

            CraftingManager.getInstance().addRecipe(
                    new ItemStack(Block.ice, 1),
                    new Object[]{"**", "**", '*', itemIceshard,});
        }
        if (blockMarble != null)
            CarvingData.MarbleInit(blockMarble);
        if (blockMarblePillar != null)
        {
            if (oldPillars) {
                CarvingData.OldPillarsInit(blockMarblePillar);
            } else {
                CarvingData.PillarsInit(blockMarblePillar);
            }
        }
        if (blockLimestone != null)
            CarvingData.LimestoneInit(blockLimestone);
        if (blockCobblestone != null)
            CarvingData.CobblestoneInit(blockCobblestone);
        if (blockGlass != null)
            CarvingData.GlassInit(blockGlass);
        if (blockSandstone != null)
            CarvingData.SandstoneInit(blockSandstone);
        if (blockSandSnakestone != null)
            CarvingData.SandSnakestoneInit(blockSandSnakestone);
        if (blockSandstoneScribbles != null)
            CarvingData.SandstoneScribbleInit(blockSandstoneScribbles);
        if (blockConcrete != null)
            CarvingData.ConcreteInit(blockConcrete);
        if (blockIron != null)
            CarvingData.IronInit(blockIron);
        if (blockGold != null)
            CarvingData.GoldInit(blockGold);
        if (blockDiamond != null)
            CarvingData.DiamondInit(blockDiamond);
        if (blockLightstone != null)
            CarvingData.LightstoneInit(blockLightstone);
        if (blockLapis != null)
            CarvingData.LapisInit(blockLapis);
        if (blockEmerald != null)
            CarvingData.EmeraldInit(blockEmerald);
        if (blockNetherBrick != null)
            CarvingData.NetherBrickInit(blockNetherBrick);
        if (blockNetherrack != null)
            CarvingData.NetherrackInit(blockNetherrack);
        if (blockCobblestoneMossy != null)
            CarvingData.MossyCobblestoneInit(blockCobblestoneMossy);
        if (stoneBrick != null)
            CarvingData.StoneBrickInit(stoneBrick);
        if (blockSnakestone != null)
            CarvingData.SnakeStoneInit(blockSnakestone);
        if (blockIce != null)
            CarvingData.IceInit(blockIce);
        if (blockIcePillar != null)
            CarvingData.IcePillarInit(blockIcePillar);
        for (int i = 0; i < 4; i++) {
            String n = plank_names[i];
            String u = plank_ucnames[i];
            if (blockPlanks[i] != null)
                CarvingData.WoodInit(i, n, u, blockPlanks);
        }
        if (blockObsidian != null)
            CarvingData.ObsidianInit(blockObsidian);
        if (blockObsidianSnakestone != null)
            CarvingData.ObsidianSnakestoneInit(blockObsidianSnakestone);
        if (blockRedstone != null)
            CarvingData.RedstoneInit(blockRedstone);
        if (blockHolystone != null)
            CarvingData.HolystoneInit(blockHolystone);
        if (blockLavastone != null)
            CarvingData.LavastoneInit(blockLavastone);
        if (blockFft != null)
            CarvingData.FFTInit(blockFft);
        if (blockCarpet != null)
            CarvingData.CarpetInit(blockCarpet);
        if (blockCarpetFloor != null)
            CarvingData.CarpetFloorInit(blockCarpetFloor);
        if (blockBookshelf != null)
            CarvingData.BookshelfInit(blockBookshelf);
        if (blockTyrian != null)
            CarvingData.FutureBlockInit(blockTyrian);
        if (blockTyrian != null)
            CarvingData.DirtInit(blockDirt);
        if (blockGrass != null)
            CarvingData.GrassInit(blockGrass);
        if (blockTemple != null)
            CarvingData.TempleInit(blockTemple);
        if (blockTempleMossy != null)
            CarvingData.TempleMossyInit(blockTempleMossy);
        if (blockCloud != null)
            CarvingData.CloudInit(blockCloud);
        if (blockFactory != null)
            CarvingData.FactoryBlockInit(blockFactory);

        // get everything setup that we want to pass onto FMP
        modcompat = new ChiselModCompatibility();
        modcompat.Init(event);
        // Do the rest
        if (addPanes) {
            if(blockPaneIron!=null)
                CarvingData.IronFenceInit(blockPaneIron);

            if(blockPaneGlass!=null)
                CarvingData.GlassPaneInit(blockPaneGlass);
        }
        if (makerIceStairs != null)
            CarvingData.IceStairsInit(makerIceStairs);
        if (blockLimestoneSlab != null)
            CarvingData.LimestoneSlabInit(blockLimestoneSlab);
        if (makerLimestoneStairs != null)
            CarvingData.LimestoneStairsInit(makerLimestoneStairs);
        if (blockMarblePillarSlab != null)
            CarvingData.MarblePillarSlabInit(blockMarblePillarSlab);
        if (makerMarbleStairs != null)
            CarvingData.MarbleStairsInit(makerMarbleStairs);
        if (blockMarblePillarSlab != null)
            CarvingData.MarbleSlabInit(blockMarbleSlab);

        GameRegistry.registerBlock(blockAutoChisel, "autoChisel");
        GameRegistry.registerTileEntity(TileEntityAutoChisel.class,
                "TEAutoChisel");
        MinecraftForge.setBlockHarvestLevel(Block.stone, 0, "chisel", 0);


        for (int meta = 0; meta < 16; meta++) {
            if (blockMarble != null) {
                if (blockMarblePillar != null) {
                    GameRegistry.addRecipe(
                            new ItemStack(blockMarble, 4),
                            new Object[]{"XX", "XX", 'X',
                                    new ItemStack(blockMarblePillar, 1, meta),}
                    );
                    GameRegistry.addRecipe(
                            new ItemStack(blockMarblePillar, 6),
                            new Object[]{"XX", "XX", "XX", 'X',
                                    new ItemStack(blockMarble, 1, meta),}
                    );
                    if (blockMarblePillarSlab != null)
                        GameRegistry.addRecipe(
                                new ItemStack(blockMarblePillarSlab, 6, 0),
                                new Object[]{"***", '*',
                                        new ItemStack(blockMarblePillar, 1, meta)}
                        );


                }
                if (blockMarbleSlab != null)
                    GameRegistry.addRecipe(
                            new ItemStack(blockMarbleSlab, 6, 0),
                            new Object[]{"***", '*',
                                    new ItemStack(blockMarble, 1, meta)}
                    );
            }
            if (blockLimestone != null && blockLimestoneSlab != null)
                GameRegistry.addRecipe(
                        new ItemStack(blockLimestoneSlab, 6, 0),
                        new Object[]{"***", '*',
                                new ItemStack(blockLimestone, 1, meta)}
                );

            if (blockIcePillar != null) {
                GameRegistry.addRecipe(
                        new ItemStack(blockIcePillar, 6),
                        new Object[]{"XX", "XX", "XX", 'X',
                                new ItemStack(blockIce, 1, meta),}
                );

                GameRegistry.addRecipe(
                        new ItemStack(blockIce, 4),
                        new Object[]{"XX", "XX", 'X',
                                new ItemStack(blockIcePillar, 1, meta),}
                );
            }
            if (blockSandstoneScribbles != null)
                GameRegistry.addRecipe(
                        new ItemStack(blockSandstone, 1),
                        new Object[]{"X", 'X',
                                new ItemStack(blockSandstoneScribbles, 1, meta),}
                );
            if (blockCarpet != null) {
                GameRegistry.addRecipe(
                        new ItemStack(blockCarpet, 8, meta),
                        new Object[]{"YYY", "YXY", "YYY", 'X',
                                new ItemStack(Item.silk, 1), 'Y',
                                new ItemStack(Block.cloth, 1, meta),}
                );
                if (blockCarpetFloor != null)
                    GameRegistry.addRecipe(
                            new ItemStack(blockCarpetFloor, 3, meta),
                            new Object[]{"XX", 'X',
                                    new ItemStack(blockCarpet, 1, meta),}
                    );
            }
        }


        GameRegistry.addRecipe(new ShapedOreRecipe(blockAutoChisel, false,
                new Object[]{"XXX", "X*X", "XrX", '*',
                        new ItemStack(chisel, 1), 'X', "plankWood", 'r',
                        Item.redstone}
        ));
        if (!flipRecipe) {
            if (config.get("general", "Alternative recipe", false,
                    "Use alternative crafting recipe for the chisel").getBoolean(
                    false)) {
                GameRegistry.addRecipe(new ShapedOreRecipe(chisel, true,
                        new Object[]{" YY", " YY", "X  ", 'X', "stickWood", 'Y',
                                Item.ingotIron}
                ));

            } else {
                GameRegistry.addRecipe(new ShapedOreRecipe(chisel, true,
                        new Object[]{" Y", "X ", 'X', "stickWood", 'Y',
                                Item.ingotIron}
                ));
            }
        } else {
            if (config.get("general", "Alternative recipe", false,
                    "Use alternative crafting recipe for the chisel").getBoolean(
                    false)) {
                GameRegistry.addRecipe(new ShapedOreRecipe(chisel, true,
                        new Object[]{"YY ", "YY ", "  X", 'X', "stickWood", 'Y',
                                Item.ingotIron}
                ));

            } else {
                GameRegistry.addRecipe(new ShapedOreRecipe(chisel, true,
                        new Object[]{"Y  ", "  X", 'X', "stickWood", 'Y',
                                Item.ingotIron}
                ));
            }
        }

        GameRegistry.addRecipe(
                new ItemStack(itemBallOMoss, 1),
                new Object[]{"XYX", "YXY", "XYX", 'X', Block.vine, 'Y',
                        Item.stick}
        );


        NetworkRegistry.instance().registerGuiHandler(this, new IGuiHandler() {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player,
                                              World world, int x, int y, int z) {
                if (ID == 1) {
                    TileEntity te = world.getBlockTileEntity(x, y, z);
                    if (te instanceof TileEntityAutoChisel)
                        return new ContainerAutoChisel(
                                (TileEntityAutoChisel) te, player.inventory);

                }
                return new ContainerChisel(player.inventory,
                        new InventoryChiselSelection(null));
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player,
                                              World world, int x, int y, int z) {
                if (ID == 1) {
                    TileEntity te = world.getBlockTileEntity(x, y, z);
                    if (te instanceof TileEntityAutoChisel)
                        ;
                    return new GuiAutoChisel(player.inventory,
                            (TileEntityAutoChisel) te);

                }
                return new GuiChisel(player.inventory,
                        new InventoryChiselSelection(null));
            }
        });


        Packets.chiseled.create();
        PacketHandler.register(this);

        proxy.init();
        if (blockMarble != null)
            blockChiselGroups[blockMarble.blockID] = 1;
        if (blockMarblePillar != null)
            blockChiselGroups[blockMarblePillar.blockID] = 1;
        if (blockIce != null)
            blockChiselGroups[blockIce.blockID] = 2;
        if (blockIcePillar != null)
            blockChiselGroups[blockIcePillar.blockID] = 2;

        config.save();

        MinecraftForge.EVENT_BUS.register(this);
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        modcompat.postInit(event);
    }
}
