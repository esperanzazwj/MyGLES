package zwj.mygles;

import android.opengl.Matrix;

/**
 * Created by student on 2016/12/17.
 */

public class Camera {

    static final float step = 0.01f;
    float []CameraPos;
    float []Direction;
    float []Up = {0.0f,1.0f,0.0f};

    public Camera(float []cameraPos, float []direction)
    {
        CameraPos = cameraPos;
        Direction = direction;
    }
    public float[] GetViewMatrix() {
        float []center=new float[3];
        MyVector.add(center,CameraPos,Direction);
        float []mat=new float[16];
        Matrix.setLookAtM(mat,0,CameraPos[0], CameraPos[1],CameraPos[2],center[0],center[1],center[2],Up[0],Up[1],Up[2]);
        return mat;
    }
    public float[] GetCameraPos() {
        return CameraPos;
    }
    public void Move(float x, float y)
    {
        //y front & back
        float []Direction_norm= MyVector.normalize(Direction);
        MyVector.mul(Direction_norm,Direction_norm,step*(y>0?1:-1));
        MyVector.add(CameraPos,CameraPos,Direction_norm);
        //x left & right
        float []MoveLeft = MyVector.normalize(MyVector.cross(Up,Direction_norm));
        MyVector.mul(MoveLeft,MoveLeft,step*(x>0?1:-1));
        MyVector.add(CameraPos,CameraPos,MoveLeft);
    }

}
