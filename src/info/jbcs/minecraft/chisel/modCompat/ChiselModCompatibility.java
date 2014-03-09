package info.jbcs.minecraft.chisel.modCompat;

import info.jbcs.minecraft.chisel.blocks.BlockMarbleCarpet;
import info.jbcs.minecraft.chisel.blocks.BlockMarblePillar;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.Carving;
import info.jbcs.minecraft.chisel.handlers.ChiselEventHandler;
import info.jbcs.minecraft.chisel.modCompat.ChiselMicroMaterial;
import info.jbcs.minecraft.chisel.modCompat.ChiselPillarMicroMaterial;
import info.jbcs.minecraft.chisel.modCompat.MultiPartCarpetTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import codechicken.microblock.MicroMaterialRegistry;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ChiselModCompatibility {

	static Method carpentersHackGet;
	static Class carpenterTEHack;
	static HashSet<Integer> carpentersBlocks=new HashSet<Integer>();
	static ChiselEventHandler eh;
	public static boolean hasFMP;
	abstract class ClassCompat{
		Class cl;

		public ClassCompat(String name){
			try {				 
				if((cl = Class.forName(name))!=null)
					action();
			} catch (ClassNotFoundException e) {
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}

		abstract void action() throws Exception;

		abstract class BlockCompat{
			Block block;

			public BlockCompat(String variableName) throws Exception{
				block=(Block) ClassCompat.this.cl.getField(variableName).get(null);

				if(block!=null)
					action();
			}

			abstract void action() throws Exception;
		};
	};

	abstract class ClassBlockCompat{
		Block block;

		ClassBlockCompat(final String className,final String blockName){
			new ClassCompat(className){
				@Override void action() throws Exception {
					new BlockCompat(blockName){
						@Override void action() throws Exception {
							ClassBlockCompat.this.doAction(block);
						}
					};
				}
			};
		}

		void doAction(Block b){
			block=b;

			action();
		}

		abstract void action();
	}

	public static int getBlockID(IBlockAccess world,int x,int y,int z)
	{
		int id=world.getBlockId(x, y, z);
		if(carpentersBlocks.contains(id))
		{
			try {
				id=((Block)(carpentersHackGet.invoke(null,world, 6,x,y,z))).blockID;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	public static int getBlockID(Block bl,IBlockAccess world,int x,int y,int z)
	{
		int id=bl.blockID;
		if(carpentersBlocks.contains(id))
		{
			try {
				id=((Block)(carpentersHackGet.invoke(null,world, 6,x,y,z))).blockID;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	public static void setBlockID(World world, int x,int y,int z,int newID,int meta)
	{
		int id=world.getBlockId(x, y, z);
		TileEntity te=world.getBlockTileEntity(x, y, z);
		if(carpentersBlocks.contains(id))
		{
			try
			{
				Field fieldCover=carpenterTEHack.getField("cover");
				short s=(short)(newID+(meta << 12));
				short[] covers=(short[]) fieldCover.get(te);
				covers[6]=s;
				world.setBlockMetadataWithNotify(x, y, z, meta, 2);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			world.setBlock(x,y,z,newID,meta,2);
		}
	}
	
	public void Init(FMLInitializationEvent event)
	{
		
		new ClassCompat("codechicken.microblock.MicroMaterialRegistry")
		{
			@Override
			void action() throws Exception {
                ConnectionCheckManager.addCheck(new ChiselMicroMaterial.microMaterialCheck());
				MultiPartCarpetTest.init();
				eh=new ChiselEventHandler();
				MinecraftForge.EVENT_BUS.register(eh);
				Class out=Class.forName("codechicken.microblock.MicroMaterialRegistry$IMicroMaterial");
				Method m=cl.getMethod("registerMaterial", out,String.class);
				for(Block bl:CarvableHelper.chiselBlocks)
				{
					if(bl instanceof Carvable &&!(bl instanceof BlockMarbleCarpet))
					{
						Carvable pl=(Carvable)bl;
						List<ItemStack> list=new LinkedList<ItemStack>();
						bl.getSubBlocks(0, null, list);
						for(ItemStack is:list)
						{
							String s=bl.getUnlocalizedName()+Integer.toString(is.getItemDamage());
//							if(bl instanceof BlockMarblePillar)
//								m.invoke(null,new ChiselPillarMicroMaterial(bl, is.getItemDamage()),s);
//							else
								m.invoke(null,new ChiselMicroMaterial(bl, is.getItemDamage()),s);
						}
					}
				}
				hasFMP=true;
			}
		};
	}
	public void postInit(FMLPostInitializationEvent event) {
		new ClassBlockCompat("shukaro.artifice.ArtificeBlocks","blockMarble"){
			@Override void action(){
				Carving.chisel.addVariation("marble",block.blockID,0,99);
				MinecraftForge.setBlockHarvestLevel(block,0,"chisel",0);
			}
		};
		new ClassBlockCompat("num.numirp.block.ModBlocks","blockDecor"){
			@Override void action(){
				Carving.chisel.addVariation("marble",block.blockID,0,99);
				MinecraftForge.setBlockHarvestLevel(block,0,"chisel",0);
			}
		};				
		new ClassBlockCompat("mrtjp.projectred.ProjectRedExploration","blockStones"){
			@Override void action(){
				Carving.chisel.addVariation("marble",block.blockID,0,99);
				MinecraftForge.setBlockHarvestLevel(block,0,"chisel",0);
			}
		};

		new ClassBlockCompat("mariculture.core.Core","oreBlocks"){
			@Override void action(){
				Carving.chisel.addVariation("limestone",block.blockID,3,99);
				MinecraftForge.setBlockHarvestLevel(block,3,"chisel",0);
			}
		};
		new ClassBlockCompat("emasher.core.EmasherCore","limestone"){
			@Override void action(){
				Carving.chisel.addVariation("limestone",block.blockID,0,99);
				MinecraftForge.setBlockHarvestLevel(block,0,"chisel",0);
			}
		};

		new ClassCompat("Reika.GeoStrata.GeoStrata"){
			@Override void action() throws Exception {
				Block[] blocks=(Block[]) cl.getField("blocks").get(null);

				Carving.chisel.addVariation("marble",blocks[0].blockID,2,99);
				MinecraftForge.setBlockHarvestLevel(blocks[0],2,"chisel",0);

				Carving.chisel.addVariation("limestone",blocks[0].blockID,3,99);
				MinecraftForge.setBlockHarvestLevel(blocks[0],3,"chisel",0);
			}
		};
		new ClassCompat("carpentersblocks.util.registry.BlockRegistry"){

			@Override
			void action() throws Exception {
				harvestRegister("blockCarpentersSlope");
				harvestRegister("blockCarpentersStairs");
				harvestRegister("blockCarpentersBarrier");
				harvestRegister("blockCarpentersGate");
				harvestRegister("blockCarpentersBlock");
				harvestRegister("blockCarpentersButton");
				harvestRegister("blockCarpentersLever");
				harvestRegister("blockCarpentersPressurePlate");
				harvestRegister("blockCarpentersDaylightSensor");
				harvestRegister("blockCarpentersHatch");
				harvestRegister("blockCarpentersDoor");
				harvestRegister("blockCarpentersBed");
				harvestRegister("blockCarpentersLadder");
				harvestRegister("blockCarpentersCollapsibleBlock");
				harvestRegister("blockCarpentersTorch");
				harvestRegister("blockCarpentersSafe");
				harvestRegister("blockCarpentersFlowerPot");

			}
			void harvestRegister(String s)  throws Exception
			{ 

				Block block = (Block) cl.getField(s).get(null);
				carpentersBlocks.add(block.blockID);
				for(int i=0;i<16;i++)
					MinecraftForge.setBlockHarvestLevel(block,i,"chisel",0);
			}
		};
		new ClassCompat("carpentersblocks.util.BlockProperties"){

			@Override
			void action() throws Exception {
				carpenterTEHack=Class.forName("carpentersblocks.tileentity.TEBase");
				carpentersHackGet=cl.getDeclaredMethod("getCoverBlock", IBlockAccess.class,int.class,int.class,int.class,int.class);
			}
		};

	}

}
