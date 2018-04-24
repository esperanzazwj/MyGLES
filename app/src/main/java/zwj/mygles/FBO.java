package zwj.mygles;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by student on 2016/12/18.
 */

public class FBO {
    int width;
    int height;

    int []mFBO=new int[1];
    int []colorTextures=new int[2];
    int []depthTexture=new int[1];

    public FBO(int width, int height)
    {
        this.width=width;
        this.height=height;
        GLES30.glGenFramebuffers(1,mFBO,0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,mFBO[0]);

        GLES30.glGenTextures(2,colorTextures,0);
        for (int i=0;i<2;i++) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, colorTextures[i]);

            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, width, height,
                    0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

            GLES30.glFramebufferTexture2D(GLES30.GL_DRAW_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0+i, GLES30.GL_TEXTURE_2D,
                    colorTextures[i], 0);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        }
        int []mrt=new int[2];
        mrt[0]=GLES30.GL_COLOR_ATTACHMENT0;
        mrt[1]=GLES30.GL_COLOR_ATTACHMENT1;
        GLES30.glDrawBuffers(2,mrt,0);
        GLES30.glGenRenderbuffers(1, depthTexture, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, depthTexture[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16,
                width, height);
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_RENDERBUFFER, depthTexture[0]);

        int Status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
        if (Status != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            Log.e("FB error", String.format("status: 0x%x\n", Status));
        }

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0);
    }
    void glBegin()
    {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,mFBO[0]);
        GLES30.glViewport(0,0, width,height);
        GLES30.glClear( GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT);
    }
    void glEnd()
    {
        GLES30.glBindFramebuffer( GLES30.GL_DRAW_FRAMEBUFFER,0);
    }
    void glCopyToScreen()
    {
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER,mFBO[0]);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER,0);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT0);
        GLES30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_LINEAR);
        GLES30.glReadBuffer(GLES30.GL_NONE);
    }
    public int OnClick(float x, float y)
    {
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mFBO[0]);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT1);

        ByteBuffer RGBABuffer = ByteBuffer.allocate(4);
        RGBABuffer.position(0);

        GLES30.glReadPixels((int)x, (int)y, 1, 1, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, RGBABuffer);

        byte []data=RGBABuffer.array();

        GLES30.glReadBuffer(GLES30.GL_NONE);
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);

        return data[0];
    }
}
