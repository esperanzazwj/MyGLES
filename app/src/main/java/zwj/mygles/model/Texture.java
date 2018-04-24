package zwj.mygles.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import zwj.mygles.R;

/**
 * Created by student on 2016/12/4.
 */

public class Texture {

    int width,height;
    int []pixels=null;
    ByteBuffer pixelsBuffer=null;

    int []OGLTexture;

    public Texture(Context context, String fileName){

        Bitmap bmp=null;

        if (fileName.equals("teapot.png")) {
            Resources res = context.getResources();
            bmp = BitmapFactory.decodeResource(res,
                    R.drawable.teapot);
        }
        else if (fileName.equals("texture.bmp")) {
            Resources res = context.getResources();
            bmp = BitmapFactory.decodeResource(res,
                    R.drawable.texture);
        }
        else if (fileName.equals("lambertian.png")) {
            Resources res = context.getResources();
            bmp = BitmapFactory.decodeResource(res,
                    R.drawable.lambertian);
        }
        width = bmp.getWidth();
        height = bmp.getHeight();
        pixels=new int[width*height];
        bmp.getPixels(pixels,0,width,0,0,width,height);

        pixelsBuffer = ByteBuffer.allocateDirect(width*height*3);
        pixelsBuffer.order(ByteOrder.nativeOrder());
        for(int i=0; i<width*height; i++){
            pixelsBuffer.put((byte)Color.red(pixels[i]));
            pixelsBuffer.put((byte)Color.green(pixels[i]));
            pixelsBuffer.put((byte)Color.blue(pixels[i]));
        }
        pixelsBuffer.position(0);

        GenOGLTexture();
    }

    private void GenOGLTexture(){
        if (pixelsBuffer!=null) {
            OGLTexture = new int[1];
            GLES30.glGenTextures(1, OGLTexture, 0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, OGLTexture[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, width, height, 0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE, pixelsBuffer);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        }
    }

    public int GetOGLTexture(){
        return OGLTexture[0];
    }
}
