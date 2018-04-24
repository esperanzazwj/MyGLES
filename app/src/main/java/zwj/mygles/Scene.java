package zwj.mygles;

import java.util.Vector;

/**
 * Created by student on 2016/12/18.
 */

public class Scene {
    public Vector<Mesh> meshes;
    public float []position=new float[3];
    public float size;

    public Scene(Vector<Mesh> meshes, float[]position, float size)
    {
        this.meshes=meshes;
        this.position=position;
        this.size=size;
    }
}
