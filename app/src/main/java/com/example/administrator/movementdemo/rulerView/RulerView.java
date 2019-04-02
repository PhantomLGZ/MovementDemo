package com.example.administrator.movementdemo.rulerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.OverScroller;

public class RulerView extends View {

    private OverScroller overScroller = new OverScroller(getContext());
    private Paint paint = new Paint();
    private Paint linePaint = new Paint();
    private Paint minLinePaint = new Paint();
    private Paint textPaintForLine = new Paint();
    private int centerX;
    private int halfOfX;
    private int myHeight = 300;
    private DisplayMetrics metrics = new DisplayMetrics();
    private VelocityTracker velocityTracker;

    private boolean isStart = false;

    private int num = 555;
    private int minNum = 400;

    private int minCenterX;

    private float downX = 0;
    private float moveX = 0;
    private float oldMoveX = 0;
    private float upX = 0;

    private int lastCurrX = 0;

    {
        linePaint.setColor(0xffe5e9e7);
        linePaint.setStrokeWidth(10);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        minLinePaint.setColor(0xffe5e9e7);
        minLinePaint.setStrokeWidth(5);
        minLinePaint.setStrokeCap(Paint.Cap.ROUND);
        textPaintForLine.setColor(0xff333333);
        textPaintForLine.setTextSize(40);
    }

    {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        centerX = metrics.widthPixels/2;
//        Log.e("centerX = ", ""+centerX);
        minCenterX = centerX - (num - minNum);
        halfOfX = centerX;
    }

    public RulerView(Context context) {
        super(context);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), myHeight);
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()){
            /*
            原本应该用scrollTo 但是这里图方便复用了自己写的myScrollBy来处理边界问题
            所以需要每次计算差值 而第一次会因为上次的残留值或默认初始化为0 导致第一次差值有问题
            需要在开始判断是否是本次fling第一次获取X值 如果是就将差值设为0 牺牲第一次变化
            */
            if (!isStart){
                lastCurrX = overScroller.getCurrX()/10;
                isStart = true;
            }
//            Log.e("overScroller.getCurrX()/10-lastCurrX =", ""+(overScroller.getCurrX()/10 - lastCurrX));
            myScrollBy(overScroller.getCurrX()/10 - lastCurrX);
            lastCurrX = overScroller.getCurrX()/10;
        }else {
//            Log.e("isStart = ", "false");
            isStart = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        上面有一个RulerLayout 本来想在上面设置点击事件的 但是最后在这里实现了
        所以需要两次调用getParent()
        */
        getParent().requestDisallowInterceptTouchEvent(true);
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.e("action", "down");
                if (!overScroller.isFinished()) {
                    overScroller.abortAnimation();
                }
                downX = event.getX();
                oldMoveX = downX;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("action", "move"+centerX);
                moveX = event.getX();
                myScrollBy(Math.round((oldMoveX-moveX)/10));
                oldMoveX = moveX;
                break;
            case MotionEvent.ACTION_UP:
//                Log.e("action", "up");
                upX = event.getX();
                velocityTracker.computeCurrentVelocity(1000);
                double vx = velocityTracker.getXVelocity();
                if(Math.abs(vx) > 100){
//                    Log.e("fling ","start");
                    fling(-vx);
                }
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return true;
    }

    private void myScrollBy(int change){

        scrollBy(centerX+change<minCenterX?minCenterX-centerX:change, 0);
        centerX += change;
        if (centerX < minCenterX){
            centerX = minCenterX;
        }
        num += change;
        if (num < minNum){
            num = minNum;
        }
        invalidate();
    }

    private void fling(double vx){
        overScroller.fling(0, 0, (int) Math.round(vx), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(0xfff5faf7);
        canvas.save();
        canvas.translate(centerX,15);
        canvas.drawRect(-halfOfX,-15, halfOfX, myHeight-15, paint);

        int last = 0;
        int last2 = 0;
        for(int i = -halfOfX; i < halfOfX; i++){
            int temp = i/2;
            if((temp/10+num) >= minNum){
                if ((temp/10+num)%10 == 0) {
                    if (last != ((temp/10 + num) / 10)) {
                        last = (temp/10 + num) / 10;
                        canvas.drawLine(i, -5, i, myHeight / 3, linePaint);
                        String tempString = "" + last;
                        canvas.drawText(tempString, i - textPaintForLine.measureText(tempString) / 2, myHeight / 2, textPaintForLine);
                    }
                }else if (last2 != (temp/10+num)){
                    last2 = (temp/10+num);
                    canvas.drawLine(i, -10, i, myHeight/5, minLinePaint);
                }
            }
        }
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(0xff19be7d);
        paint.setStrokeWidth(15);
        paint.setTextSize(80);
        canvas.drawLine(-15,0, -15,myHeight/2, paint);
        String result = num/10+"."+num%10;
        canvas.drawText(result, -paint.measureText(result) / 2, myHeight/6*5, paint);
        canvas.restore();
    }
}
