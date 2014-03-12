package info.jbcs.minecraft.chisel.items;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.modCompat.ChiselModCompatibility;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleCarpet;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.Carving;
import info.jbcs.minecraft.chisel.core.CarvingVariation;
import info.jbcs.minecraft.chisel.util.GeneralChiselClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class ItemChisel extends ItemTool {
	Random random=new Random();
	public Carving carving;
    static HashSet<Integer> chisels=new HashSet<Integer>(16);
    public static boolean isChiselItem(int id)
    {
        boolean bl=chisels.contains(id);
        return bl;
    }
	public ItemChisel(int id,Carving c) {
		super(id,1,EnumToolMaterial.IRON,CarvableHelper.chiselBlocks.toArray(new Block[CarvableHelper.chiselBlocks.size()]));

		maxStackSize = 1;
		if(Chisel.hardMode)
        {
			setMaxDamage(500);
            efficiencyOnProperMaterial=1f;
        }
		else
        {
			setMaxDamage(-1);
            efficiencyOnProperMaterial=100f;
        }


		carving=c;
		MinecraftForge.setToolClass(this,"chisel",1);
        ItemChisel.chisels.add(id+256);
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,final int x,final int y,final int z, int w, float par8, float par9, float par10) 
	{
		if (player.isSneaking())
		{
			player.openGui(Chisel.instance, 0, world, 0, 0, 0);
			return false;
		}
		else
		{

			int blockId=ChiselModCompatibility.getBlockID(world, x, y, z);
			int blockMeta=world.getBlockMetadata(x,y,z);

			if(! ForgeHooks.isToolEffective(stack, Block.blocksList[blockId], blockMeta))
				return false;

			ItemStack chiselTarget=null;

			if(stack.stackTagCompound!=null){
				chiselTarget=ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
			}

			boolean chiselHasBlockInside=true;

			if(chiselTarget==null){
				CarvingVariation[] variations=carving.getVariations(blockId, blockMeta);
				if(variations==null || variations.length<2) return false;

				int index=random.nextInt(variations.length-1);
				if(variations[index].blockId==blockId && variations[index].meta==blockMeta){
					index++;
					if(index>=variations.length) index=0;
				}
				CarvingVariation var=variations[index];
				chiselTarget=new ItemStack(var.blockId,1,var.damage);

				chiselHasBlockInside=false;
			}

			int targetId=chiselTarget.itemID;
			int targetMeta=chiselTarget.getItemDamage();

			boolean match=carving.isVariationOfSameClass(targetId,targetMeta,blockId,blockMeta);
			int resultId=targetId;


			// special case: stone can be carved to cobble and bricks 
			if(!match && blockId==Block.stone.blockID && targetId==Chisel.blockCobblestone.blockID)
				match=true;
			if(!match && blockId==Block.stone.blockID && targetId==Chisel.stoneBrick.blockID)
				match=true;

			if(!match) return false;
			//if(resultId==blockId && targetMeta == blockMeta) return false;

			if(! world.isRemote || chiselHasBlockInside) 
				ChiselModCompatibility.setBlockID(world,x, y, z, resultId, chiselTarget.getItemDamage());

			switch(FMLCommonHandler.instance().getEffectiveSide()){
			case SERVER:

				break;

			case CLIENT:

				String sound=carving.getVariationSound(resultId, chiselTarget.getItemDamage());
				GeneralChiselClient.spawnChiselEffect(x, y, z, sound);
				break;

			default:
				break;
			} 
			stack.damageItem(1, player);
			if(stack.stackSize==0){
				player.inventory.mainInventory[player.inventory.currentItem]=
						chiselHasBlockInside?
								chiselTarget:
									null;
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (entityplayer.isSneaking()) 
			entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

		return itemstack;
	}
	public boolean canHarvestBlock(Block var1)
	{
		if(var1 instanceof BlockMarbleCarpet)
			return true;
		return super.canHarvestBlock(var1);
	}
	public float getStrVsBlock(ItemStack var1, Block var2, int var3)
	{
		if(var2 instanceof BlockMarbleCarpet)
			return 100f;
		return super.getStrVsBlock(var1, var2, var3);
	}
	/*
    @Override
	public void registerIcons(IconRegister register)
    {
    }*/

	HashMap<String,Long> chiselUseTime=new HashMap<String,Long>();
	HashMap<String,String> chiselUseLocation=new HashMap<String,String>();
	/*
	@Override
	public boolean onBlockStartBreak(ItemStack stack, final int x, final int y, final int z, EntityPlayer player){
		World world=player.worldObj;
		int blockId=world.getBlockId(x, y, z);
		int blockMeta=world.getBlockMetadata(x,y,z);

		if(! ForgeHooks.isToolEffective(stack, Block.blocksList[blockId], blockMeta))
			return false;

		ItemStack chiselTarget=null;

		if(stack.stackTagCompound!=null){
			chiselTarget=ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
		}

		boolean chiselHasBlockInside=true;

		if(chiselTarget==null){
			Long useTime=chiselUseTime.get(player.username);
			String loc=chiselUseLocation.get(player.username);

			if(useTime!=null && chiselUseLocation!=null && loc.equals(x+"|"+y+"|"+z)){
				long cooldown=20;
				long time=world.getWorldInfo().getWorldTotalTime();

				if(time>useTime-cooldown && time<useTime+cooldown)
					return true;					

			}

			CarvingVariation[] variations=carving.getVariations(blockId, blockMeta);
			if(variations==null || variations.length<2) return true;

			int index=random.nextInt(variations.length-1);
			if(variations[index].blockId==blockId && variations[index].meta==blockMeta){
				index++;
				if(index>=variations.length) index=0;
			}
			CarvingVariation var=variations[index];
			chiselTarget=new ItemStack(var.blockId,1,var.damage);

			chiselHasBlockInside=false;
		}

		int targetId=chiselTarget.itemID;
		int targetMeta=chiselTarget.getItemDamage();

		boolean match=carving.isVariationOfSameClass(targetId,targetMeta,blockId,blockMeta);
		int resultId=targetId;


		// special case: stone can be carved to cobble and bricks 
		if(!match && blockId==Block.stone.blockID && targetId==Chisel.blockCobblestone.blockID)
			match=true;
		if(!match && blockId==Block.stone.blockID && targetId==Chisel.stoneBrick.blockID)
			match=true;

		if(!match) return true;
		if(resultId==blockId && targetMeta == blockMeta) return true;

		if(! world.isRemote || chiselHasBlockInside) 
			world.setBlock(x, y, z, resultId, chiselTarget.getItemDamage(), 2);
        world.markBlockForUpdate(x, y, z);
		switch(FMLCommonHandler.instance().getEffectiveSide()){
		case SERVER:
			chiselUseTime.put(player.username, world.getWorldInfo().getWorldTotalTime());
			chiselUseLocation.put(player.username,x+"|"+y+"|"+z);

			ServerConfigurationManager mgr = MinecraftServer.getServer().getConfigurationManager();
			for (int j = 0; j < mgr.playerEntityList.size(); ++j) {
				EntityPlayerMP p = (EntityPlayerMP) mgr.playerEntityList.get(j);

				if (p.dimension != player.dimension) continue;
				if (p==player && chiselHasBlockInside) continue;
				if (! General.isInRange(30.0f, p.posX, p.posY, p.posZ, x, y, z)) continue;

				Packets.chiseled.sendToPlayer(p,new PacketData(){
					@Override
					public void data(DataOutputStream stream) throws IOException {
						stream.writeInt(x);
						stream.writeInt(y);
						stream.writeInt(z);
					}
				});
			}
			break;

		case CLIENT:
			if(chiselHasBlockInside){
				String sound=carving.getVariationSound(resultId, chiselTarget.getItemDamage());
				GeneralChiselClient.spawnChiselEffect(x, y, z, sound);
			}
			break;

		default:
			break;
		} 

		stack.damageItem(1, player);
		if(stack.stackSize==0){
			player.inventory.mainInventory[player.inventory.currentItem]=
					chiselHasBlockInside?
							chiselTarget:
								null;
		}

		return true;
	}
	 */

}
