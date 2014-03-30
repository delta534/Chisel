package info.jbcs.minecraft.chisel.tiles;

import info.jbcs.minecraft.chisel.core.CarvingRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayDeque;
import java.util.HashMap;

public class TileEntityAutoChisel extends TileEntity implements ISidedInventory {
    public ItemStack[] items;

    HashMap<String, ItemStack> patternIDs;
    public boolean active;
    public ArrayDeque<ItemStack> processed;
    public static final int GuiInputSlot = 0;
    public static final int OutputSlot = 1;
    public static final int SideInputSlot = 2;
    public static final int CopySlotStart = 3;
    public static final byte MaxProcessedSize = 32;
    static int[] sideslots = {OutputSlot, SideInputSlot};
    static int[] inputslots = {GuiInputSlot, SideInputSlot};

    public TileEntityAutoChisel() {
        items = new ItemStack[19];
        patternIDs = new HashMap<String, ItemStack>();
        processed = new ArrayDeque<ItemStack>();
        active = true;
    }

    public boolean isCopySlot(int i) {
        return i >= CopySlotStart;
    }

    @Override
    public int getSizeInventory() {
        return 19;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return items[i];
    }

    public boolean changeStack(ItemStack is) {
        CarvingRegistry.CarvingGroup group = CarvingRegistry.chisel.getGroup(is.itemID, is.getItemDamage());
        if (this.patternIDs.containsKey(group.className)) {
            ItemStack pattern = patternIDs.get(group.className);
            is.itemID = pattern.itemID;
            is.setItemDamage(pattern.getItemDamage());
            return true;
        }
        return false;
    }

    public boolean mergeStacks(ItemStack is1, ItemStack is2) {
        is1.stackSize += is2.stackSize;
        is2.stackSize = 0;
        if (is1.stackSize > is1.getMaxStackSize()) {
            int diff = is1.stackSize - is1.getMaxStackSize();
            is1.stackSize = is1.getMaxStackSize();
            is2.stackSize = diff;
        }
        return is2.stackSize > 0;
    }

    public void addProcessedStack(ItemStack stk) {
        boolean addToQueue = true;
        for (ItemStack item : processed) {
            if (item.stackSize < 64 && item.itemID == stk.itemID) {
                addToQueue = !mergeStacks(item, stk);
                break;
            }
        }
        if (addToQueue) {
            processed.add(stk);
        }
    }

    public void SetOutput(ItemStack stk) {
        ItemStack output = items[1];
        if (output == null)
            output = stk;
        else {
            if (output.itemID == stk.itemID) {
                if (mergeStacks(output, stk))
                    addProcessedStack(stk);
            } else {
                addProcessedStack(stk);
            }
        }
        items[1] = output;
    }

    public void setActive(boolean flag) {
        active = flag;
    }

    public void onAutoInventoryChanged() {
        if (active) {
            for (int i : inputslots) {
                if (items[i] != null) {
                    ItemStack input = items[i].copy();

                    if (i != 0 || items[OutputSlot] == null) {
                        changeStack(input);
                        items[i] = null;
                        SetOutput(input);
                    } else {
                        if (CarvingRegistry.chisel.isVariationOfSameClass(input.itemID, input.getItemDamage(),
                                items[OutputSlot].itemID, items[OutputSlot].getItemDamage())) {
                            mergeStacks(items[OutputSlot], input);
                            items[0].stackSize = input.stackSize;

                        }
                    }
                }
            }
        }
        super.onInventoryChanged();

    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        readFromNBT(packet.data);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack outstack = null;
        if (i == OutputSlot || i == GuiInputSlot) {
            if (items[i].stackSize <= j) {
                outstack = items[i];
                items[i] = processed.poll();
            } else {
                outstack = items[i].copy();
                outstack.stackSize = j;
                items[i].stackSize -= j;
            }
        }
        if (isCopySlot(i)) {
            CarvingRegistry.CarvingGroup group = CarvingRegistry.chisel.getGroup(items[i].itemID, items[i].getItemDamage());

            patternIDs.remove(group.className);
            outstack = items[i];
            outstack.stackSize = -1;
            items[i] = null;
        }
        onAutoInventoryChanged();
        return outstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {

        if (isCopySlot(i)) {
            if (items[i] != null) {
                CarvingRegistry.CarvingGroup group = CarvingRegistry.chisel.getGroup(items[i].itemID, items[i].getItemDamage());
                patternIDs.remove(group.className);
            }
            if (itemstack != null) {
                CarvingRegistry.CarvingGroup group = CarvingRegistry.chisel.getGroup(itemstack.itemID, itemstack.getItemDamage());
                patternIDs.put(group.className, itemstack);
                itemstack.stackSize = 1;
            }
        }
        items[i] = itemstack;
        onAutoInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "auto chisel";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    public boolean hasPattern(ItemStack i) {
        CarvingRegistry.CarvingGroup group = CarvingRegistry.chisel.getGroup(i.itemID, i.getItemDamage());

        return patternIDs.containsKey(group.className);
    }

    public boolean canCarve(ItemStack i) {
        return CarvingRegistry.chisel.getGroup(i.itemID, i.getItemDamage()) != null;
    }

    public boolean validBlock(ItemStack itemstack) {
        return itemstack.itemID < Block.blocksList.length;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == GuiInputSlot)
            return itemstack.isItemEqual(items[i]);
        if (isCopySlot(i) && validBlock(itemstack))
            return canCarve(itemstack) && !hasPattern(itemstack);
        if (i == OutputSlot)
            return false;
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return sideslots;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if (i == SideInputSlot) {
            return hasPattern(itemstack) && (processed.size() < MaxProcessedSize);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if (i == OutputSlot)
            return items[i] != null;
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList tagItems = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagItems.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagItems.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < items.length) {
                ItemStack is = ItemStack.loadItemStackFromNBT(tag);
                setInventorySlotContents(slot, is);
            }
        }
        tagItems = tagCompound.getTagList("Processed");
        for (int i = 0; i < tagItems.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagItems.tagAt(i);
            ItemStack is = ItemStack.loadItemStackFromNBT(tag);
            processed.add(is);

        }
        NBTTagCompound run = tagCompound.getCompoundTag("active");
        setActive(run.getBoolean("dat"));


    }


    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                tag = stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
        NBTTagList itemList2 = new NBTTagList();
        for (ItemStack stack : processed) {
            NBTTagCompound tag = new NBTTagCompound();
            tag = stack.writeToNBT(tag);
            itemList2.appendTag(tag);
        }
        tagCompound.setTag("Processed", itemList2);
        NBTTagCompound run = new NBTTagCompound();
        run.setBoolean("dat", active);
        tagCompound.setTag("active", run);
    }

}
