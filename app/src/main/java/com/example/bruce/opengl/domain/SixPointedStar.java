package com.example.bruce.opengl.domain;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.bruce.opengl.util.MatrixState;
import com.example.bruce.opengl.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Update by sunhongzhi on 2016/11/21.
 */

public class SixPointedStar {

    private static final String TAG = "SixPointedStar";

    static float[] mMMatrix = new float[16];    //具体物体的3D变换矩阵，包括旋转、平移、缩放
    public float yAngle;
    public float xAngle;
    final float UNIT_SIZE = 1;
    private int vCount;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPMatrixHandle;
    private int mProgram;

    public SixPointedStar(float r, float R, float z) {
        super();
        initVertexData(R, r, z);
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
        mProgram = ShaderUtil.createProgram(vertextSource, fragmentSource);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * 初始化顶点数据
     *
     * @param R
     * @param r
     * @param z
     */
    private void initVertexData(float R, float r, float z) {
        List<Float> flist = new ArrayList<Float>();
        float tempAngle = 360 / 5;
        //循环生成构成六角形的定点坐标；
        for (float angle = 0; angle < 360; angle += tempAngle) {
            //第一个点的xyz；
            flist.add(0f);
            flist.add(0f);
            flist.add(z);

            //第二个点的坐标；
            float secX = (float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle)));
            float secY = (float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle)));
            Log.d(TAG, "initVertexData angle = " + angle + ",secX = " + secX + ",secY = " + secY + ",z = " + z);
            flist.add(secX);//x
            flist.add(secY);//y
            flist.add(z);

            //第三个点的坐标；
            float thirdAngle = (angle + tempAngle / 2);
            float thirdX = (float) (r * UNIT_SIZE * Math.cos(Math.toRadians(thirdAngle)));
            float thirdY = (float) (r * UNIT_SIZE * Math.sin(Math.toRadians(thirdAngle)));
            Log.d(TAG, "initVertexData thirdAngle = " + thirdAngle + ",thirdX = " + thirdX + ",thirdY = " + thirdY + ",z = " + z);
            flist.add(thirdX);//x
            flist.add(thirdY);//y
            flist.add(z);

            //第一个中心点的xyz坐标；
            flist.add(0f);
            flist.add(0f);
            flist.add(z);

            //第二个中心点的xyz坐标；
            flist.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle + tempAngle / 2))));//x
            flist.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle + tempAngle / 2))));//y
            flist.add(z);

            //第三个中心点的xyz；
            flist.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle + tempAngle))));//x
            flist.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle + tempAngle))));//y
            flist.add(z);
        }

        vCount = flist.size() / 3;
        //顶点坐标数据；
        float[] vertexArray = new float[flist.size()];
        for (int i = 0; i < vCount; i++) {
            vertexArray[i * 3] = flist.get(i * 3);
            vertexArray[i * 3 + 1] = flist.get(i * 3 + 1);
            vertexArray[i * 3 + 2] = flist.get(i * 3 + 2);
        }

        ByteBuffer vBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vBuffer.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

        Log.d(TAG, "vCount = " + vCount);
        float[] colorArray = new float[vCount * 4];
        for (int i = 0; i < vCount; i++) {
            if (i == 0 || i == 1 || i == 2) {//中心点为白色；
                colorArray[i * 4] = 0;
                colorArray[i * 4 + 1] = 0;
                colorArray[i * 4 + 2] = 1;
                colorArray[i * 4 + 3] = 0;
            } else if (i == 3 || i == 4 || i == 5) {
                colorArray[i * 4] = 1;
                colorArray[i * 4 + 1] = 0;
                colorArray[i * 4 + 2] = 0;
                colorArray[i * 4 + 3] = 0;
            } else {
                colorArray[i * 4] = (float) (0);
                colorArray[i * 4 + 1] = (float) (1);
                colorArray[i * 4 + 2] = (float) (0);
                colorArray[i * 4 + 3] = 0;
            }
        }

        ByteBuffer cBuffer = ByteBuffer.allocateDirect(colorArray.length * 4);
        cBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = cBuffer.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
    }

    public void drawSelf() {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        //设置沿Z轴正向位移1
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        //设置绕y轴旋转
        Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
        //设置绕z轴旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
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
        //绘制六角星
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
