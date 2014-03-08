package info.jbcs.minecraft.chisel.util;

import codechicken.lib.render.UV;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.render.CTM;

public class QuadDivider {


    public class subQuad {
        final Vertex5[] verts_ = new Vertex5[] { new Vertex5(), new Vertex5(),
                new Vertex5(), new Vertex5() };
        Vector3 edgeAve=new Vector3();
        public byte size;
        public int region;
        int first = -1;
        public int octRegion = 0;
        public UV offset_;
        public final Vector3 midPoint=new Vector3();
        public void reset() {
            for (int i = 0; i < 4; i++) {
                verts_[i].set(0, 0, 0, 0, 0);

            }
            size = 0;
            first = -1;
        }

        public void addVert(Vertex5 vert, int i) {
            if (first == -1)
                first = i;
            verts_[i].vec.set(vert.vec);
            verts_[i].uv.set(vert.uv);
            verts_[i].uv.u=verts_[i].uv.u%2;
            size++;
        }

        public subQuad(double offU, double offV) {
            offset_ = new UV(offU, offV);
        }

        public Vertex5[] work(int side) {
            switch (size) {

                case 1:
                    point.set(0.5, 0.5);
                    split4(side);
//                    offset_=offsets[region];
                    break;
                default:
                    //offset_=offsets[region];
                    break;
            }
            for(int i=0;i<4;i++)
            {
                verts_[i].uv.mul(2).add(offset_);
            }



            return verts_;
        }

        final UV diff = new UV();
        final UV[] uvs = new UV[] { new UV(), new UV(), new UV(), new UV() };


        final UV point = new UV();
        final UV uvdiff=new UV();
        public void split4(int side) {

            diff.set(vert2D[first].u - point.u, vert2D[first].v - point.v);
            uvdiff.set(verts_[first].uv.u-0.5,verts_[first].uv.v-0.5);
            res[0].set(vert2D[first]);
            uvs[0].set(verts_[first].uv);
            res[2].set(point);
            uvs[2].set(0.5,0.5);

            if(first%2==0)
            {
                res[1].set(point.u+diff.u,point.v);
                res[3].set(point.u,point.v+diff.v);
                uvs[1].set(0.5+uvdiff.u,0.5);

                uvs[3].set(0.5,0.5+uvdiff.v);
            }
            else
            {
                res[3].set(point.u + diff.u, point.v);
                res[1].set(point.u, point.v + diff.v);
                uvs[3].set(0.5+uvdiff.u,0.5);
                uvs[1].set(0.5,0.5+uvdiff.v);
            }


            finishSplit(side);
        }

        public void finishSplit(int side) {


            double d=0;
            for (int j = 0; j < 4; j++) {
                switch (side) {
                    case 0:
                        verts_[j].vec.set(res[j].u, edgeAve.y, res[j].v);
                        verts_[j].uv.set(uvs[j]);
                        break;
                    case 1:
                        verts_[j].vec.set(res[j].u, edgeAve.y, res[j].v);
                        verts_[j].uv.set(uvs[j]);
                        break;
                    case 2:
                        verts_[j].vec.set(res[j].u, res[j].v, edgeAve.z);
                        verts_[j].uv.set(uvs[j]);
                        break;
                    case 3:
                        verts_[j].vec.set(res[j].u, res[j].v, edgeAve.z);
                        verts_[j].uv.set(uvs[j]);
                        break;
                    case 4:
                        verts_[j].vec.set(edgeAve.x, res[j].v, res[j].u);
                        verts_[j].uv.set(uvs[j]);
                        break;
                    case 5:
                        verts_[j].vec.set(edgeAve.x, res[j].v, res[j].u);
                        verts_[j].uv.set(uvs[j]);
                        break;
                }
            }
        }

    }



    private final UV[] res = new UV[] { new UV(), new UV(), new UV(), new UV() };
    public final subQuad[] quads = new subQuad[] { new subQuad(0, 0),
            new subQuad(0, -1), new subQuad(-1, 0), new subQuad(-1, -1) };
    UV[] offsets={new UV(0,0),new UV(-1,0),new UV(0,-1),new UV(-1,-1)};
    private static byte getRegion(UV uv) {
        byte region = CTM.lowerleft;
        if (uv.u%2 <= 0.5) {
            if (uv.v >= 0.5) {
                region = CTM.lowerleft;
            }
            else
            {
                region = CTM.upperleft;
            }
        } else {
            if (uv.v >= 0.5) {
                region = CTM.lowerright;
            } else {
                region = CTM.upperright;
            }
        }
        return region;
    }

    private static int getOctalRegion(Vector3 v)
    {
        int region=0;
        if(v.x<.5)
        {
            if(v.y<.5)
            {
                if(v.z>.5)
                    region=0;
                else
                    region=4;
            }
            else
            {
                if(v.z>.5)
                    region=1;
                else
                    region=5;
            }
        }
        else
        {
            if(v.y<.5)
            {
                if(v.z>.5)
                    region=2;
                else
                    region=6;
            }
            else
            {
                if(v.z>.5)
                    region=3;
                else
                    region=7;
            }
        }
        return region;
    }


    private static UV vecToUv(Vector3 vect, int side) {
        UV outUV = new UV();

        switch (side) {
            case 0:
            case 1:
                outUV.set(vect.x, vect.z);
                break;
            case 2:
            case 3:
                outUV.set(vect.x, vect.y);
                break;
            case 4:
            case 5:
                outUV.set(vect.z, vect.y);
                break;
        }
        return outUV;
    }
    private final UV [] vert2D=new UV[4];
    public int [] order=new int[4];
    public int farIndex;

    public void setup(Vertex5[] verts, int side) {
        double len=0;
        double res;
        double du;
        double dv;

        int limit=4;
        int start=0;
        int incr=1;
        if(side%6==0||side%6==3||side%6==4)
        {
            limit=-1;
            start=3;
            incr=-1;
        }
        for(int i=start;i!=limit;i+=incr)
        {
            vert2D[i]=vecToUv(verts[i].vec,side%6);
            du=vert2D[i].u-0.5;
            dv=vert2D[i].v-0.5;
            du=du*du;
            dv=dv*dv;
            res=du+dv;

            if(res>len)
            {
                len=res;
                farIndex=i;
            }
        }


        int octR=getOctalRegion(verts[farIndex].vec);

        UV far = vert2D[farIndex];
        UV near = vert2D[(farIndex + 2) % 4];
        int pflags;
        if(far.u<=near.u) {
            pflags = pointCheck(far.u, far.v, near.u - far.u, near.v - far.v);
        }
        else {
            pflags = pointCheck(near.u, near.v, far.u - near.u, far.v - near.v);
        }

        int region;
        switch (pflags)
        {
            case inU|inV:
                for (byte i = 0; i < 4; i++) {

                    region=getRegion(vert2D[i]);
                    octR=getOctalRegion(verts[i].vec);
                    //region=;
                    quads[region].edgeAve.set(verts[i].vec).add(verts[(i+1)%4].vec).multiply(0.5);
                    quads[region].octRegion=octR;
                    quads[region].addVert(verts[i], i);
                    quads[region].region=region;
                    order[region]=(i);
                    quads[region].midPoint.set(verts[i].vec).add(0.5,0.5,0.5).multiply(0.5);
                    quads[region].offset_=offsets[getRegion(verts[i].uv)];

                }
                break;
            case inU:

                //break;
            case inV:
                //break;
            default:

                //region=octFaces[octR][side%6];
                region= getRegion(far);
                //region=getRegionCCW(near);
                quads[region].region=region;
                quads[region].octRegion=octR;
                quads[region].midPoint.set(0, 0, 0);
                for(int i=0;i<4;i++)
                {


                    //region= VariationCTMX.uvIndex[side%6][region];
                    order[region]=i;
                    quads[region].addVert(verts[i], i);
                    quads[region].midPoint.add(verts[i].vec);
                }
                quads[region].midPoint.multiply(0.25);
                quads[region].offset_=offsets[getRegion(verts[farIndex].uv)];
        }
    }


    private static final int inU =1;
    private static final int inV =2;
    private static int pointCheck(double u, double v, double h, double w)
    {
        int flags=0;
        if(0.5>u&&0.5<(u+w))
            flags+= 1;
        if(0.5>v&&0.5<(v+h))
            flags+=2;
        return flags;
    }

}


