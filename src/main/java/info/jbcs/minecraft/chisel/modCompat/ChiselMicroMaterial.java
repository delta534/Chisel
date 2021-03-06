package info.jbcs.minecraft.chisel.modCompat;


import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial;
import codechicken.microblock.Microblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.IPartMeta;
import cpw.mods.fml.common.registry.LanguageRegistry;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMX;
import info.jbcs.minecraft.chisel.util.IConnectionCheck;
import info.jbcs.minecraft.chisel.util.proxyWorld;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ChiselMicroMaterial extends BlockMicroMaterial implements IPartMeta {


    protected DelayedIcons icontr;

    public ChiselMicroMaterial(Block arg0, int arg1) {
        super(arg0, arg1);

    }

    @Override
    public void loadIcons() {
        icontr = new DelayedIcons(block(), meta());

    }

    private final VariationCTMX test = new VariationCTMX();

    @Override
    public void renderMicroFace(Vertex5[] verts, int side, Vector3 pos,
                                LightMatrix lightMatrix, IMicroMaterialRender part) {
        icontr.bindPart(part);
        RenderVariation var = ((Carvable) block()).getVariation(meta());
        // test.submap=var.submap;
        //test.submapSmall=var.submapSmall;
        proxyWorld world = null;
        if (part.world() != null)
            world = new proxyWorld(part.world(), pos, icontr.getBlock(), icontr.getMetadata());
        var.setup(verts, side % 6, pos, world, part.getRenderBounds());
        var.renderSide(verts, side % 6, pos, lightMatrix, getColour(0), part.getRenderBounds());

    }

    @Override
    public String getLocalizedName() {
        return LanguageRegistry.instance().getStringLocalization(((Carvable) block()).getVariation(meta()).description) ;

    }

    @Override
    public int getMetadata() {
        return icontr.getMetadata();
    }

    @Override
    public World getWorld() {
        return icontr.getWorld();
    }

    @Override
    public Block getBlock() {

        return icontr.getBlock();
    }

    @Override
    public BlockCoord getPos() {

        return icontr.getPos();
    }

    final static Vector3 axis = new Vector3(0, 0.5, 0);

    static class microMaterialCheck implements IConnectionCheck {
        static Vector3 point = new Vector3();
        static Vector3 enclosurePoint = new Vector3(0.5, 0.5, 0.5);
        boolean isMultipart;
        boolean isMultipartOpaque;


        @Override
        public boolean opacityCheck(IBlockAccess world, double dx, double dy, double dz) {
            int x = MathHelper.floor_double(dx);
            int y = MathHelper.floor_double(dy);
            int z = MathHelper.floor_double(dz);
            dx = dx - x;
            dy = dy - y;
            dz = dz - z;
            TileEntity te = world.getTileEntity(x, y, z);
            isMultipartOpaque = false;
            if (te instanceof TileMultipart) {
                isMultipartOpaque = true;
                point.set(Math.abs(dx % 1), Math.abs(dy % 1), Math.abs(dz % 1));
                TileMultipart tmp = (TileMultipart) te;
                for (TMultiPart npart : tmp.jPartList()) {
                    Cuboid6 bounds = npart.getRenderBounds();
                    if (npart instanceof Microblock) {
                        if (point.equalsT(Vector3.zero) || checkBounds(bounds, point)) {
                            // FMLLog.info("Is in");
                            Microblock mb = (Microblock) npart;
                            return mb.isTransparent();
                        }
                    }
                }

            }

            return false;
        }

        @Override
        public boolean checkConnection(IBlockAccess world, double dx, double dy, double dz, int id, int meta) {
            int x = MathHelper.floor_double(dx);
            int y = MathHelper.floor_double(dy);
            int z = MathHelper.floor_double(dz);
            dx = dx - x;
            dy = dy - y;
            dz = dz - z;
            TileEntity te = world.getTileEntity(x, y, z);
            isMultipart = false;
            if (te instanceof TileMultipart) {
                isMultipart = true;
                point.set(Math.abs(dx % 1), Math.abs(dy % 1), Math.abs(dz % 1));
                TileMultipart tmp = (TileMultipart) te;
                for (int i = 0; i < tmp.jPartList().size(); i++) {
                    TMultiPart npart = tmp.jPartList().get(i);
                    Cuboid6 bounds = npart.getRenderBounds().copy().enclose(enclosurePoint);
                    if (npart instanceof Microblock) {
                        if (point.equalsT(Vector3.zero) || checkBounds(bounds, point)) {
                            // FMLLog.info("Is in");
                            Microblock mb = (Microblock) npart;
                            IMicroMaterial m = MicroMaterialRegistry.getMaterial(mb.getMaterial());
                            boolean temp = meta == m.getItem().getItemDamage();
                            if (temp)
                                return temp;

                        }
                    }
                }

            }
            //FMLLog.info("END test=======================");

            return false;
        }

        public boolean checkBounds(Cuboid6 bound, Vector3 point) {
            if (point.x >= bound.min.x && point.x <= bound.max.x)
                if (point.y >= bound.min.y && point.y <= bound.max.y)
                    if (point.z >= bound.min.z && point.z <= bound.max.z)
                        return true;
            return false;
        }

        @Override
        public boolean continueCheck() {
            return !isMultipart;
        }

        @Override
        public boolean contineOpaqueCheck() {
            return !isMultipartOpaque;
        }
    }

}

