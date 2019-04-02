package com.example.administrator.movementdemo.moveView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.administrator.movementdemo.R;
import com.example.administrator.movementdemo.Utils;

public class MoveView extends View {

    private boolean isFirst = true;
    private boolean isAnimeEnd =true;
    private boolean isThreadRun = false;
    //初始化
    private int count = 0;
    private int addCount = 1;
    private AnimationThread animationThread = new AnimationThread();;
    private DisplayMetrics metrics = new DisplayMetrics();
    private Paint paint =new Paint();
    int actionBarHeight = 0;
    int oldCircleAlpha = 0xaa;
    int circleColor1 = 0x22ffffff;
    int lineColor = 0xffffffff;

    //anime
    int lineDegrees = 0;
    int circleDegrees = 0;
    int circleAlpha = oldCircleAlpha;
    int changeInZ = 0;

    {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);

        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, this.getResources().getDisplayMetrics());
        }
    }

    private float centerX = metrics.widthPixels/2;
    private float centerY = (metrics.heightPixels-Utils.dpToPixel(48)-actionBarHeight)/2;
    private float radius = Math.min(centerX/3*2, centerY/3*2);

    //喷射粒子
    private PointF startPoint = new PointF(centerX + radius, centerY);
    private StarModel[] starModels = new StarModel[64];

    public void startAnima(){
        isAnimeEnd = false;
        usuallyAnime.cancel();
        circleColor1 = 0x00ffffff;
        lineColor = 0x00ffffff;
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "lineDegrees", 0, 360);
        animator.setDuration(2000);
        animator.setRepeatCount(1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isThreadRun =true;
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isThreadRun = false;
                super.onAnimationEnd(animation);
            }
        });

        ObjectAnimator alphaAnimator1 = ObjectAnimator.ofInt(this, "circleAlpha", oldCircleAlpha, 0);
        alphaAnimator1.setDuration(10);

        ObjectAnimator alphaAnimator2 = ObjectAnimator.ofInt(this, "circleAlpha", 0, oldCircleAlpha);
        alphaAnimator2.setDuration(10);

        ObjectAnimator zAnimator = ObjectAnimator.ofInt(this, "changeInZ", 0, -50);
        zAnimator.setDuration(500);
        zAnimator.setInterpolator(new CycleInterpolator(0.5f));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator).after(alphaAnimator1).before(alphaAnimator2).before(zAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                usuallyAnime();
            }
        });
        animatorSet.start();
    }

    ObjectAnimator usuallyAnime;

    public void usuallyAnime(){
        isAnimeEnd = true;
        circleColor1 = 0x22ffffff;
        lineColor = 0xffffffff;
        usuallyAnime = ObjectAnimator.ofInt(this, "circleDegrees", 0, 360);
        usuallyAnime.setInterpolator(new LinearInterpolator());
        usuallyAnime.setDuration(10000);
        usuallyAnime.setRepeatCount(ValueAnimator.INFINITE);
        usuallyAnime.start();
    }

    public boolean isAnimeEnd(){
        return isAnimeEnd;
    }

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLineDegrees(int lineDegrees) {
        this.lineDegrees = lineDegrees;
        invalidate();
    }

    public void setCircleDegrees(int circleDegrees) {
        this.circleDegrees = circleDegrees;
        invalidate();
    }

    public void setCircleAlpha(int circleAlpha) {
        this.circleAlpha = circleAlpha;
        invalidate();
    }


    public void setChangeInZ(int changeInZ) {
        this.changeInZ = changeInZ;
        invalidate();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
    int lineNum = 8;
    int halfLineNum = lineNum/2;
    float changeRandomTop[] = new float[lineNum];
    float changeRandomLift[] = new float[lineNum];
    float changeRandomBottem[] = new float[lineNum];
    {
        for (int i = 0; i < lineNum; i++){
            changeRandomTop[i] = (float) (Math.random()*(i-halfLineNum)*3);
            changeRandomLift[i] = (float) (Math.random()*(i-halfLineNum)*5);
            changeRandomBottem[i] = (float) (Math.random()*(i-halfLineNum)*3);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirst){
            usuallyAnime();
            animationThread.start();
            isFirst = false;
        }
        super.onDraw(canvas);

        Camera camera = new Camera();

        //圆环
        paint.reset();
        canvas.save();
        canvas.translate(centerX, centerY);
        int color = (circleAlpha<<24)+0xffffff;
        paint.setAlpha(circleAlpha);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        int circleStrokeWidth = 40;
        paint.setStrokeWidth(circleStrokeWidth);
        camera.save();
        camera.translate(0, 0, changeInZ*2);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();

        //内环
        paint.reset();
        paint.setColor(lineColor);
        canvas.save();
        canvas.translate(centerX, centerY);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        Path inner1 = new Path();
        float newRadius = radius-circleStrokeWidth;
        inner1.addArc(-newRadius, -newRadius, newRadius, newRadius, -90,270);
        canvas.drawPath(inner1, paint);
        Path point = new Path();
        point.addOval(-3,-3,3,3, Path.Direction.CW);
        PathEffect pathEffect = new PathDashPathEffect(point, 12, 0, PathDashPathEffect.Style.ROTATE);
        paint.setPathEffect(pathEffect);
        Path inner2 = new Path();
        inner2.addArc(-newRadius, -newRadius, newRadius, newRadius, 180,90);
        canvas.drawPath(inner2, paint);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(-newRadius, 0, 10, paint);
        canvas.restore();

        //光晕
        paint.reset();
        canvas.save();
        canvas.translate(centerX, centerY);
        Shader circleShader = new LinearGradient(0, -radius-circleStrokeWidth/2, 0, radius+circleStrokeWidth/2, 0x00ffffff, circleColor1, Shader.TileMode.CLAMP);
        paint.setShader(circleShader);
        Path circlePath = new Path();
        circlePath.addCircle(0, 0, radius-circleStrokeWidth/2, Path.Direction.CCW);
        circlePath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.clipPath(circlePath);
        canvas.rotate(circleDegrees);
        for(int i = 0; i < 4; i ++){
            canvas.drawOval(-radius-circleStrokeWidth/2-i*3, -radius-circleStrokeWidth/2, +radius+circleStrokeWidth/2+i*3, +radius+circleStrokeWidth/2+i*10, paint);
        }
        canvas.restore();

        //文字和表
        paint.reset();
        canvas.save();
        canvas.translate(centerX, centerY);
        camera.save();
        camera.translate(0, 0, changeInZ);
        camera.applyToCanvas(canvas);
        camera.restore();
        paint.setColor(0xffffffff);
        paint.setTextSize(200);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("2274", 0, 0, paint);
        paint.setTextSize(40);
        float textSpacing = paint.getFontSpacing();
        canvas.drawText("1.5公里 | 34千卡", 0, textSpacing*2f, paint);
        Bitmap watchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_watch);
        canvas.scale(0.5f,0.5f , 0,radius-watchBitmap.getHeight()*1.5f/2f);
        canvas.drawBitmap(watchBitmap, -watchBitmap.getWidth()/2,radius-watchBitmap.getHeight()*1.5f, paint);
        canvas.restore();

        //线
        canvas.save();
        canvas.rotate(lineDegrees, centerX, centerY);
        int strokeWidth = 1;
        int color1 = ((oldCircleAlpha-circleAlpha)*0xff/0xaa << 24) + 0xffffff;
        Shader shader = new SweepGradient(centerX, centerY, 0x00ffffff, color1);
        paint.setShader(shader);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        for(int i = 0; i < lineNum; i++) {
            float temp = (i-halfLineNum)*strokeWidth*4;
            canvas.drawOval(centerX-radius-temp-+changeRandomLift[i], centerY-radius-temp-+changeRandomTop[i], centerX+radius+temp, centerY+radius+temp+changeRandomBottem[i], paint);
        }
        //喷射粒子
        for (int i = 0; i < starModels.length; i++){
            if (starModels[i] != null){
                starModels[i].draw(canvas, color1);
            }
        }
        canvas.restore();
    }

    private class AnimationThread extends Thread{
        @Override
        public void run() {
            while(true){
                if (isThreadRun){
                    if (count < starModels.length){
                        for (int i = count; (i < count+addCount) && (i < starModels.length); i++){
                            float veloX = (float) (Math.random()*2-1.5);
                            float veloY = (float) -(Math.random()*4+1);
                            starModels[i] = new StarModel(new PointF(startPoint.x, startPoint.y), veloX, veloY);
                        }
                        count += addCount;
                    }
                    for (int i = 0; (i < starModels.length) && (starModels[i] != null); i++){
                        starModels[i].reflash();
                    }
                    invalidate();
                }
                try {
                    sleep(1000/60);
                }catch (InterruptedException ignore){
                }
            }
        }
    }
}
