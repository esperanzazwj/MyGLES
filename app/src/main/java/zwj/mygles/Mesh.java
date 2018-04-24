package zwj.mygles;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

import zwj.mygles.model.Material;

/**
 * Created by student on 2016/12/16.
 */

public class Mesh {

    Material material;

    int []VAO=new int[1];
    int []VBO = new int [1];
    int texture;
    int texLoc;

    int count=0;
    public Mesh(Vector<Integer> vPointer, Vector<Integer> vtPointer,
                Vector<Integer> vnPointer, Material material,
                Vector<Float> v, Vector<Float> vt, Vector<Float> vn){
        this.material=material;
        float []vertices=new float[vPointer.size()*3];
        float []texCoords=new float[vtPointer.size()*2];
        float []normals=new float[vnPointer.size()*3];

        for(int i=0; i<vPointer.size(); i++) {
            vertices[i*3]=v.get(vPointer.get(i) * 3);
            vertices[i*3+1]=v.get(vPointer.get(i) * 3+1);
            vertices[i*3+2]=v.get(vPointer.get(i) * 3+2);
        }
        for(int i=0; i<vtPointer.size(); i++) {
            texCoords[i*2]=vt.get(vtPointer.get(i) * 2);
            texCoords[i*2+1]=vt.get(vtPointer.get(i) * 2+1);
        }
        for(int i=0; i<vnPointer.size(); i++) {
            normals[i*3]=vn.get(vnPointer.get(i) * 3);
            normals[i*3+1]=vn.get(vnPointer.get(i) * 3+1);
            normals[i*3+2]=vn.get(vnPointer.get(i) * 3+2);
        }

        initGPUData(vertices,texCoords,normals);
    }
    public void initGPUData(float []vertices,float []texCoords,float []normals)
    {
        count=vertices.length/3;

        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glBindVertexArray(VAO[0]);

        GLES30.glGenBuffers(1, VBO,0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer;
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(texCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer texBuffer;
        texBuffer = bb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

        bb = ByteBuffer.allocateDirect(normals.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer normalBuffer;
        normalBuffer = bb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);


        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, (vertices.length+texCoords.length+normals.length)*4, null, GLES30.GL_STATIC_DRAW);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, vertices.length*4, vertexBuffer);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, vertices.length*4, texCoords.length*4, texBuffer);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, (vertices.length+texCoords.length)*4, normals.length*4, normalBuffer);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);//pointer 0
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, vertices.length*4);// pointer 1
        GLES30.glVertexAttribPointer(2, 3, GLES30.GL_FLOAT, false, 0, (vertices.length+texCoords.length)*4);// pointer 2

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glEnableVertexAttribArray(2);
        GLES30.glBindVertexArray(0);



    }
    public void Render(int program)
    {
        if (material!=null) material.SetMaterial(program);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, count);
        GLES30.glBindVertexArray(0);
    }
}
