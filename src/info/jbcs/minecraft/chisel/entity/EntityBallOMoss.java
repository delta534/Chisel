package info.jbcs.minecraft.chisel.entity;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.util.GeneralChiselClient;
import info.jbcs.minecraft.utilities.General;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBallOMoss extends EntityThrowable {

    public EntityBallOMoss(World par1World) {
        super(par1World);
    }

    public EntityBallOMoss(World par1World, EntityLivingBase par2EntityLiving) {
        super(par1World, par2EntityLiving);
    }

    public EntityBallOMoss(World par1World, double x, double y, double z) {
        super(par1World, x, y, z);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        int x = movingobjectposition.blockX;
        int y = movingobjectposition.blockY;
        int z = movingobjectposition.blockZ;

        switch (movingobjectposition.sideHit) {
            case 0:
                y--;
                break;
            case 1:
                y++;
                break;
            case 2:
                z--;
                break;
            case 3:
                z++;
                break;
            case 4:
                x--;
                break;
            case 5:
                x++;
                break;
        }

        setDead();

        if (worldObj.isRemote) {
            worldObj.playSound(x, y, z, "chisel:squash", 1.0f, 1.0f, false);

            for (int i = 0; i < 32; i++)
                GeneralChiselClient.spawnBallOMossFX(worldObj, posX, posY, posZ);

            return;
        }

        int radius = 5;
        int falloff = 3;

        for (int xx = -radius; xx < radius; xx++) {
            for (int yy = -radius; yy < radius; yy++) {
                for (int zz = -radius; zz < radius; zz++) {
                    double dist = (xx < 0 ? -xx : xx) + (yy < 0 ? -yy : yy) + (zz < 0 ? -zz : zz);

                    if (!(dist < falloff || General.rand.nextInt(radius * 3 - falloff) >= dist * 2)) continue;

                    if (!worldObj.isRemote)
                        turnToMoss(worldObj, x + xx, y + yy, z + zz);
                }
            }
        }

    }


    public static void turnToMoss(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        int resId = id;
        int resMeta = meta;

        if (id == Block.cobblestone.blockID) {
            resId = Block.cobblestoneMossy.blockID;
        } else if (id == Block.cobblestoneWall.blockID && meta == 0) {
            resMeta = 1;
        } else if (id == Chisel.blockCobblestone.blockID) {
            resId = Chisel.blockCobblestoneMossy.blockID;
        } else if (id == Chisel.blockTemple.blockID) {
            resId = Chisel.blockTempleMossy.blockID;
        }

        if (resId == id && resMeta == meta) return;
        world.setBlock(x, y, z, resId, resMeta, 3);
    }

}
