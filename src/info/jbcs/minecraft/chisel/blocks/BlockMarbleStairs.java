package info.jbcs.minecraft.chisel.blocks;


import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.BlockMarbleStairsRenderer;
import info.jbcs.minecraft.chisel.util.IMetaDataName;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class BlockMarbleStairs extends BlockStairs implements Carvable, IMetaDataName {
    CarvableHelper carverHelper;
    int blockMeta;
    int index;

    public BlockMarbleStairs(String name, int i, Block block, int meta, CarvableHelper helper, int ind) {
        super(name == null ? i : Chisel.config.getBlock(name, i).getInt(i), block, meta);

        useNeighborBrightness[blockID] = true;
        setCreativeTab(Chisel.tabChisel);
        carverHelper = helper;
        blockMeta = meta;
        index = ind;
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(blockMeta + metadata / 8).getIcon(side);
    }

    @Override
    public int damageDropped(int i) {
        return i & 0x8;
    }

    @Override
    public void registerIcons(IconRegister register) {
        if (blockMeta == 0)
            carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(int blockId, CreativeTabs tabs, List list) {
        list.add(new ItemStack(blockID, 1, 0));
        list.add(new ItemStack(blockID, 1, 8));
    }

    @Override
    public int getRenderType() {
        return BlockMarbleStairsRenderer.id;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
        int l = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = par1World.getBlockMetadata(par2, par3, par4) & 4;
        int odd = par6ItemStack.getItemDamage();

        if (l == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | i1 + odd, 2);
        }

        if (l == 1) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | i1 + odd, 2);
        }

        if (l == 2) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | i1 + odd, 2);
        }

        if (l == 3) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | i1 + odd, 2);
        }
    }


    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int damage) {
        // int res=super.onBlockPlaced();
        return side != 0 && (side == 1 || hy <= 0.5D) ? damage : damage | 4;
    }

    @Override
    public RenderVariation getVariation(int metadata) {
        return carverHelper.getVariation(index + metadata);
    }

    @Override
    public String getUnlocalizedName(int metadata) {
        return this.getUnlocalizedName();
    }

    @Override
    public CarvableHelper getHelper() {
        return carverHelper;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        BlockGlassCarvable.pass=pass;
        return true;
    }
    @Override
    public int getRenderBlockPass()
    {
        return 0;
    }

}
