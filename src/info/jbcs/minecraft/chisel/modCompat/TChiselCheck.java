package info.jbcs.minecraft.chisel.modCompat;

import info.jbcs.minecraft.chisel.util.IChiselCheck;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class TChiselCheck extends TileMultipart implements IChiselCheck {

	@Override
	public boolean ContainsEquivalentBlock(int inID, int inMeta) {
		boolean flag=false;
		for(TMultiPart part: jPartList())
		{
			if(part instanceof IChiselCheck)
				flag|=((IChiselCheck)part).ContainsEquivalentBlock(inID, inMeta);
		}
		return flag;
	}

}
