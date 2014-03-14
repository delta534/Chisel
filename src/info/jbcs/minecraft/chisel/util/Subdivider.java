package info.jbcs.minecraft.chisel.util;

import codechicken.lib.render.UV;

import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.Line3;
import codechicken.lib.vec.Vector3;
import info.jbcs.minecraft.chisel.render.CTM;

public class Subdivider {
    public static class Result
    {
        public final Vertex5[] verts_ = {new Vertex5(),new Vertex5(),new Vertex5(),new Vertex5()};
        public int iconIndex;
        public final Vector3 offset=new Vector3();
    }

    static final Line3 [] edges = new Line3[]{new Line3(),new Line3(),new Line3(),new Line3()};
    static final Line3 [] UVLines = new Line3[]{new Line3(),new Line3(),new Line3(),new Line3()};
    static final UV [] flattenVecs={new UV(),new UV(),new UV(),new UV()};
    static final Vector3 []intersections={new Vector3(),new Vector3(),new Vector3(),new Vector3()};
    static final Vector3 []uvInt={new Vector3(),new Vector3(),new Vector3(),new Vector3()};
    static final Result [] results={new Result(),new Result(),new Result(),new Result()};
    public static int numResults;
    /*
    static  int getVecRegion(Vector3 v)
    {
        int region=0;
        if(v.x>0.5)
            region+=1;
        if(v.y>0.5)
            region+=2;
        if(v.z<0.5)
            region+=4;
        return  region;
    }  */
    static int getUVRegion(UV uv)
    {
        int region=0;
        if(uv.u%2>0.5)
            region+=1;
        if(uv.v>0.5)
            region+=2;

        return  region;
    }

    static int pointCheck(UV near,UV far)
    {
        int flags=0;
        if(near.u<0.5&&far.u>0.5)
            flags+=1;
        if(near.v<0.5&&far.v>0.5)
            flags+=2;
        return  flags;
    }
    static int findFarthest(int side, UV[] uvs)
    {
        int index=0;
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

            du=uvs[i].u-0.5;
            dv=uvs[i].v-0.5;
            du=du*du;
            dv=dv*dv;
            res=du+dv;

            if(res>len)
            {
                len=res;
                index=i;
            }
        }
        return index;
    }

    static void flattenVec3(int side,Vector3 vec, UV store)
    {
        switch (side%6) {
            case 0:
            case 1:
                store.set(vec.x, vec.z);
                break;
            case 2:
            case 3:
                store.set(vec.x, vec.y);
                break;
            case 4:
            case 5:
                store.set(vec.z, vec.y);
                break;
        }
    }
    static void correctUV(UV uv,UV offset)
    {
        uv.mul(2).add(offset);
    }
    static void calcVecOffset(Vector3 vec,int side,Vector3 store)
    {
        store.set(0.25,0.25,0.25);
        if(vec.x>0.5)
            store.x+=0.5;
        else if (vec.x==0.5)
            store.x+=.25;
        if(vec.y>0.5)
            store.y+=0.5;
        else if (vec.y==0.5)
            store.y+=.25;
        if(vec.z>0.5)
            store.z+=0.5;
        else if (vec.z==0.5)
            store.z+=.25;

    }
    static void calcUVOffset(UV uv,UV store)
    {
        store.set(0,0);
        if(uv.u%2>0.5)
            store.u=-1;
        if(uv.v>0.5)
            store.v=-1;
    }
    static final int [][] oppEdge={{2,3},{3,0},{0,1},{1,2}};
    static final UV near=new UV();
    static final UV far=new UV();
    static final UV offset=new UV();
    static final UV temp=new UV();
    static final Vector3 vecStore=new Vector3();
    static final Vector3 midpoint=new Vector3();
    static final UV uvAv=new UV();
    static final int []edgeIndex=new int[2];
    static final int []uvIndex=new int[2];
    static final Line3 [] midLines ={new Line3(),new Line3()};//1 up and down, 0 left and right
    static
    {

        midLines[1].pt1=new Vector3(0.5,0,0);
        midLines[1].pt2=new Vector3(0.5,0,1);
        midLines[0].pt1=new Vector3(0,0,0.5);
        midLines[0].pt2=new Vector3(1,0,0.5);
    }
    public  static Result getResult(int i)
    {
        return  results[i];
    }
    public static void setup(Vertex5 [] verts,int side)
    {
        numResults=0;

        midpoint.set(0,0,0);
        uvAv.set(0,0);
        for(int i=0;i<4;i++)
        {
            flattenVec3(side,verts[i].vec,flattenVecs[i]);
            midpoint.add(verts[i].vec);
            verts[i].uv.u=verts[i].uv.u%2;
            uvAv.add(verts[i].uv);

        }
        midpoint.multiply(0.25);
        uvAv.mul(0.25);
        int farIndex= findFarthest(side, flattenVecs);
        far.set(flattenVecs[farIndex]);
        near.set(flattenVecs[(2+farIndex)%4]);
        int flags;
        if(near.u<far.u)
        {
            flags=pointCheck(near,far);
        }
        else
        {
            flags=pointCheck(far,near);
        }
        for(int i=0;i<4;i++)
        {
            edges[i].pt1.set(flattenVecs[i].u,0,flattenVecs[i].v);
            edges[i].pt2.set(flattenVecs[(i+1)%4].u,0,flattenVecs[(i+1)%4].v);
            UVLines[i].pt1.set(verts[i].uv.u,0,verts[i].uv.v);
            UVLines[i].pt2.set(verts[(i+1)%4].uv.u,0,verts[(i+1)%4].uv.v);
        }




        switch (flags)
        {
            case 3:
                numResults=4;
                for(int i=0;i<4;i++)
                {
                   if(! Line3.intersection2D(edges[i],midLines[0],intersections[i]))
                   {
                       Line3.intersection2D(edges[i],midLines[1],intersections[i]);
                   }
                    if(!Line3.intersection2D(UVLines[i],midLines[0],uvInt[i]))
                    {
                        Line3.intersection2D(UVLines[i],midLines[1],uvInt[i]);
                    }
                }
                for(int i=0;i<4;i++)
                {
                    Result result=results[i];
                    result.iconIndex=getUVRegion(verts[i].uv);
                    calcVecOffset(verts[i].vec,side,result.offset);
                    result.verts_[0].vec.set(verts[i].vec);
                    result.verts_[0].uv.set(verts[i].uv);
                    result.verts_[2].uv.set(0.5, 0.5);
                    switch (side%6)
                    {
                        case 0:
                        case 1:
                            result.verts_[1].vec.set(intersections[i].x,midpoint.y,intersections[i].z);
                            result.verts_[2].vec.set(0.5,midpoint.y,0.5);
                            result.verts_[3].vec.set(intersections[(i+3)%4].x,midpoint.y,intersections[(i+3)%4].z);
                            break;
                        case 2:
                        case 3:
                            result.verts_[1].vec.set(intersections[i].x,intersections[i].z,midpoint.z);
                            result.verts_[2].vec.set(0.5,0.5,midpoint.z);
                            result.verts_[3].vec.set(intersections[(i+3)%4].x,intersections[(i+3)%4].z,midpoint.z);
                            break;
                        case 4:
                        case 5:
                            result.verts_[1].vec.set(midpoint.x,intersections[i].z,intersections[i].x);
                            result.verts_[2].vec.set(midpoint.x,0.5,0.5);
                            result.verts_[3].vec.set(midpoint.x,intersections[(i+3)%4].z,intersections[(i+3)%4].x);
                            break;
                    }
                    result.verts_[1].uv.set(uvInt[i].x,uvInt[i].z);
                    result.verts_[3].uv.set(uvInt[(i+3)%4].x,uvInt[(i+3)%4].z);
                    calcUVOffset(verts[i].uv,offset);
                    for(Vertex5 vert:result.verts_)
                    {
                        correctUV(vert.uv,offset);
                    }
                }
                break;

            case 1:
            case 2:
                for(int i=0;i<2;i++)
                {
                    if(! Line3.intersection2D(edges[i*2],midLines[flags%2],intersections[i]))
                    {
                        Line3.intersection2D(edges[(i*2+1)%4],midLines[flags%2],intersections[i]);
                        edgeIndex[i]=(i*2+1)%4;
                    }
                    else
                    {
                        edgeIndex[i]=2*i;
                    }
                    if(!Line3.intersection2D(UVLines[i*2],midLines[flags%2],uvInt[i]))
                    {
                        Line3.intersection2D(UVLines[(i*2+1)%4],midLines[flags%2],uvInt[i]);
                    }
                    for(int j=0;j<4;j++)
                    {

                        results[i].verts_[j].vec.set(verts[j].vec);
                        results[i].verts_[j].uv.set(verts[j].uv);
                    }
                }

                for(int i=0;i<2;i++)
                {
                    int indexa=edgeIndex[i];
                    int indexb=(edgeIndex[i]+3)%4   ;
                    Result result=results[i];
                    switch (side%6)
                    {
                        case 0:
                        case 1:
                            result.verts_[indexa].vec.set(intersections[i].x,midpoint.y,intersections[i].z);
                            result.verts_[indexb].vec.set(intersections[(i+1)%2].x,midpoint.y,intersections[(i+1)%2].z);
                            break;
                        case 2:
                        case 3:
                            result.verts_[indexa].vec.set(intersections[i].x,intersections[i].z,midpoint.z);
                            result.verts_[indexb].vec.set(intersections[(i+1)%2].x,intersections[(i+1)%2].z,midpoint.z);
                            break;
                        case 4:
                        case 5:
                            result.verts_[indexa].vec.set(midpoint.x,intersections[i].z,intersections[i].x);
                            result.verts_[indexb].vec.set(midpoint.x,intersections[(i+1)%2].z,intersections[(i+1)%2].x);
                            break;
                    }
                    result.verts_[indexa].uv.set(uvInt[i].x,uvInt[i].z);
                    result.verts_[indexb].uv.set(uvInt[(i+1)%2].x,uvInt[(i+1)%2].z);
                    vecStore.set(0,0,0);
                    temp.set(0,0);
                    for(int j=0;j<4;j++)
                    {
                        vecStore.add(result.verts_[j].vec);
                        temp.add(result.verts_[j].uv);
                    }
                    vecStore.multiply(0.25);
                    temp.mul(0.25);
                    calcUVOffset(temp,offset);
                    result.iconIndex=getUVRegion(temp);
                    calcVecOffset(vecStore,side,result.offset);
                    for(Vertex5 vert:result.verts_)
                    {
                        correctUV(vert.uv,offset);
                    }
                }
                numResults=2;
                break;


            default:
                numResults=1;
                calcVecOffset(midpoint,side,vecStore);
                results[0].iconIndex=getUVRegion(uvAv);
                calcUVOffset(uvAv,offset);
                for(int i=0;i<4;i++)
                {
                    Vertex5 vert=verts[i];
                    results[0].verts_[i].uv.set(vert.uv);
                    correctUV(results[0].verts_[i].uv, offset);
                    results[0].verts_[i].vec.set(vert.vec);
                }
                results[0].offset.set(vecStore);
                break;

        }


    }






}
