package info.jbcs.minecraft.chisel.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockProxy extends Block {
    CarvableHelper carverHelper;
    Block parent;

    public BlockProxy(Block parent, int i) {
        this(parent, null, i, Material.rock);
    }

    public BlockProxy(Block parent, String name, int i) {
        this(parent, name, i, Material.rock);
    }

    public BlockProxy(Block parent, int i, Material m) {
        this(parent, null, i, Material.rock);
    }

    public BlockProxy(Block p, String name, int i, Material m) {
        super(name == null ? i : Chisel.config.getBlock(name, i).getInt(i), m);

        carverHelper = new CarvableHelper();

        setCreativeTab(Chisel.tabChisel);
        parent = p;

        setHardness(parent.blockHardness);
        setResistance(parent.blockResistance);
        setStepSound(parent.stepSound);
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        return carverHelper.getVariation(meta).getBlockTexture(world, x, y, z, side);
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @Override
    public void registerIcons(IconRegister register) {
        carverHelper.registerIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(int blockId, CreativeTabs tabs, List list) {
        carverHelper.registerSubBlocks(this, tabs, list);
    }


    @Override
    public boolean renderAsNormalBlock() {
        return parent.renderAsNormalBlock();
    }

    @Override
    public int getRenderType() {
        return parent.getRenderType();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return parent.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return parent.isBlockSolid(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        parent.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return parent.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return parent.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @Override
    public boolean isOpaqueCube() {
        return parent.isOpaqueCube();
    }

    @Override
    public boolean isCollidable() {
        return parent.isCollidable();
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        parent.updateTick(par1World, par2, par3, par4, par5Random);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        parent.randomDisplayTick(par1World, par2, par3, par4, par5Random);
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        parent.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
        parent.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }

    @Override
    public int tickRate(World par1World) {
        return parent.tickRate(par1World);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        parent.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {

        return parent.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    @Override
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
        parent.onBlockDestroyedByExplosion(par1World, par2, par3, par4, par5Explosion);
    }

    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5, ItemStack par6ItemStack) {
        return parent.canPlaceBlockOnSide(par1World, par2, par3, par4, par5, par6ItemStack);
    }

    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
        return parent.canPlaceBlockOnSide(par1World, par2, par3, par4, par5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return parent.getRenderBlockPass();
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {

        return parent.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return parent.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
    }

    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        parent.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
        return parent.onBlockPlaced(par1World, par2, par3, par4, par5, par6, par7, par8, par9);
    }

    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
        parent.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        parent.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return parent.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return parent.isProvidingStrongPower(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canProvidePower() {
        return parent.canProvidePower();
    }

    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        parent.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
        return parent.canBlockStay(par1World, par2, par3, par4);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
        parent.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);
    }

    @Override
    public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5) {
        parent.onPostBlockPlaced(par1World, par2, par3, par4, par5);
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        return parent.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public int getMobilityFlag() {
        return parent.getMobilityFlag();
    }

    @Override
    public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6) {
        parent.onFallenUpon(par1World, par2, par3, par4, par5Entity, par6);
    }

    @Override
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return parent.idPicked(par1World, par2, par3, par4);
    }

    @Override
    public void fillWithRain(World par1World, int par2, int par3, int par4) {
        parent.fillWithRain(par1World, par2, par3, par4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFlowerPot() {
        return parent.isFlowerPot();
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return parent.hasComparatorInputOverride();
    }

    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
        return parent.getComparatorInputOverride(par1World, par2, par3, par4, par5);
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        return parent.isBlockSolidOnSide(world, x, y, z, side);
    }

    @Override
    public boolean isBlockBurning(World world, int x, int y, int z) {
        return parent.isBlockBurning(world, x, y, z);
    }

    @Override
    public boolean isAirBlock(World world, int x, int y, int z) {
        return parent.isAirBlock(world, x, y, z);
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return parent.canHarvestBlock(player, meta);
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        return parent.getFlammability(world, x, y, z, metadata, face);
    }

    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        return parent.getFireSpreadSpeed(world, x, y, z, metadata, face);
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side) {
        return parent.isFireSource(world, x, y, z, metadata, side);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return parent.hasTileEntity(metadata);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return parent.createTileEntity(world, metadata);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        return parent.canCreatureSpawn(type, world, x, y, z);
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z) {
        parent.beginLeavesDecay(world, x, y, z);
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z) {
        return parent.canSustainLeaves(world, x, y, z);
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z) {
        return parent.isLeaves(world, x, y, z);
    }

    @Override
    public boolean isWood(World world, int x, int y, int z) {
        return parent.isWood(world, x, y, z);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        parent.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
        return parent.canPlaceTorchOnTop(world, x, y, z);
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return parent.canRenderInPass(pass);
    }

    @Override
    public boolean isFertile(World world, int x, int y, int z) {
        return parent.isFertile(world, x, y, z);
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        return parent.rotateBlock(worldObj, x, y, z, axis);
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) {
        return parent.getValidRotations(worldObj, x, y, z);
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        return parent.getEnchantPowerBonus(world, x, y, z);
    }

}
