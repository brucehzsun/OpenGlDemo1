package com.example.bruce.opengl.domain;

import com.example.bruce.opengl.util.MatrixState;

import static com.example.bruce.opengl.util.Constant.UNIT_SIZE;

//立方体
public class Cube2 {
    //用于绘制各个面的颜色矩形
    ColorRect cr;

    public Cube2() {
        //创建用于绘制各个面的颜色矩形
        cr = new ColorRect();
    }

    public void drawSelf() {
        //总绘制思想：通过把一个颜色矩形旋转移位到立方体每个面的位置
        //绘制立方体的每个面

        //保护现场
        MatrixState.pushMatrix();

        //绘制前小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, UNIT_SIZE);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制后小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -UNIT_SIZE);
        MatrixState.rotate(180, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制上大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, UNIT_SIZE, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制下大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, -UNIT_SIZE, 0);
        MatrixState.rotate(90, 1, 0, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制左大面
        MatrixState.pushMatrix();
        MatrixState.translate(UNIT_SIZE, 0, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        MatrixState.rotate(90, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //绘制右大面
        MatrixState.pushMatrix();
        MatrixState.translate(-UNIT_SIZE, 0, 0);
        MatrixState.rotate(90, 1, 0, 0);
        MatrixState.rotate(-90, 0, 1, 0);
        cr.drawSelf();
        MatrixState.popMatrix();

        //恢复现场
        MatrixState.popMatrix();
    }


}
