package info.jbcs.minecraft.chisel.util;

import net.minecraft.block.Block.SoundType;

public class StepSoundEx extends SoundType {
    String stepName;
    String placeName;

    public StepSoundEx(String name, String stepName, String placeName, float volume) {
        super(name, volume, 1.0f);

        this.stepName = stepName;
        this.placeName = placeName;
    }



}
