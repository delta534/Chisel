package info.jbcs.minecraft.chisel.blocks;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import net.minecraft.block.BlockIce;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockMarbleIce extends BlockIce implements Carvable {
    public CarvableHelper carverHelper;

    public BlockMarbleIce(int i) {
        super(i);

        carverHelper = new CarvableHelper();

        setCreativeTab(Chisel.tabChisel);
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
    }

    @Override
    public int damageDropped(int i) {
        return 0;
    }

    @Override
    public void registerIcons(IconRegister register) {
        carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(int blockId, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list, Chisel.disableOverriding);
    }

    /**
     * Called when  the player destroys a block with an item that can harvest it.
     * (i, j, k) are the coordinates of the block and l is the block's
     * subtype/damage.
     */
    @Override
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        if (!Chisel.dropIceShards) {
            super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
            return;
        }

        par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        par2EntityPlayer.addExhaustion(0.025F);

        if (par1World.isRemote)
            return;

        if (this.canSilkHarvest(par1World, par2EntityPlayer, par3, par4, par5, par6) && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer)) {
            ItemStack itemstack = this.createStackedBlock(par6);

            if (itemstack != null) {
                this.dropBlockAsItem_do(par1World, par3, par4, par5, itemstack);
            }
        } else {
            int i1 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
            this.dropBlockAsItem(par1World, par3, par4, par5, par6, i1);
        }
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return Chisel.dropIceShards ? Chisel.itemIceshard.itemID : 0;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return Chisel.dropIceShards ? 1 + par1Random.nextInt(5) : 0;
    }

    @Override
    public RenderVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
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
