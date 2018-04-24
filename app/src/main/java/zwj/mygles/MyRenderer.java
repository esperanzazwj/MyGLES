package zwj.mygles;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zwj.mygles.model.OBJParser;

/**
 * Created by student on 2016/12/16.
 */

public class MyRenderer implements Renderer {
    int program;
    Context context;

    ArrayList<Scene> Scenes=new ArrayList<>();

    Camera camera;
    float []CameraPos = {0.0f,0.0f,20.0f};
    float [] CameraDirection = {0.0f,0.0f,-1.0f};
    float [] gPerspctive = new float[16];
    Light light;

    FBO fbo;

    boolean OnTouched = false;
    float OnTouched_x, OnTouched_y;

    int selected;

    public  MyRenderer(Context context)
    {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            program=Program.getProgram("shader/vShader.glsl","shader/fShader.glsl",context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OBJParser objParser=new OBJParser(context);

        Scenes.add(new Scene(new OBJParser(context).parseOBJ("Gargoyle.obj"),MyVector.vec3(5,0,0), 10.0f));
        Scenes.add(new Scene(new OBJParser(context).parseOBJ("head.obj"),MyVector.vec3(-5,0,0),25.0f));

        camera = new Camera(CameraPos,CameraDirection);
        light = new Light(program);
        fbo = new FBO(1920,1080);

        //设置背景的颜色
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        Matrix.perspectiveM(gPerspctive,0,45,(float)width/height,0.5f,1000f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.2f, 0.2f, 0.2f, 0.2f);
        //touched detect
        if (OnTouched){
            selected=fbo.OnClick(OnTouched_x,OnTouched_y);
            OnTouched=false;
        }

        // 重绘背景色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT);

        //select program
        GLES30.glUseProgram(program);

        //calc Proj
        float gView[] = camera.GetViewMatrix();
        float []gViewPerspective=new float[16];
        Matrix.multiplyMM(gViewPerspective,0,gPerspctive,0, gView,0);

        //set VP matrix
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program,"gViewProjMatrix"),1,false,gViewPerspective,0);

        //set CameraPos
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program,"CameraPos"),1,camera.GetCameraPos(),0);

        //set light attributes
        light.setLightProperties();

        //render
        fbo.glBegin();

        for (int k=0;k<Scenes.size();k++) {

            Scene scene=Scenes.get(k);

            //calc gWorld
            float []gWorld=new float[16];
            Matrix.setIdentityM(gWorld,0);
            Matrix.translateM(gWorld,0,scene.position[0],scene.position[1],scene.position[2]);
            Matrix.scaleM(gWorld,0,scene.size,scene.size,scene.size);
            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program,"gWorld"),1,false,gWorld,0);

            //put index
            int loc_index=GLES30.glGetUniformLocation(program,"index");
            GLES30.glUniform1f(loc_index,(float)(k+1)/255.0f); //1~2, which means that 0 is invalid

            for (int i = 0; i < scene.meshes.size(); i++) {
                scene.meshes.get(i).Render(program);
            }
        }

        fbo.glEnd();
        fbo.glCopyToScreen();

        //unuse
        GLES30.glUseProgram(0);
    }

    public Camera GetCamera()
    {
        return camera;
    }
    public Light GetLight()
    {
        return light;
    }
    public void OnClick(float x, float y)
    {
        OnTouched=true;
        OnTouched_x=x;
        OnTouched_y=1080-y;
    }
    public void OnMoveScene(float dx, float dy)
    {
        float step = 0.01f;
        if (selected==1 || selected==2) //invalid
        {
            Scene scene = Scenes.get(selected-1);
            scene.position[0]+=dx*step;
            scene.position[1]-=dy*step;
        }
    }
}
