package info.jbcs.minecraft.chisel.proxy;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.render.BlockAdvancedMarbleRenderer;
import info.jbcs.minecraft.chisel.render.BlockCarpetRenderer;
import info.jbcs.minecraft.chisel.render.BlockEldritchRenderer;
import info.jbcs.minecraft.chisel.render.BlockMarblePaneRenderer;
import info.jbcs.minecraft.chisel.render.BlockMarblePillarRenderer;
import info.jbcs.minecraft.chisel.render.BlockMarbleStairsRenderer;
import info.jbcs.minecraft.chisel.render.BlockNoCTMRenderer;
import info.jbcs.minecraft.chisel.render.BlockRoadLineRenderer;
import info.jbcs.minecraft.chisel.render.BlockSnakeStoneRenderer;
import info.jbcs.minecraft.chisel.render.BlockSpikesRenderer;
import info.jbcs.minecraft.chisel.render.ItemChiselRenderer;
import info.jbcs.minecraft.utilities.BlockTexturedOreRenderer;
import info.jbcs.minecraft.utilities.Sounds;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends Proxy {
	ItemChiselRenderer renderer = new ItemChiselRenderer();

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new Sounds() {
			@Override
			public void addSounds() {
				addSound("chisel:chisel.ogg");

				addSound("chisel:chisel-wood2.ogg");
				addSound("chisel:chisel-wood3.ogg");
				addSound("chisel:chisel-wood4.ogg");
				addSound("chisel:chisel-wood5.ogg");
				addSound("chisel:chisel-wood8.ogg");
				addSound("chisel:chisel-wood9.ogg");
				addSound("chisel:chisel-wood11.ogg");

				addSound("chisel:holystone1.ogg");
				addSound("chisel:holystone2.ogg");
				addSound("chisel:holystone3.ogg");
				addSound("chisel:holystone5.ogg");
				addSound("chisel:holystone7.ogg");

				addSound("chisel:squash.ogg");
				addSound("chisel:squash2.ogg");
				
				addSound("chisel:temple-footstep1.ogg");
				addSound("chisel:temple-footstep2.ogg");
				addSound("chisel:temple-footstep3.ogg");
				addSound("chisel:temple-footstep4.ogg");
				addSound("chisel:temple-footstep5.ogg");
				
				addSound("chisel:metal1.ogg");
				addSound("chisel:metal2.ogg");
				addSound("chisel:metal3.ogg");
				addSound("chisel:metal4.ogg");
				addSound("chisel:metal5.ogg");
				addSound("chisel:metal6.ogg");
				addSound("chisel:metal7.ogg");
				addSound("chisel:metal8.ogg");
				addSound("chisel:metal9.ogg");
			}
		});
	}


	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new BlockMarbleStairsRenderer());
		RenderingRegistry.registerBlockHandler(new BlockMarblePaneRenderer());
		RenderingRegistry.registerBlockHandler(new BlockRoadLineRenderer());
		RenderingRegistry.registerBlockHandler(new BlockSnakeStoneRenderer());
		RenderingRegistry.registerBlockHandler(new BlockNoCTMRenderer());
		RenderingRegistry.registerBlockHandler(new BlockSpikesRenderer());
		RenderingRegistry.registerBlockHandler(new BlockMarblePillarRenderer());
		RenderingRegistry.registerBlockHandler(new BlockEldritchRenderer());
		RenderingRegistry.registerBlockHandler(new BlockAdvancedMarbleRenderer());
		RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());
		
		RenderingRegistry.registerBlockHandler(new BlockTexturedOreRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(Chisel.itemCloudInABottle));
		RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(Chisel.itemBallOMoss));
		
		
		MinecraftForgeClient.registerItemRenderer(Chisel.chisel.itemID, renderer);
//		MinecraftForgeClient.registerItemRenderer(Chisel.needle.itemID, renderer);

	}
}
