package info.jbcs.minecraft.chisel.modCompat;

import info.jbcs.minecraft.chisel.blocks.BlockMarblePillar;
import info.jbcs.minecraft.chisel.render.BlockMarblePillarRenderer;
import info.jbcs.minecraft.chisel.render.Util;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import codechicken.lib.render.IUVTransformation;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.microblock.IMicroMaterialRender;

public class BlockPillarUVTransform extends DelayedIcons {

    Icon []icons;
    int side;
    byte rotation;
    static final double uc = 0.5;
    static final double vc = -0.5;
    static Util.RotationData dummy = new Util.RotationData();

    public BlockPillarUVTransform(Block bl, int m) {
        super(bl, m);
        icons = new Icon[6];
        side = 0;
        rotation = 0;
    }


    public void bindSide(int inSide) {
        side = inSide;
    }

    public void update() {
        dummy.clear();
        if (part != null && part.world() != null) {
            BlockMarblePillarRenderer.setupRotation((BlockMarblePillar) block,
                    block.blockID, metadata, dummy, part.x(), part.y(),
                    part.z(), part.world());
            BlockMarblePillarRenderer.setupIcons(icons,
                    (BlockMarblePillar) block, block.blockID, metadata,
                    part.world(), part.x(), part.y(), part.z());
            switch (side % 6) {
            case 0:
                rotation = (byte) dummy.rotateYNeg;// good
                break;
            case 1:
                rotation = (byte) dummy.rotateYPos;// good
                break;
            case 2:
                rotation = (byte) dummy.rotateZNeg;// good
                break;
            case 3:
                rotation = (byte) dummy.rotateZPos;// good
                break;
            case 4:
                rotation = (byte) dummy.rotateXNeg;// good
                break;
            case 5:
                rotation = (byte) dummy.rotateXPos;// good
                break;

            }
        }

    }

    @Override
    public void transform(UV texcoord) {
        Icon icon;
        if (part != null & part.world() != null) {
            Util.rotateUV(texcoord, side, rotation);
            icon = icons[side % 6];

        } else {
            int i = (int) texcoord.u >> 1;
            icon = block.getIcon(i % 6, metadata);

        }
        texcoord.u = icon.getInterpolatedU(texcoord.u % 2 * 16);
        texcoord.v = icon.getInterpolatedV(texcoord.v % 2 * 16);
    }

}
