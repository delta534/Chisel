package info.jbcs.minecraft.chisel.blocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.render.BlockSpikesRenderer;
import info.jbcs.minecraft.chisel.util.GeneralChiselClient;
import info.jbcs.minecraft.utilities.General;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSpikes extends Block {
    public Icon iconBase;
    public Icon iconSpike;

    public BlockSpikes(int id, Material mat) {
        super(id, Material.circuits);

        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f / 16, 1.0f);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return;

//		int damage = (int) (entity.motionY*10);
//		if(damage!=0)
//			System.out.println(damage);

        double dy = entity.posY - entity.prevPosY;
        if (dy != 0)
            System.out.println(dy);

//		System.out.println(entity.speedInAir);


        GeneralChiselClient.speedupPlayer(world, entity, Chisel.concreteVelocity);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }


    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public int getRenderType() {
        return BlockSpikesRenderer.id;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    public void registerIcons(IconRegister register) {
        blockIcon = iconBase = register.registerIcon(General.getName(this) + "base");
        iconSpike = register.registerIcon(General.getName(this) + "spike");
    }
}
