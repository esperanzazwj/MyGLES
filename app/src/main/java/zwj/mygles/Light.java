package zwj.mygles;

import android.opengl.GLES30;

import static zwj.mygles.MyVector.vec3;

/**
 * Created by student on 2016/12/18.
 */

public class Light {
    private int NumLights;
    private float []ambientColor;
    private  int program;

    public Light(int program){
        this.program=program;
        initLights();
        findLocation();
    }

    private int loc_numLights;
    private  int loc_ambientColor;
    private  int []loc_color ;
    private  int []loc_position;

    class LightProperties
    {
        public float []color;
        public float []position;
    }

    private LightProperties []Lights;

    public void initLights() {
        NumLights = 2;
        ambientColor = vec3(0.1f,0.1f,0.1f);
        Lights = new LightProperties[NumLights];
        loc_color = new int[NumLights];
        loc_position = new int[NumLights];

        Lights[0]=new LightProperties();
        Lights[0].color = vec3(1.0f, 0.5f, 0.25f);
        Lights[0].position = vec3(0.0f, 10.0f, 10.0f);

        Lights[1]=new LightProperties();
        Lights[1].color = vec3(0.2f, 0.3f, 1.0f);
        Lights[1].position = vec3(0.0f, -10.0f, 10.0f);
    }
    void findLocation()
    {
        loc_numLights=GLES30.glGetUniformLocation(program, "numLights");
        loc_ambientColor=GLES30.glGetUniformLocation(program, "ambientColor");
        for (int i=0;i<NumLights;i++)
        {
            String str;
            str=String.format("Lights[%d].color", i);
            loc_color[i] = GLES30.glGetUniformLocation(program, str);
            str=String.format("Lights[%d].position", i);
            loc_position[i] = GLES30.glGetUniformLocation(program, str);
        }
    }
    void setLightProperties()
    {

        GLES30.glUniform1i(loc_numLights, NumLights);
        GLES30.glUniform3fv(loc_ambientColor, 1,ambientColor,0);
        for (int i=0;i<NumLights;i++)
        {
            GLES30.glUniform3fv(loc_color[i],1, Lights[i].color,0);
            GLES30.glUniform3fv(loc_position[i],1, Lights[i].position,0);
        }

    }
    public void Move(float x, float y)
    {
        //y front & back
        for (int i=0;i<NumLights;i++)
        {
            if (x>0)
            {
                Lights[i].position[0]+=0.05;
            }
            else
            {
                Lights[i].position[0]-=0.05;
            }
            if (y>0)
            {
                Lights[i].position[1]+=0.05;
            }
            else
            {
                Lights[i].position[1]-=0.04;
            }
        }
    }


}
