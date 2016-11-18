package com.example.bruce.opengldemo1.domain;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.bruce.opengldemo1.TriangleGLSurfaceView;
import com.example.bruce.opengldemo1.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Update by sunhongzhi on 2016/11/18.
 */

public class Triangle {

    public static float[] mProjMatrix = new float[16];
    public static float[] mVMatrix = new float[16];
    private static float[] mMVPMatrix;
    private static float[] mMMatrix = new float[16];

    private int mProgram;
    private int muMVPMatrixHandle;
    private int maPositionHandle;
    private int maColorHandle;
    private FloatBuffer mVertexBuffer;
    public float xAngle;
    private FloatBuffer mColorBuffer;
    private int vCount;

    public Triangle(TriangleGLSurfaceView glSurfaceView) {
        initVertexData();
        initShader(glSurfaceView);
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);

        float[] finalMatrix = getFinalMatrix(mMMatrix);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, finalMatrix, 0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);

        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    private void initShader(TriangleGLSurfaceView glSurfaceView) {
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
        mProgram = ShaderUtil.createProgram(vertextSource, fragmentSource);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * 初始化顶点的方法；
     */
    private void initVertexData() {
        vCount = 3;//三个顶点；
        final float UNIT_SIZE = 0.2f;//设置单位长度；
        float[] vertices = new float[]{//顶点坐标数据
                -4 * UNIT_SIZE, 0, 0, 0, -4 * UNIT_SIZE, 0, 4 * UNIT_SIZE, 0, 0
        };

        ByteBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());//设置字节顺序为本地操作系统顺序；？
        mVertexBuffer = vertexBuffer.asFloatBuffer();//转为浮点类型；
        mVertexBuffer.put(vertices);//在缓冲区内写入数据；
        mVertexBuffer.position(0);//设置缓冲区起始位置；

        float[] colors = new float[]{
                1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0
        };
        ByteBuffer vertextColorBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        vertextColorBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = vertextColorBuffer.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    /**
     * 产生最终变换矩阵的方法；
     *
     * @param spec
     * @return
     */
    static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
