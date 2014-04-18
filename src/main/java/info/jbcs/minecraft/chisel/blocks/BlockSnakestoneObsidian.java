package info.jbcs.minecraft.chisel.blocks;


import info.jbcs.minecraft.chisel.util.GeneralChiselClient;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSnakestoneObsidian extends BlockSnakestone {
    public IIcon[] particles = new IIcon[8];

    public BlockSnakestoneObsidian(int id, String iconPrefix) {
        super(id, iconPrefix);

        flipTopTextures = true;

    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        GeneralChiselClient.spawnSnakestoneObsidianFX(world, this, x, y, z);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);

        for (int i = 0; i < particles.length; i++) {
            particles[i] = register.registerIcon(iconPrefix + "particles/" + i);
        }
    }

}
