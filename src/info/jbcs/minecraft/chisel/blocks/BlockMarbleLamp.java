package info.jbcs.minecraft.chisel.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockMarbleLamp extends BlockMarble {
	BlockMarbleLamp blockUnpowered;
	BlockMarbleLamp blockPowered;
	boolean powered;

	public BlockMarbleLamp(String name,int id) {
		super(name+".unpowered", id, Material.redstoneLight);
		
		powered=false;
		blockUnpowered=this;
		blockPowered=new BlockMarbleLamp(this,name,id+1);
		
        setHardness(0.3F);
	}

	public BlockMarbleLamp(BlockMarbleLamp unpoweredVersion, String name,int id) {
		super(name+".powered", id, Material.redstoneLight);
		
		carverHelper=unpoweredVersion.carverHelper;
		
		powered=true;
		blockUnpowered=unpoweredVersion;
		blockPowered=this;
		
        setLightValue(1.0F);
        setHardness(0.3F);
	}

	
	public void checkPower(World world, int x, int y, int z){
        if (world.isRemote) return;
        
        boolean isGettingPower=world.isBlockIndirectlyGettingPowered(x, y, z);
        int meta=world.getBlockMetadata(x, y, z);
         
		if (powered && !isGettingPower) {
			world.setBlock(x, y, z, blockUnpowered.blockID, meta, 2);
		} else if (!powered && isGettingPower) {
			world.setBlock(x, y, z, blockPowered.blockID, meta, 2);
		}
	}

    @Override
	public void onBlockAdded(World world, int x, int y, int z){
    	checkPower(world,x,y,z);
    }

    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id){
    	checkPower(world,x,y,z);
    }

/*
    @Override
	public void updateTick(World world, int x, int y, int z, Random rand){
        if (world.isRemote) return;

        if(powered && !world.isBlockIndirectlyGettingPowered(x, y, z)){
            int meta=world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, Block.redstoneLampIdle.blockID, meta, 2);
        }
    }*/

    @Override
	public int idDropped(int par1, Random par2Random, int par3){
        return blockUnpowered.blockID;
    }
	
    @Override
	public int idPicked(World par1World, int par2, int par3, int par4){
        return blockUnpowered.blockID;
    }

}
