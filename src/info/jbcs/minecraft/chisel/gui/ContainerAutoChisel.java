package info.jbcs.minecraft.chisel.gui;

import info.jbcs.minecraft.chisel.tiles.TileEntityAutoChisel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoChisel extends Container {
	TileEntityAutoChisel inventory;
	InventoryPlayer playerInventory;
	public ContainerAutoChisel(TileEntityAutoChisel inv,InventoryPlayer playerI)
	{
		this.inventory=inv;
		this.playerInventory=playerI;
		int[] leftOffsets = { 8, 26, 134, 152, 44, 116 };
		int[] topOffsets = { 14, 32, 50, 68 };


		addSlotToContainer(new Slot(inventory, 0, 55, 11));
		addSlotToContainer(new AutoOutSlot(inventory,1,106,11));
		int index = 3;
		for (int x = 0; x < 7; x++) {
            for(int y=0;y<2;y++)
            {
                addSlotToContainer(new SlotAutoChisel( inventory, index, 26+18*x, 42+18*y));
                index++;

            }

		}


		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				addSlotToContainer(new Slot(playerInventory, j1 + k * 9 + 9, 8 + j1 * 18, 105 + k * 18 - 18));
			}
		}

		for (int l = 0; l < 9; l++) {
			addSlotToContainer(
					new Slot(playerInventory, l, 8 + l * 18, 163 - 18)
					);
		}
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return inventory.isUseableByPlayer(entityplayer);

	}
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer){
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entity, int i) {

		return null;
	}

    @Override
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
        return super.mergeItemStack(par1ItemStack, par2, par3, par4);
    }

    class SlotAutoChisel extends Slot
	{

		public int itemIndex = 0;

		public SlotAutoChisel(IInventory par1iInventory, int par2, int par3,
				int par4) {
			super(par1iInventory, par2, par3, par4);
			itemIndex=par2;	
		}
		@Override
		public void putStack(ItemStack stack) {

		}
		@Override
		public boolean isItemValid(ItemStack stack)
		{				
			if (stack != null)
			{
				if(this.inventory.isItemValidForSlot(this.getSlotIndex(), stack))
				{

					this.inventory.setInventorySlotContents(itemIndex, stack.copy());
				}
			}
			return false;
		}

		@Override
		public boolean canTakeStack(EntityPlayer player)
		{
			
			inventory.setInventorySlotContents(itemIndex, player.inventory.getItemStack());
			return false;
		}


	}
	class AutoOutSlot extends Slot
	{

		public AutoOutSlot(IInventory par1iInventory, int par2, int par3,
				int par4) {
			super(par1iInventory, par2, par3, par4);
		}
		@Override
		public boolean isItemValid(ItemStack stack)
		{				
			return false;
		}
		
	}
}
