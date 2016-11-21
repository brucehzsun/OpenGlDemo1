package com.example.bruce.opengldemo1.util;

import android.opengl.Matrix;

/**
 * Update by sunhongzhi on 2016/11/21.
 */

public class MatrixState {
    private static float[] mProjectMatrix = new float[16];//4*4矩阵
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵；
    private static float[] mMVPMatrix;//最终的总变换矩阵；

    /**
     * @param cx  //摄像机位置的xyz参数；
     * @param cy  //摄像机位置的xyz参数；
     * @param cz  //摄像机位置的xyz参数；
     * @param tx  //观察目标的xyz参数
     * @param ty  //观察目标的xyz参数
     * @param tz  //观察目标的xyz参数
     * @param upx //up向量 xyz；
     * @param upy //up向量 xyz；
     * @param upz //up向量 xyz；
     */
    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    /**
     * 设置正交投影的方法
     *
     * @param left   near 的left
     * @param right  near 的right
     * @param bottom near 的bottom
     * @param top    near 的top
     * @param near   near面与视点的距离
     * @param far    far面与视点的距离
     */
    public static void setPojectOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjectMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 生成物体总变换矩阵的方法
     *
     * @param spec
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        //将摄影机矩阵乘以最终变换矩阵；
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);

        //将投影矩阵乘以上一步的结果矩阵得到最终变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjectMatrix, 0, left, right, bottom, top, near, far);
    }
}
