package com.example.bruce.opengldemo1.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Update by sunhongzhi on 2016/11/18.
 */

public class ShaderUtil {

    private static final String TAG = "ShaderUtil";

    /**
     * 检查GL错误
     *
     * @param op
     */
    public static void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "GLES20 ERROR: op = " + op + ", error = " + error);
            throw new RuntimeException(op + ": glError = " + error);
        }
    }

    /**
     * 加载着色器；
     *
     * @param shaderType
     * @param source
     * @return
     */
    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {//加载成功;
            GLES20.glShaderSource(shader, source);//加载着色器源码
            GLES20.glCompileShader(shader);//编译

            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//失败
                Log.e(TAG, "Could not compile shader = " + shader + ",error = " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建着色器程序
     *
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program == 0) {
            return 0;
        }

        GLES20.glAttachShader(program, vertexShader);
        checkGLError("glAttachShader");
        GLES20.glAttachShader(program, fragmentShader);
        checkGLError("glAttachShader");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == GLES20.GL_FALSE) {
            Log.e(TAG, "link error = " + GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            return 0;
        }
        return program;
    }
}
