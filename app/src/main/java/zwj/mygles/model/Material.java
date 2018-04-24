package zwj.mygles.model;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class Material {
    String name;
    float[] Ka;
    float[] Kd;
    float[] Ks;
    float alpha;
    float Shininess;
    int illum;
    String DiffuseTextureFile=null;
    String SpecularTextureFile=null;
    Texture DiffuseTexture=null;
    Texture SpecularTexture=null;

    public Material(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmbientColor(float r, float g, float b) {
        Ka = new float[3];
        Ka[0]=r;Ka[1]=g; Ka[2]=b;
    }

    public void setDiffuseColor(float r, float g, float b) {
        Kd = new float[3];
        Kd[0]=r; Kd[1]=g; Kd[2]=b;
    }

    public void setSpecularColor(float r, float g, float b) {
        Ks = new float[3];
        Ks[0]=r;  Ks[1]=g;  Ks[2]=b;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setShine(float shine) {
        this.Shininess = shine;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public void setDiffuseTextureFile(String textureFile) {
        this.DiffuseTextureFile = textureFile;
    }

    public void setSpecularTextureFile(String textureFile) {
        this.SpecularTextureFile = textureFile;
    }

    public void GenOGLTexture(Context context){
        if (DiffuseTextureFile!=null){
            DiffuseTexture=new Texture(context,DiffuseTextureFile);
        }
        if (SpecularTextureFile!=null){
            SpecularTexture=new Texture(context,SpecularTextureFile);
        }
    }
    public void SetMaterial(int program){
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, "Ka"), 1, Ka,0);
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, "Kd"), 1, Kd,0);
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, "Ks"), 1, Ks,0);
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "Shininess"), Shininess);
        //glUniform1f(glGetUniformLocation(program, "Opacity"), Opacity)

        if (DiffuseTexture!=null){
            GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "HasDiffuseTexture"), 1);
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, DiffuseTexture.GetOGLTexture());
            GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "tex"), 0);
        }
        else
            GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "HasDiffuseTexture"), 0);

        /*if (specularTex.size() > 0 && m_Textures.find(specularTex) != m_Textures.end())
        {
            glUniform1i(glGetUniformLocation(program, "HasSpecularTexture"), 1);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, m_Textures[specularTex].OGLTexture);
            glUniform1i(glGetUniformLocation(program, "SpecularTexture"), 1);
        }
        else
            glUniform1i(glGetUniformLocation(program, "HasSpecularTexture"), 0);
        */
    }
}
