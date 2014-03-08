package info.jbcs.minecraft.chisel.render;

import codechicken.lib.render.UV;

public class Util {
	static final double uc = 0.5;
	static final double vc = -0.5;
	// sides order 0=bottom,1=top,2=,3=,4=,5=
	
	public static class RotationData
	{
		public int rotateYNeg;//bottom
		public int rotateYPos;//top
		public int rotateXNeg;//east
		public int rotateXPos;//west
		public int rotateZNeg;//north
		public int rotateZPos;//south
		public RotationData()
		{
			rotateYNeg=0;
			rotateYPos=0;
			rotateXNeg=0;
			rotateXPos=0;
			rotateZNeg=0;
			rotateZPos=0;
		}
		public void clear()
		{
			rotateYNeg=0;
			rotateYPos=0;
			rotateXNeg=0;
			rotateXPos=0;
			rotateZNeg=0;
			rotateZPos=0;
		}
	}
	public static void rotateUV(UV uv, int side, int rotation) {

		double u = uv.u % 2;
		double v = uv.v;
		if (side % 6 == 2 || side % 6 == 4 || side % 6 == 0) {
			switch (rotation) {

			case 0:
				break;
			case 3:
				uv.u = vc + v + uc;
				uv.v = -vc + uc - u;
				break;
			case 1:
				uv.u = 2 * uc - u;
				uv.v = -2 * vc - v;
				break;
			case 2:
				uv.u = -vc - v + uc;
				uv.v = -vc - uc + u;
				break;
			}
		} else {
			switch (rotation) {

			case 0:
				break;
			case 1:
				uv.u = vc + v + uc;
				uv.v = -vc + uc - u;
				break;
			case 2:
				uv.u = 2 * uc - u;
				uv.v = -2 * vc - v;
				break;
			case 3:
				uv.u = -vc - v + uc;
				uv.v = -vc - uc + u;
				break;
			}
		}
	}
}
