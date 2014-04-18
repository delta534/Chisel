package info.jbcs.minecraft.utilities;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMetaBlock extends ItemBlock {
	public ItemMetaBlock(Block b) {
		super(b);
		setMaxDamage(0);
		setHasSubtypes(true);
	}


	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack == null) {
			return field_150939_a.getUnlocalizedName();
		}

		return field_150939_a.getUnlocalizedName();
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}
}
