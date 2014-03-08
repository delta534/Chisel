package info.jbcs.minecraft.chisel.blocks;




import info.jbcs.minecraft.chisel.util.GeneralChiselClient;

import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSnakestoneObsidian extends BlockSnakestone {
	public Icon[] particles=new Icon[8];
	
	public BlockSnakestoneObsidian(int id, String iconPrefix) {
		super(id, iconPrefix);
		
		flipTopTextures=true;

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		GeneralChiselClient.spawnSnakestoneObsidianFX(world,this,x,y,z);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		
		for(int i=0;i<particles.length;i++){
			particles[i]=register.registerIcon(iconPrefix + "particles/"+i);
		}
	}

}
