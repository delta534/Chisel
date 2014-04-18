package info.jbcs.minecraft.utilities;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;

public abstract class Sounds {
	SoundLoadEvent event;

    @SubscribeEvent
    public void onSound(SoundLoadEvent evt){
		event=evt;

		try {
			addSounds();
		} catch (Exception e) {
			System.err.println("Failed to register one or more sounds: "+e);
		}
	}

    public abstract void addSounds();

    protected void addSound(String path){

    }
}
