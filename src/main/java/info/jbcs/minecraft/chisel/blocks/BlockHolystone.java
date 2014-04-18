package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.util.GeneralChiselClient;
import info.jbcs.minecraft.utilities.General;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockHolystone extends BlockMarble {
    public IIcon iconStar;

    public BlockHolystone(String name, int i, Material m) {
        super(name, i, m);

        setLightValue(0.25F);

    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (General.rand.nextInt(4) == 0)
            GeneralChiselClient.spawnHolystoneFX(world, this, x, y, z);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);

        iconStar = register.registerIcon("Chisel:holystone/particles/star");
    }

}
