package com.example.administrator.movementdemo.flipView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.movementdemo.R;

public class FilpView extends View {

    private Paint paint = new Paint();
    private Bitmap bitmap ;
    private BitmapFactory.Options options;
    private Camera camera = new Camera();

    private int halfOfWidth;
    private int halfOfHeight;
    //Anime
    private int degrees = 0;
    private int upDegrees = 0;
    private int lastDegrees = 0;

    {
        options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filpboard, options);
        halfOfWidth = bitmap.getWidth()/2;
        halfOfHeight = bitmap.getHeight()/2;
    }

    public FilpView(Context context) {
        super(context);
    }

    public FilpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bitmap.getWidth()*2,
                bitmap.getHeight()*2);
    }

    public void playAnime(){
        reset();
        ObjectAnimator animator0 = ObjectAnimator.ofInt(this, "upDegrees", 0, -15);
        animator0.setDuration(1000);

        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "degrees", 0, -270);
        animator1.setDuration(2000);

        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "lastDegrees", 0, 15);
        animator2.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).before(animator2).after(animator0);
        animatorSet.start();
    }

    private void reset(){
        degrees = 0;
        upDegrees = 0;
        lastDegrees = 0;
        invalidate();
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
        invalidate();
    }

    public void setUpDegrees(int upDegrees) {
        this.upDegrees = upDegrees;
        invalidate();
    }

    public void setLastDegrees(int lastDegrees) {
        this.lastDegrees = lastDegrees;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Log.e("width", ""+halfOfWidth);
//        Log.e("height", ""+halfOfHeight);

        canvas.translate(bitmap.getWidth(), bitmap.getHeight());
        canvas.save();
        canvas.rotate(degrees);
        camera.save();
        canvas.clipRect(0, -bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight());
        canvas.rotate(-degrees);
        camera.rotateZ(-degrees);
        camera.rotateY(upDegrees);
        camera.rotateZ(degrees);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.drawBitmap(bitmap, -halfOfWidth, -halfOfHeight, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(degrees);
        canvas.clipRect(-bitmap.getWidth(), -bitmap.getHeight(), 0, bitmap.getHeight());//要改
        canvas.rotate(-degrees);
        camera.save();
        camera.rotateZ(-degrees);
        camera.rotateY(lastDegrees);
        camera.rotateZ(degrees);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.drawBitmap(bitmap, -halfOfWidth, -halfOfHeight, paint);
        canvas.restore();
        canvas.translate(-bitmap.getWidth(), -bitmap.getHeight());

    }
}
