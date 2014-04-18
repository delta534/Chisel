package info.jbcs.minecraft.chisel.items;

import codechicken.lib.lang.LangUtil;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.core.Carvable;
import info.jbcs.minecraft.chisel.core.RenderVariation;
import info.jbcs.minecraft.chisel.util.IMetaDataName;
import info.jbcs.minecraft.utilities.General;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemCarvable extends ItemBlock {
    int blockId;

    public ItemCarvable(int id) {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
        blockId = id + 256;
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        Block bl = Block.blocksList[blockId];
        if (bl instanceof IMetaDataName)
            return ((IMetaDataName) bl).getUnlocalizedName(itemstack.getItemDamage());
        if (bl instanceof Carvable) {
            Carvable cv = (Carvable) bl;
            RenderVariation var = cv.getVariation(itemstack.getItemDamage());
            if (var != null && !Chisel.blockDescriptions)
                return var.description;

        }
        return bl.getUnlocalizedName();
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return Block.blocksList[blockId].getIcon(2, damage);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        if (!Chisel.blockDescriptions) return;

        Item item = General.getItem(stack);
        if (item == null) return;

        Block block = General.getBlock(item.itemID);
        if (!(block instanceof Carvable)) return;

        Carvable carvable = (Carvable) block;
        RenderVariation var = carvable.getVariation(stack.getItemDamage());
        if (var == null) return;

        lines.add(LangUtil.translateG(var.description));
    }

}
