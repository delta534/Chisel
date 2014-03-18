package info.jbcs.minecraft.chisel.gui;

import info.jbcs.minecraft.chisel.items.ItemChisel;
import info.jbcs.minecraft.utilities.General;
import info.jbcs.minecraft.utilities.InventoryStatic;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class InventoryChiselSelection extends InventoryStatic {
    ItemStack chisel = null;
    final static int normalSlots = 32;
    int activeVariations = 0;
    ContainerChisel container;

    public InventoryChiselSelection(ItemStack c) {
        super(normalSlots + 1);

        chisel = c;
    }

    @Override
    public String getInvName() {
        return "Carve blocks";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void onInventoryChanged() {
    }

    public void clearItems() {
        activeVariations = 0;
        for (int i = 0; i < normalSlots; i++) {
            items[i] = null;
        }
    }

    public ItemStack getStackInSpecialSlot() {
        return items[normalSlots];
    }

    public void updateItems() {
        ItemStack chiseledItem = items[normalSlots];

        clearItems();

        if (chiseledItem == null || chiseledItem.itemID < 0 || chiseledItem.itemID >= Item.itemsList.length) {
            container.onChiselSlotChanged();
            return;
        }

        if (chiseledItem.itemID >= Block.blocksList.length)
            return;

        Item item = General.getItem(chiseledItem);
        if (item == null) return;

        ArrayList<ItemStack> list = container.carving.getItems(chiseledItem);

        activeVariations = 0;
        while (activeVariations < normalSlots && activeVariations < list.size()) {
            items[activeVariations] = list.get(activeVariations);
            activeVariations++;
        }

        container.onChiselSlotChanged();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public boolean isInvNameLocalized() {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        if (stack != null && (Item.itemsList[stack.itemID] instanceof ItemChisel)) {
            return false;
        }

        return i == normalSlots;
    }
}
