package com.example.bruce.opengldemo1.domain;

import android.opengl.GLES20;

import com.example.bruce.opengldemo1.util.Constant;
import com.example.bruce.opengldemo1.util.MatrixState;
import com.example.bruce.opengldemo1.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.example.bruce.opengldemo1.util.Constant.UNIT_SIZE;

//颜色矩形
public class Ball {
    ;

    public float yAngle;
    public float xAngle;
    float zAngle;
    float r = 0.8f;
    int muRHandle;
    private int vCount;
    private FloatBuffer mVertexBuffer;
    private int mProgram;
    private int maPositionHandle;
    private int muMVPMatrixHandle;

    public Ball() {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader();
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData() {
        ArrayList<Float> vertixList = new ArrayList<>();
        final int angleSpan = 10;
        for (int vAngle = -90; vAngle < 90; vAngle = vAngle + angleSpan)
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan) {
                float x0 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle)) * Constant.UNIT_SIZE * Math.cos(Math.toRadians(hAngle)));
                float y0 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle)) * Constant.UNIT_SIZE * Math.sin(Math.toRadians(hAngle)));
                float z0 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math.toRadians(vAngle)));

                float x1 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle)) * Constant.UNIT_SIZE * Math.cos(Math.toRadians(hAngle + angleSpan)));
                float y1 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle)) * Constant.UNIT_SIZE * Math.sin(Math.toRadians(hAngle + angleSpan)));
                float z1 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math.toRadians(vAngle)));

                float x2 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan)) * Constant.UNIT_SIZE * Math.cos(Math.toRadians(hAngle + angleSpan)));
                float y2 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan)) * Constant.UNIT_SIZE * Math.sin(Math.toRadians(hAngle + angleSpan)));
                float z2 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math.toRadians(vAngle + angleSpan)));

                float x3 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan)) * Constant.UNIT_SIZE * Math.cos(Math.toRadians(hAngle)));
                float y3 = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(vAngle + angleSpan)) * Constant.UNIT_SIZE * Math.sin(Math.toRadians(hAngle)));
                float z3 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math.toRadians(vAngle + angleSpan)));

                vertixList.add(x1);
                vertixList.add(y1);
                vertixList.add(z1);
                vertixList.add(x3);
                vertixList.add(y3);
                vertixList.add(z3);
                vertixList.add(x0);
                vertixList.add(y0);
                vertixList.add(z0);


                vertixList.add(x1);
                vertixList.add(y1);
                vertixList.add(z1);
                vertixList.add(x2);
                vertixList.add(y2);
                vertixList.add(z2);
                vertixList.add(x3);
                vertixList.add(y3);
                vertixList.add(z3);
            }

        vCount = vertixList.size() / 3;
        float[] vertices = new float[vCount * 3];
        for (int i = 0; i < vertixList.size(); i++) {
            vertices[i] = vertixList.get(i);
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();// 转换为int型缓冲
        mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);// 设置缓冲区起始位置

    }

    //初始化shader
    public void initShader() {
        String vertextSource = "uniform mat4 uMVPMatrix;" + //总变换矩阵
                "attribute vec3 aPosition;" +  //顶点位置
                "varying vec3 vPosition;" +//用于传递给片元着色器的顶点位置
                "void main(){" +
                //根据总变换矩阵计算此次绘制此顶点位置
                "   gl_Position = uMVPMatrix * vec4(aPosition,1);" +
                //将顶点的位置传给片元着色器
                "   vPosition = aPosition;" +//将原始顶点位置传递给片元着色器
                "}";
        String fragmentSource = "precision mediump float;" +
                "uniform float uR;" +
                "varying vec2 mcLongLat;" +//接收从顶点着色器过来的参数
                "varying vec3 vPosition;" +//接收从顶点着色器过来的顶点位置
                "void main(){" +
                "   vec3 color;" +
                "   float n = 8.0;" +//一个坐标分量分的总份数
                "   float span = 2.0*uR/n;" +//每一份的长度
                //每一维在立方体内的行列数
                "   int i = int((vPosition.x + uR)/span);" +
                "   int j = int((vPosition.y + uR)/span);" +
                "   int k = int((vPosition.z + uR)/span);" +
                //计算当点应位于白色块还是黑色块中
                "   int whichColor = int(mod(float(i+j+k),2.0));" +
                "   if(whichColor == 1) {" +//奇数时为红色
                "       color = vec3(0.678,0.231,0.129);" +//红色
                "   }else {" +//偶数时为白色
                "       color = vec3(1.0,1.0,1.0);" +//白色
                "   }" +
                //将计算出的颜色给此片元
                "   gl_FragColor=vec4(color,0);" +
                "}";
        // 基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(vertextSource, fragmentSource);
        // 获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        // 获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中球半径引用
        muRHandle = GLES20.glGetUniformLocation(mProgram, "uR");
    }

    public void drawSelf() {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 1, 0);//绕Y轴转动
        MatrixState.rotate(zAngle, 0, 0, 1);//绕Z轴转动
        // 制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        // 将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
                MatrixState.getFinalMatrix(), 0);
        // 将半径尺寸传入shader程序
        GLES20.glUniform1f(muRHandle, r * UNIT_SIZE);
        // 为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        // 允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        // 绘制球
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
