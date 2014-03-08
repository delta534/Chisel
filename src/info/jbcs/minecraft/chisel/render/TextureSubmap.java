package info.jbcs.minecraft.chisel.render;

import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

public class TextureSubmap {
	public int width,height;
	public Icon icon;
	public Icon icons[];

	public TextureSubmap(Icon i,int w,int h) {
		icon=i;
		width=w;
		height=h;
		icons=new Icon[width*height];
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe
	public void TexturesStitched(TextureStitchEvent.Post event){
		
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				icons[y*width+x]=new TextureVirtual(icon,width,height,x,y);
			}
		}
	}

}
