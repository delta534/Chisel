package info.jbcs.minecraft.chisel.items;

import info.jbcs.minecraft.chisel.blocks.BlockMarbleSlab;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMarbleSlab extends ItemCarvable {

	public ItemMarbleSlab(int id) {
		super(id);
	}
	
    /**
     * Returns true if the given ItemBlock can be placed on the given side of the given block position.
     */
    @Override
	public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack){
		BlockMarbleSlab block=(BlockMarbleSlab)Block.blocksList[blockId];
		
		switch(side){
		case 0: --y; break;
		case 1: ++y; break;
		case 2: --z; break;
		case 3: ++z; break;
		case 4: --x; break;
		case 5: ++x; break;
		}
		
        int id = world.getBlockId(x, y, z);
    	int meta=world.getBlockMetadata(x, y, z);

		if((id==blockId || id==block.top.blockID) && meta==stack.getItemDamage())
			return true;

        return world.canPlaceEntityOnSide(getBlockID(), x, y, z, false, side, (Entity)null, stack);
    }

	
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz){
		BlockMarbleSlab block=(BlockMarbleSlab)Block.blocksList[blockId];
   	
    	int id=world.getBlockId(x, y, z);
    	int meta=world.getBlockMetadata(x, y, z);
    	boolean metaMatches=meta==stack.getItemDamage();

    	if(metaMatches && side==0 && id==block.top.blockID){
    		world.setBlock(x, y, z, block.master.blockID, meta, 2);
    		return true;
    	} else if(metaMatches && side==1 && id==block.bottom.blockID){
    		world.setBlock(x, y, z, block.master.blockID, meta, 2);
    		return true;
    	}
    	
		boolean result=super.onItemUse(stack,player,world,x,y,z,side,hz,hy,hz);
		
		
		switch(side){
		case 0: --y; break;
		case 1: ++y; break;
		case 2: --z; break;
		case 3: ++z; break;
		case 4: --x; break;
		case 5: ++x; break;
		}
		
    	id=world.getBlockId(x, y, z);
    	meta=world.getBlockMetadata(x, y, z);

    	if(!result && (id==block.top.blockID || id==block.bottom.blockID) && meta==stack.getItemDamage()){
    		world.setBlock(x, y, z, block.master.blockID, meta, 2);
    		return true;
    	}
    	
    	if(!result)
    		return false;
    	
		if(side != 0 && (side == 1 || hy <= 0.5D))
			return true;
		
		world.setBlock(x, y, z, block.top.blockID, meta, 2);
    	return true;
    }
}
