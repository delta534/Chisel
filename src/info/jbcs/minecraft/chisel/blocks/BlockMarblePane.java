package info.jbcs.minecraft.chisel.blocks;


import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.CarvableHelper;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.render.BlockMarblePaneRenderer;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

import java.util.List;

public class BlockMarblePane extends BlockPane implements Carvable {
    public CarvableHelper carverHelper;

    public BlockMarblePane(int id, Material material, boolean drops) {
        super(id, "", "", material, drops);

        carverHelper = new CarvableHelper();

        setCreativeTab(Chisel.tabChisel);
    }


    @Override
    public int getRenderType() {
        return BlockMarblePaneRenderer.id;
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        return carverHelper.getVariation(metadata).getIcon(side);
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
    public RenderVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }

    @Override
    public CarvableHelper getHelper() {
        return carverHelper;
    }
}
