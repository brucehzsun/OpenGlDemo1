package com.example.bruce.opengldemo1.domain;

import android.opengl.GLES20;

import com.example.bruce.opengldemo1.util.Constant;
import com.example.bruce.opengldemo1.util.MatrixState;
import com.example.bruce.opengldemo1.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Update by sunhongzhi on 2016/11/24.
 */

public class Circle {

    private int vCount;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private int mProgram;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPMatrixHandle;
    private int iCount;
    private ByteBuffer mIndexBuffer;

    public Circle() {
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
        int n = 10;
        vCount = n + 2;
        float angdegSpan = 360.0f / n;
        float[] vertices = new float[vCount * 3];

        int count = 0;
        vertices[count++] = 0;
        vertices[count++] = 0;
        vertices[count++] = 0;
        for (float angdeg = 0; Math.ceil(angdeg) <= 360; angdeg += angdegSpan) {
            double angrad = Math.toRadians(angdeg);
            vertices[count++] = (float) (-Constant.UNIT_SIZE * Math.sin(angrad));
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.cos(angrad));
            vertices[count++] = 0;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        iCount = vCount;
        byte[] indexs = new byte[iCount];
        for (int i = 0; i < iCount; i++) {
            indexs[i] = (byte) i;
        }
        mIndexBuffer = ByteBuffer.allocateDirect(indexs.length);
        mIndexBuffer.put(indexs);
        mIndexBuffer.position(0);

        count = 0;
        float[] colors = new float[vCount * 4];
        colors[count++] = 0;
        colors[count++] = 1;
        colors[count++] = 0;
        colors[count++] = 0;

        for (int i = 4; i < colors.length; i += 4) {

            colors[count++] = 0;
            colors[count++] = 0;
            colors[count++] = 1;
            colors[count++] = 0;
        }

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
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
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, iCount, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
