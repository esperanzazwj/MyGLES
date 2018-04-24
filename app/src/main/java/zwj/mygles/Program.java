package zwj.mygles;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by student on 2016/12/16.
 */

public class Program {

    static public int LoadShaderFromAsset(int type, String shaderName, Context context) throws IOException {
        InputStream is=null;
        is = context.getAssets().open(shaderName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String content = new String(buffer, "GB2312");
        return LoadShader(type,content);
    }

    static public int LoadShader (int type, String shaderCode){

        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        int []compiled=new int[1];
        GLES30.glGetShaderiv ( shader, GLES30.GL_COMPILE_STATUS, compiled,0);

        if (compiled[0]==0)
        {
            String infoLog=GLES30.glGetShaderInfoLog(shader);
            Log.e("Shader Compiling Error", infoLog);
            GLES30.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    static int getProgram(String vShader, String fShader, Context context) throws IOException{
        int vs=LoadShaderFromAsset(GLES30.GL_VERTEX_SHADER,vShader,context);
        int fs=LoadShaderFromAsset(GLES30.GL_FRAGMENT_SHADER,fShader,context);
        int program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vs);
        GLES30.glAttachShader(program, fs);
        GLES30.glLinkProgram(program);

        int []linked=new int[1];
        GLES30.glGetProgramiv(program,GLES30.GL_LINK_STATUS,linked,0);
        if (linked[0]!=GLES30.GL_TRUE)
        {
            String infoLog=GLES30.glGetProgramInfoLog(program);
            Log.e("Program linking Error", infoLog);
            GLES30.glDeleteProgram(program);
            return 0;
        }
        return program;
    }
}
