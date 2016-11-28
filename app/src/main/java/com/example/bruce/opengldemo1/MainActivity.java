package com.example.bruce.opengldemo1;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.bruce.opengldemo1.glsurface.BallSurface;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    private boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.activity_main);

        glSurfaceView = new BallSurface(this);
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusable(true);
        rootLayout.addView(glSurfaceView);

//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isClick) {
//                    isClick = false;
//                    MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 20, 100);
//                    MatrixState.setCamera(0, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//                } else {
//                    isClick = true;
//                    MatrixState.setProjectFrustum(-Constant.ratio * 0.7f, Constant.ratio * 0.7f, -0.7f, 0.7f, 1, 10);
//                    MatrixState.setCamera(0, 0.5f, 4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        glSurfaceView.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
