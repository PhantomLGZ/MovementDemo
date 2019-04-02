package com.example.administrator.movementdemo.moveView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class StarModel {

    private PointF pointF;
    private float veloX;
    private float veloY;
    private int color;

    private static PointF oldPointF;

    private static int oldColor = 0xffffffff;

    public StarModel(PointF pointF, float veloX, float veloY) {
        this.pointF = pointF;
        this.veloX = veloX;
        this.veloY = veloY;
        color = oldColor;
        this.oldPointF = new PointF(pointF.x, pointF.y);
    }

    private void reset(){
        pointF = new PointF(oldPointF.x, oldPointF.y);
        color = oldColor;
    }

    public void reflash(){
        pointF.x += veloX;
        pointF.y += veloY;
        color -= 0x04000000;
        if (((color >> 24) & 0xff) <= 0x04){
//            Log.e("Color",color+"");
            reset();
        }
    }

    public void draw(Canvas canvas, int limitColor){
        Paint paint = new Paint();
        paint.setColor((color>>>24)<(limitColor>>>24)?color:limitColor);
        canvas.drawCircle(pointF.x, pointF.y, 8, paint);
    }
}
