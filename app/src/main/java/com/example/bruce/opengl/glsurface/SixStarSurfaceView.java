package com.example.bruce.opengl.glsurface;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.example.bruce.opengl.domain.SixPointedStar;
import com.example.bruce.opengl.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Update by sunhongzhi on 2016/11/21.
 */

public class SixStarSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final SceneRenderer mRender;
    private float mPreviousX;
    private float mPrevieusY;

    public SixStarSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRender = new SceneRenderer();
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPrevieusY;
                float dx = x - mPreviousX;
                for (SixPointedStar star : mRender.startList) {
                    star.yAngle += dx * TOUCH_SCALE_FACTOR;
                    star.xAngle += dy * TOUCH_SCALE_FACTOR;
                }
                break;
        }
        mPrevieusY = event.getY();
        mPreviousX = event.getX();
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        SixPointedStar[] startList = new SixPointedStar[30];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);//设置屏幕背景色；
            for (int i = 0; i < startList.length; i++) {
                startList[i] = new SixPointedStar(0.2f, 0.5f, -1f * i);
            }
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            //设置正交投影:近平面的宽高比 与 视窗的宽高比相等;
//            MatrixState.setPojectOrtho(-ratio, ratio, -1, 2, 1, 10);

            MatrixState.setProjectFrustum(-ratio*0.4f, ratio*0.4f, -1*0.4f, 1*0.4f, 1, 50);
            //设置摄像机
            MatrixState.setCamera(0, 0, 3f, 0, 0, 0f, 0f, 1.0f, 0.0f);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            for (SixPointedStar start : startList) {
                start.drawSelf();
            }
        }
    }
}
