package info.jbcs.minecraft.chisel.util;

import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class proxyWorld implements IBlockAccess {
    IBlockAccess world;
    Vector3 pos;
    Block id;
    int meta;

    public proxyWorld(IBlockAccess w, Vector3 v, Block i, int m) {
        world = w;
        pos = v;
        id = i;
        meta = m;
    }

    @Override
    public Block getBlock(int i, int j, int k) {
        if (((int) pos.x == i) && ((int) pos.y == j) && ((int) pos.z == k))
            return id;
        return world.getBlock(i, j, k);
    }

    @Override
    public TileEntity getTileEntity(int i, int j, int k) {
        return world.getTileEntity(i, j, k);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
        return world.getLightBrightnessForSkyBlocks(i, j, k, l);
    }

    @Override
    public int getBlockMetadata(int i, int j, int k) {
        if (((int) pos.x == i) && ((int) pos.y == j) && ((int) pos.z == k))
            return meta;
        return world.getBlockMetadata(i, j, k);
    }





    @Override
    public boolean isAirBlock(int i, int j, int k) {
        return world.isAirBlock(i, j, k);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int i, int j) {
        return world.getBiomeGenForCoords(i, j);
    }

    @Override
    public int getHeight() {
        return world.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return world.extendedLevelsInChunkCache();
    }

    @Override
    public Vec3Pool getWorldVec3Pool() {
        return world.getWorldVec3Pool();
    }

    @Override
    public int isBlockProvidingPowerTo(int i, int j, int k, int l) {
        return world.isBlockProvidingPowerTo(i, j, k, l);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return world.isSideSolid(x, y, z, side, _default);
    }
}
