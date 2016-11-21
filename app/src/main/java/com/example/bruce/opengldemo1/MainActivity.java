package com.example.bruce.opengldemo1;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        glSurfaceView = new SixStarSurfaceView(this);
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusable(true);
        setContentView(glSurfaceView);
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
