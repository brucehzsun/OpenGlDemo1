package com.example.bruce.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.example.bruce.opengl.glsurface.BallSurface;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    private boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        SeekBar sb = (SeekBar) this.findViewById(R.id.SeekBar01);
        sb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        ((BallSurface) glSurfaceView).setLightOffset((seekBar.getMax() / 2.0f - progress) / (seekBar.getMax() / 2.0f) * -4);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

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
