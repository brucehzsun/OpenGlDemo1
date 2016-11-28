package com.example.bruce.opengl.domain;

import android.opengl.GLES20;
import android.util.Log;

import com.example.bruce.opengl.util.Constant;
import com.example.bruce.opengl.util.MatrixState;
import com.example.bruce.opengl.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Update by sunhongzhi on 2016/11/24.
 */

public class Belt {
    private static final String TAG = "Belt";
    private int vCount;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private int mProgram;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPMatrixHandle;

    public Belt() {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader();
    }

    private void initShader() {
        String vertextSource = "uniform mat4 uMVPMatrix;" +
                "attribute vec3 aPosition;" +
                "attribute vec4 aColor;" +
                "varying vec4 vColor;" +
                "void main(){" +
                "   gl_Position = uMVPMatrix * vec4(aPosition,1);" +
                "   vColor = aColor;" +
                "}";
        String fragmentSource = "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main(){" +
                "   gl_FragColor = vColor;" +
                "}";
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(vertextSource, fragmentSource);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    private void initVertexData() {
        int n = 60;
        vCount = 2 * (n + 1);//顶点个数；

        float angdegBegin = -90;
        float angdegEnd = 90;
        float angdegSpan = (angdegEnd - angdegBegin) / n;
        float[] vertices = new float[vCount * 3];//定点坐标数组；
        int count = 0;
        for (float angdeg = angdegBegin; angdeg <= angdegEnd; angdeg += angdegSpan) {
            Log.d(TAG,"for angdeg = "+angdeg);
            double angrad = Math.toRadians(angdeg);//当前弧度；
            vertices[count++] = (float) (-0.6f * Constant.UNIT_SIZE * Math.sin(angrad));//定点x坐标；
            vertices[count++] = (float) (0.6f * Constant.UNIT_SIZE * Math.cos(angrad));//顶点y坐标;
            vertices[count++] = 0;

            vertices[count++] = (float) (-Constant.UNIT_SIZE * Math.sin(angrad));//x
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.cos(angrad));//y
            vertices[count++] = 0;
        }

        ByteBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexBuffer.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        count = 0;
        float[] colors = new float[vCount * 4];
        for (int i = 0; i < colors.length; i += 8) {
            if(i == 0){
                colors[count++] = 0;//r
                colors[count++] = 0;//g
                colors[count++] = 1;//b
                colors[count++] = 0;//a

                colors[count++] = 0;//r
                colors[count++] = 0;//g
                colors[count++] = 1;//b
                colors[count++] = 0;//a

            }else if(i==8){
                colors[count++] = 0;//r
                colors[count++] = 0;//g
                colors[count++] = 1;//b
                colors[count++] = 0;//a

                colors[count++] = 1;//r
                colors[count++] = 0;//g
                colors[count++] = 0;//b
                colors[count++] = 0;//a
            } else {

                colors[count++] = 0;//r
                colors[count++] = 1;//g
                colors[count++] = 0;//b
                colors[count++] = 0;//a

                colors[count++] = 1;//r
                colors[count++] = 0;//g
                colors[count++] = 0;//b
                colors[count++] = 0;//a
            }

        }
        ByteBuffer colorBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = colorBuffer.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void drawSelf() {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //为画笔指定顶点着色数据
        GLES20.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES20.GL_FLOAT,
                        false,
                        4 * 4,
                        mColorBuffer
                );
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);
        //绘制立方体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount);
    }
}
