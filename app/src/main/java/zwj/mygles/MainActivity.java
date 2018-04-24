package zwj.mygles;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static zwj.mygles.R.id.MyGLSurfaceView;
import static zwj.mygles.R.id.rockerView1;
import static zwj.mygles.R.id.rockerView2;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;
    private RockerView rockerView1;
    private RockerView rockerView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window= MainActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

        setContentView(R.layout.activity_main);
        mGLView = (MyGLSurfaceView)findViewById(R.id.MyGLSurfaceView);
        rockerView1 = (RockerView) findViewById(R.id.rockerView1);
        rockerView2 = (RockerView) findViewById(R.id.rockerView2);

        rockerView1.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                mGLView.GetRenderer().GetCamera().Move(x,y);

            }

        });
        rockerView2.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                mGLView.GetRenderer().GetLight().Move(x,y);

            }

        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mGLView.onPause();
    }

}
