package zwj.mygles;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by student on 2016/12/16.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    float px,py;

    // OpenGL ES 3.0 context
    private final int CONTEXT_CLIENT_VERSION = 3;
    Context context;
    MyRenderer mRenderer;
    public MyGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
        mRenderer=new MyRenderer(context);
        setRenderer(mRenderer);
    }
    public MyGLSurfaceView(Context context){
        super(context);
        this.context = context;
        mRenderer = new MyRenderer(context);
        setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);

        //设置Renderer到GLSurfaceView
        setRenderer(mRenderer);
        // 只有在绘制数据改变时才绘制view
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mRenderer.OnClick(x,y);
                px=x;py=y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - py;
                float dx = x - px;
                mRenderer.OnMoveScene(dx,dy);
                px=x;py=y;
                break;
            case MotionEvent.ACTION_UP:
                px=0;py=0;
                break;

        }
        return true;
    }

    public MyRenderer GetRenderer()
    {
        return mRenderer;
    }
}
