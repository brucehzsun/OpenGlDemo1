package com.example.bruce.opengldemo1.glsurface;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.bruce.opengldemo1.domain.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Update by sunhongzhi on 2016/11/18.
 */

public class TriangleGLSurfaceView extends GLSurfaceView {
    private static final String TAG = "TriangleGLSurfaceView";
    private final SceneRender mRender;
    final float ANGLE_SPAN = 0.375f;

    public TriangleGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRender = new SceneRender();
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void release(){
        mRender.release();
    }


    private class SceneRender implements GLSurfaceView.Renderer {

        private Triangle triangle;
        private RotateThread rotateThread;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0, 0, 0, 1f);
            triangle = new Triangle(TriangleGLSurfaceView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            rotateThread = new RotateThread();
            rotateThread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);//设置视口
            float ratio = (float) width / height;
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            Matrix.setLookAtM(Triangle.mVMatrix,//
                    0,//offset
                    0, 0, 3,//location xyz;
                    0f, 0f, 0f,//观察xyz；
                    0f, 1f, 0f);//up xyz；
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            Log.d(TAG,"onDrawFrame");
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            triangle.drawSelf();
        }

        public void release() {
            rotateThread.flag = false;
        }

        public class RotateThread extends Thread {
            public boolean flag = true;

            @Override
            public void run() {
                super.run();
                while (flag) {
                    mRender.triangle.xAngle = mRender.triangle.xAngle + ANGLE_SPAN;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
