package com.example.bruce.opengldemo1.glsurface;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.bruce.opengldemo1.domain.Belt;
import com.example.bruce.opengldemo1.domain.Circle;
import com.example.bruce.opengldemo1.domain.Cube;
import com.example.bruce.opengldemo1.util.Constant;
import com.example.bruce.opengldemo1.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BeltSurfaceView extends GLSurfaceView {
    private SceneRenderer mRenderer;//场景渲染器

    public BeltSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRenderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(mRenderer);                //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    private class SceneRenderer implements Renderer {
//        Belt belt;//立方体
        private Circle circle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            //创建立方体对象
//            belt = new Belt();
            circle = new Circle();
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            Constant.ratio = (float) width / height;
            // 调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 20, 100);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0f, 8f, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            //绘制原立方体
            MatrixState.pushMatrix();
//            belt.drawSelf();
            circle.drawSelf();
            MatrixState.popMatrix();

//            //绘制变换后的立方体
//            MatrixState.pushMatrix();
//            MatrixState.translate(4, 0, 0);//沿x方向平移3
//            MatrixState.rotate(30, 1, 1, 1);
//            MatrixState.scale(0.4f, 0.4f, 0.4f);
//            belt.drawSelf();
//            MatrixState.popMatrix();
        }


    }
}
