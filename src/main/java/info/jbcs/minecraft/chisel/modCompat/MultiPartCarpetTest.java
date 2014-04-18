package info.jbcs.minecraft.chisel.modCompat;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.*;
import codechicken.multipart.minecraft.McBlockPart;
import codechicken.multipart.minecraft.McSidedMetaPart;
import codechicken.multipart.minecraft.PartMetaAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.blocks.BlockMarbleCarpet;
import info.jbcs.minecraft.chisel.core.variation.VariationCTMX;
import info.jbcs.minecraft.chisel.render.RenderBlocksCTMCarpet;
import info.jbcs.minecraft.chisel.util.IChiselCheck;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Arrays;

public class MultiPartCarpetTest extends McSidedMetaPart implements IChiselCheck {

    static BlockMarbleCarpet base = Chisel.blockCarpetFloor;
    public byte meta;
    public static ChiselMultiPart partHandler;
    RenderBlocksCTMCarpet renderer;
    CCModel carpetModel;
    Cuboid6 bounds;
    static final Vertex5[] verts = new Vertex5[4];

    public MultiPartCarpetTest(int newMeta) {
        renderer = new RenderBlocksCTMCarpet();
        meta = (byte) newMeta;
        carpetModel = CCModel.quadModel(24);
        bounds = new Cuboid6(base.getBlockBoundsMinX(), base.getBlockBoundsMinY(), base.getBlockBoundsMinZ(),
                base.getBlockBoundsMaxX(), base.getBlockBoundsMaxY(), base.getBlockBoundsMaxZ());
        carpetModel.generateBlock(0, bounds);

    }

    public MultiPartCarpetTest() {
        renderer = new RenderBlocksCTMCarpet();
        meta = 0;
        carpetModel = CCModel.quadModel(24);
        bounds = new Cuboid6(base.getBlockBoundsMinX(), base.getBlockBoundsMinY(), base.getBlockBoundsMinZ(),
                base.getBlockBoundsMaxX(), base.getBlockBoundsMaxY(), base.getBlockBoundsMaxZ());
        carpetModel.generateBlock(0, bounds);
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {
        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return Arrays.asList(getBounds());
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
        return Arrays.asList(getBounds());
    }

    @Override
    public Iterable<ItemStack> getDrops() {
        return Arrays.asList(new ItemStack(base, 1, meta));
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {
        return new ItemStack(base, 1, meta);
    }

    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
        return base.getPlayerRelativeBlockHardness(player, player.worldObj, hit.blockX, hit.blockY, hit.blockZ) * 30;
    }

    @Override
    public IIcon getBreakingIcon(Object subPart, int side) {
        return base.getIcon(0, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBrokenIcon(int side) {
        return base.getIcon(0, 0);
    }

    @Override
    public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
        IconHitEffects.addHitEffects(this, hit, effectRenderer);
    }

    @Override
    public void addDestroyEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
        IconHitEffects.addDestroyEffects(this, effectRenderer, false);
    }

    @Override
    public int getLightValue() {
        return Block.lightValue[base.blockID];
    }

    public static void init() {
        partHandler = new ChiselMultiPart();
        MultiPartRegistry.registerConverter(partHandler);
        MultiPartRegistry.registerParts(partHandler, new String[]{"Chisel_Carpet"});
        MultipartGenerator.registerTrait("info.jbcs.minecraft.chisel.util.IChiselCheck", "info.jbcs.minecraft.chisel.modCompat.TChiselCheck");

    }

    @Override
    public Cuboid6 getBounds() {
        return bounds;

    }

    @Override
    public String getType() {
        return "Chisel_Carpet";
    }

    @Override
    public void save(NBTTagCompound tag) {
        tag.setByte("meta", meta);
    }

    @Override
    public void load(NBTTagCompound tag) {
        meta = tag.getByte("meta");
    }

    @Override
    public void writeDesc(MCDataOutput packet) {
        packet.writeByte(meta);
    }

    @Override
    public void readDesc(MCDataInput packet) {
        meta = packet.readByte();
    }

    @Override
    public void renderStatic(Vector3 pos, LazyLightMatrix lm, int pass) {

        IBlockAccess access = new PartMetaAccess(this);
        VariationCTMX var = (VariationCTMX) base.getVariation(meta);

        Tessellator.instance.setColorOpaque(255, 255, 255);
        CCRenderState.useModelColours(false);

        for (int i = 0; i < 6; i++) {


            for (int j = 0; j < 4; j++) {
                verts[j] = carpetModel.verts[j + i * 4];
            }
            var.setup(verts, i, pos, access, bounds);
            var.renderSide(verts, i, pos, lm.lightMatrix(), 0xFFFFFFF, bounds);
        }
    }

    @Override
    public World getWorld() {
        return world();
    }

    @Override
    public int getMetadata() {
        return meta;
    }

    @Override
    public int getBlockId() {
        return base.blockID;
    }

    @Override
    public BlockCoord getPos() {
        return new BlockCoord(tile());
    }

    @Override
    public boolean doesTick() {
        return false;
    }

    @Override
    public boolean ContainsEquivalentBlock(int inID, int inMeta) {
        return base.blockID == inID && meta == inMeta;
    }

    @Override
    public int sideForMeta(int meta) {

        return 0;
    }

    @Override
    public Block getBlock() {
        return base;
    }

    public static McBlockPart placement(World world, BlockCoord pos, int side, int meta) {
        McBlockPart part = null;
        if (side == 1) {
            part = new MultiPartCarpetTest(meta);
        }
        return part;
    }

}
