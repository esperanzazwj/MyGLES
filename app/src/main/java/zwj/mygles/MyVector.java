package zwj.mygles;

/**
 * Created by student on 2016/12/17.
 */

public class MyVector {
    public static void add(float []v, float []v1, float []v2){
        v[0]=v1[0]+v2[0];
        v[1]=v1[1]+v2[1];
        v[2]=v1[2]+v2[2];
    }
    public static float []normalize(float []v) {
        float[] res = new float[3];
        float len = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        res[0] = v[0] / len;
        res[1] = v[1] / len;
        res[2] = v[2] / len;
        return res;
    }
    public static void mul(float []v, float []v1, float k){
        v[0]*=v1[0]*k;
        v[1]*=v1[1]*k;
        v[2]*=v1[2]*k;
    }
    public static float []cross(float []a, float []b){
        float []c=new float[3];
        c[0]=a[1]*b[2]-b[1]*a[2];
        c[1]=a[2]*b[0]-b[2]*a[0];
        c[2]=a[0]*b[1]-b[0]*a[1];
        return c;
    }
    public static float[] vec3(float x, float y, float z){
        float []vec=new float[3];
        vec[0]=x;vec[1]=y;vec[2]=z;
        return vec;
    }

}
