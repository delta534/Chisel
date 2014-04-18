package info.jbcs.minecraft.chisel.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.blocks.BlockMarblePillar;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.render.Util.RotationData;
import info.jbcs.minecraft.chisel.util.ConnectionCheckManager;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockMarblePillarRenderer implements ISimpleBlockRenderingHandler {
    public Util.RotationData rot = new RotationData();
    public static int id;

    public BlockMarblePillarRenderer() {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block blck, int metadata, int modelID,
                                     RenderBlocks renderer) {
        if (!(blck instanceof BlockMarblePillar))
            return;

        BlockMarblePillar block = (BlockMarblePillar) blck;

        block.sides[0] = block.sides[1] = block.getCtmIcon(4, metadata);
        block.sides[2] = block.sides[3] = block.sides[4] = block.sides[5] = block
                .getCtmIcon(0, metadata);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Drawing.drawBlock(block, metadata, renderer);
    }

    static boolean connected(IBlockAccess world, int x, int y, int z, int id,
                             int meta) {
        return ConnectionCheckManager.checkConnection(world, x, y, z, id, meta);
    }

    public static boolean setupRotation(BlockMarblePillar block, int id, int metadata, RotationData rotations, int x, int y, int z, IBlockAccess world) {

        boolean flag = rotations == null || block.carverHelper.variations.get(metadata).kind != CarvableHelper.CTMV
                || !(block.carverHelper.variations.get(metadata).useCTM);
        if (!flag) {
            boolean yp = connected(world, x, y + 1, z, id, metadata);
            boolean yn = connected(world, x, y - 1, z, id, metadata);
            if (!(yp || yn)) {
                boolean xp = connected(world, x + 1, y, z, id, metadata);
                boolean xn = connected(world, x - 1, y, z, id, metadata);

                if (xp || xn) {
                    rotations.rotateZNeg = 2;
                    rotations.rotateZPos = 1;
                    rotations.rotateYNeg = 1;
                    rotations.rotateYPos = 1;
                } else {
                    boolean zp = connected(world, x, y, z + 1, id, metadata);
                    boolean zn = connected(world, x, y, z - 1, id, metadata);

                    if (zp
                            && (connected(world, x, y + 1, z + 1, id, metadata) || connected(
                            world, x, y - 1, z + 1, id, metadata)))
                        zp = false;
                    if (zp
                            && (connected(world, x + 1, y, z + 1, id, metadata) || connected(
                            world, x - 1, y, z + 1, id, metadata)))
                        zp = false;
                    if (zn
                            && (connected(world, x, y + 1, z - 1, id, metadata) || connected(
                            world, x, y - 1, z - 1, id, metadata)))
                        zn = false;
                    if (zn
                            && (connected(world, x + 1, y, z - 1, id, metadata) || connected(
                            world, x - 1, y, z - 1, id, metadata)))
                        zn = false;

                    if (zp || zn) {
                        rotations.rotateXPos = 1;
                        rotations.rotateXNeg = 2;
                    }

                }

            }
        }
        return flag;
    }

    public static boolean setupIcons(IIcon[] Icons, BlockMarblePillar block,
                                     int id, int metadata, IBlockAccess world, int x, int y, int z
    ) {


        boolean flag = block.carverHelper.variations.get(metadata).kind != CarvableHelper.CTMV
                || !(block.carverHelper.variations.get(metadata).useCTM);
        if (flag) {
            for (int i = 0; i < 6; i++)
                Icons[i] = block.carverHelper.getVariation(metadata).getIcon(i);
        } else {


            boolean yp = connected(world, x, y + 1, z, id, metadata);
            boolean yn = connected(world, x, y - 1, z, id, metadata);

            if (yp || yn) {
                Icons[0] = block.getCtmIcon(4, metadata);
                Icons[1] = block.getCtmIcon(4, metadata);

                if (yp && yn)
                    Icons[2] = block.getCtmIcon(2, metadata);
                else if (yp)
                    Icons[2] = block.getCtmIcon(3, metadata);
                else
                    Icons[2] = block.getCtmIcon(1, metadata);

                Icons[3] = Icons[4] = Icons[5] = Icons[2];
            } else {
                boolean xp = connected(world, x + 1, y, z, id, metadata);
                boolean xn = connected(world, x - 1, y, z, id, metadata);

                if (xp
                        && (connected(world, x + 1, y + 1, z, id, metadata) || connected(
                        world, x + 1, y - 1, z, id, metadata)))
                    xp = false;
                if (xn
                        && (connected(world, x - 1, y + 1, z, id, metadata) || connected(
                        world, x - 1, y - 1, z, id, metadata)))
                    xn = false;

                if (xp || xn) {


                    Icons[4] = block.getCtmIcon(4, metadata);
                    Icons[5] = block.getCtmIcon(4, metadata);

                    if (xp && xn)
                        Icons[0] = block.getCtmIcon(2, metadata);
                    else if (xp)
                        Icons[0] = block.getCtmIcon(3, metadata);
                    else
                        Icons[0] = block.getCtmIcon(1, metadata);

                    Icons[1] = Icons[2] = Icons[3] = Icons[0];
                } else {
                    boolean zp = connected(world, x, y, z + 1, id, metadata);
                    boolean zn = connected(world, x, y, z - 1, id, metadata);

                    if (zp
                            && (connected(world, x, y + 1, z + 1, id, metadata) || connected(
                            world, x, y - 1, z + 1, id, metadata)))
                        zp = false;
                    if (zp
                            && (connected(world, x + 1, y, z + 1, id, metadata) || connected(
                            world, x - 1, y, z + 1, id, metadata)))
                        zp = false;
                    if (zn
                            && (connected(world, x, y + 1, z - 1, id, metadata) || connected(
                            world, x, y - 1, z - 1, id, metadata)))
                        zn = false;
                    if (zn
                            && (connected(world, x + 1, y, z - 1, id, metadata) || connected(
                            world, x - 1, y, z - 1, id, metadata)))
                        zn = false;

                    if (zp || zn) {
                        Icons[2] = block.getCtmIcon(4, metadata);
                        Icons[3] = block.getCtmIcon(4, metadata);

                        if (zp && zn)
                            Icons[0] = block.getCtmIcon(2, metadata);
                        else if (zp)
                            Icons[0] = block.getCtmIcon(1, metadata);
                        else
                            Icons[0] = block.getCtmIcon(3, metadata);

                        Icons[1] = Icons[4] = Icons[5] = Icons[0];
                    } else {
                        Icons[0] = Icons[1] = block.getCtmIcon(4, metadata);
                        Icons[2] = Icons[3] = Icons[4] = Icons[5] = block
                                .getCtmIcon(0, metadata);
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                                    Block blck, int modelId, RenderBlocks renderer) {
        if (!(blck instanceof BlockMarblePillar))
            return false;

        BlockMarblePillar block = (BlockMarblePillar) blck;

        int metadata = world.getBlockMetadata(x, y, z);
        int id = 0;
        rot.clear();
        setupRotation(block, id, metadata, rot, x, y, z, world);
        if (setupIcons(block.sides, block, id, metadata, world, x, y, z)) {

            return renderer.renderStandardBlock(block, x, y, z);

        } else {
            renderer.uvRotateSouth = rot.rotateXPos;
            renderer.uvRotateEast = rot.rotateZNeg;
            renderer.uvRotateWest = rot.rotateZPos;
            renderer.uvRotateNorth = rot.rotateXNeg;
            renderer.uvRotateTop = rot.rotateYPos;
            renderer.uvRotateBottom = rot.rotateYNeg;
            boolean flag = renderer.renderStandardBlock(block, x, y, z);

            renderer.uvRotateSouth = 0;
            renderer.uvRotateEast = 0;
            renderer.uvRotateWest = 0;
            renderer.uvRotateNorth = 0;
            renderer.uvRotateTop = 0;
            renderer.uvRotateBottom = 0;

            return flag;
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }

}
