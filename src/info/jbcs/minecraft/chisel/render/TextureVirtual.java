package info.jbcs.minecraft.chisel.render;

import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TextureVirtual implements Icon {
    private final int ox;
    private final int oy;
    private final float u0;
    private final float u1;
    private final float v0;
    private final float v1;
    private final String name;

    TextureVirtual(Icon parent, int w, int h, int x, int y){
        Icon icon = parent;

        u0= icon.getInterpolatedU(16.0 * (x) / w);
        u1= icon.getInterpolatedU(16.0 * (x + 1) / w);
        v0= icon.getInterpolatedV(16.0 * (y) / h);
        v1= icon.getInterpolatedV(16.0 * (y + 1) / h);

        name= icon.getIconName()+"|"+x+"."+y;

        ox= icon.getIconWidth();
        oy= icon.getIconHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinU() {
        return u0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxU() {
        return u1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedU(double d0) {
        return (float) (u0+(u1-u0)*d0/16.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinV() {
        return v0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxV() {
        return v1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedV(double d0) {
        return (float) (v0+(v1-v0)*d0/16.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getIconName() {
        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return ox;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return oy;
    }

}
