package info.jbcs.minecraft.chisel.modCompat;

import codechicken.lib.render.IUVTransformation;
import codechicken.lib.render.UV;
import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.multipart.minecraft.IPartMeta;
import codechicken.multipart.minecraft.PartMetaAccess;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

class DelayedIcons implements IUVTransformation, IPartMeta {

    public Block block;
    public int metadata;
    IMicroMaterialRender part;

    public DelayedIcons(Block bl, int m) {
        block = bl;
        metadata = m;
    }

    public void bindPart(IMicroMaterialRender par) {
        part = par;
    }

    @Override
    public void transform(UV texcoord) {
        int i = (int) texcoord.u >> 1;
        IBlockAccess world = new PartMetaAccess(this);
        Icon icon;
        if (part != null && part.world() != null) {
            icon = block.getBlockTexture(world, part.x(), part.y(), part.z(), i % 6);
        } else {
            icon = block.getIcon(i % 6, metadata);
        }

        texcoord.u = icon.getInterpolatedU(texcoord.u % 2 * 16);
        texcoord.v = icon.getInterpolatedV(texcoord.v % 2 * 16);
    }

    @Override
    public int getMetadata() {
        return metadata;
    }

    @Override
    public World getWorld() {
        return part.world() != null ? part.world() : null;
    }

    @Override
    public int getBlockId() {
        return block.blockID;
    }

    @Override
    public BlockCoord getPos() {
        if (part != null && part.world() != null)
            return new BlockCoord(part.x(), part.y(), part.z());
        return null;
    }


}