package info.jbcs.minecraft.chisel.modCompat;

import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;

public class ChiselPillarMicroMaterial extends ChiselMicroMaterial {

    public ChiselPillarMicroMaterial(Block arg0, int arg1) {
        super(arg0, arg1);


    }

    public void loadIcons() {
        icontr = new BlockPillarUVTransform(block(), meta());

    }

    public void renderMicroFace(Vertex5[] verts, int side, Vector3 pos,
                                LightMatrix lightMatrix, IMicroMaterialRender part) {

        icontr.bindPart(part);
        ((BlockPillarUVTransform) icontr).bindSide(side);
        ((BlockPillarUVTransform) icontr).update();
        UV uv = new UV();
        Tessellator t = Tessellator.instance;
        for (int i = 0; i < 4; i++) {
            if (CCRenderState.useNormals()) {
                Vector3 n = Rotation.axes[side % 6];
                t.setNormal((float) n.x, (float) n.y, (float) n.z);
            }
            Vertex5 vert = verts[i];
            if (lightMatrix != null) {
                LC lc = LC.computeO(vert.vec, side);
                if (CCRenderState.useModelColours())
                    lightMatrix.setColour(t, lc, getColour(part));
                lightMatrix.setBrightness(t, lc);
            } else {
                if (CCRenderState.useModelColours())
                    CCRenderState.vertexColour(getColour(part));
            }

            icontr.transform(uv.set(vert.uv));
            t.addVertexWithUV(vert.vec.x + pos.x, vert.vec.y + pos.y, vert.vec.z + pos.z, uv.u, uv.v);


        }

    }
}


