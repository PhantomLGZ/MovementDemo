package com.example.administrator.movementdemo.likeView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.movementdemo.R;

public class LikeView extends View {

    private boolean isLike = true;
    private int num = 12345;
    String text = num+"    ";
    String oldText = "";
    String newText = "";
    int changeNum = text.length();
    private float textWidth;

    //Anime
    private int unlikeAlpha = 0xff;
    private int likeAlpha = 0x00;
    private int circleAlpha = 0x00;
    private float shineScale = 1f;
    private int radius = 70;
    private int textAlpha = 0x00;
    private int textChangeX = 0;

    private Paint paint = new Paint();

    private BitmapFactory.Options options = new BitmapFactory.Options();;
    private Bitmap like;
    private Bitmap likeShine;
    private Bitmap unlike;
    private Paint.FontMetrics fontMetrics;


    {
        initLike();
        options.inScaled= true;
        like = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected, options);
        likeShine = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining, options);
        unlike = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected, options);
        paint.setTextSize(like.getHeight());
        reflashTextWidth();
    }

    private void reflashTextWidth(){
        fontMetrics = paint.getFontMetrics();
        textWidth = paint.measureText(text);
    }

    public LikeView(Context context) {
        super(context);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLike(Boolean isLike){
        this.isLike = isLike;
        initLike();
    }

    public void setNum(int num){
        this.num = num;
        text = num + "    ";
        changeNum = text.length();
        reflashTextWidth();
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = like.getWidth()*2 + Math.round(textWidth);
        layoutParams.height = like.getHeight()*2;
        this.setLayoutParams(layoutParams);
        invalidate();
        reflashNum();
    }

    private void initLike(){
        if (isLike){
            likeAlpha = 0xff;
            unlikeAlpha = 0x00;
        }else{
            likeAlpha = 0x00;
            unlikeAlpha = 0xff;
        }
    }

    public void addNum(){
        this.num++;
        reflashNum();
    }

    public void subNum(){
        this.num--;
        reflashNum();
    }

    private void reflashNum(){
        oldText = text.substring(changeNum);
        text = num+"    ";
        newText = text.substring(changeNum);
    }


    public void setUnlikeAlpha(int unlikeAlpha) {
        this.unlikeAlpha = unlikeAlpha;
        invalidate();
    }

    public void setLikeAlpha(int likeAlpha) {
        this.likeAlpha = likeAlpha;
        invalidate();
    }

    public void setCircleAlpha(int circleAlpha) {
        this.circleAlpha = circleAlpha;
        invalidate();
    }

    public void setShineScale(float shineScale) {
        this.shineScale = shineScale;
        invalidate();
    }

    public void setTextAlpha(int textAlpha) {
        this.textAlpha = textAlpha;
        invalidate();
    }

    public void setTextChangeX(int textChangeX) {
        this.textChangeX = textChangeX;
        invalidate();
    }

    public void playAnime(){
        changeNum = changeNum();
        if (isLike){
            subNum();
            toUnlikeAnime();
        }else{
            addNum();
            toLikeAnime();
        }
        isLike = !isLike;
    }

    private void toLikeAnime(){
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "unlikeAlpha", 0xff, 0x00);
        animator1.setDuration(10);

        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "likeAlpha", 0x00, 0xff);
        animator2.setDuration(200);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "shineScale", 0.2f, 1f);
        animator3.setDuration(500);

        ObjectAnimator animator4 = ObjectAnimator.ofInt(this, "circleAlpha", 0x00, 0xcc);
        animator4.setDuration(600);
        animator4.setInterpolator(new CycleInterpolator(0.5f));

        ObjectAnimator animator5 = ObjectAnimator.ofInt(this, "textAlpha", 0x00, 0xff);
        animator5.setDuration(600);
        animator5.setInterpolator(new FastOutSlowInInterpolator());

        ObjectAnimator animator6 = ObjectAnimator.ofInt(this, "textChangeX", 0, -like.getHeight());
        animator6.setDuration(600);
        animator6.setInterpolator(new FastOutSlowInInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2).with(animator3).with(animator4).with(animator5).with(animator6);
        animatorSet.start();
    }

    private void toUnlikeAnime(){
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "likeAlpha", 0xff, 0x00);
        animator1.setDuration(100);

        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "unlikeAlpha", 0x00, 0xff);
        animator2.setDuration(10);

        ObjectAnimator animator3 = ObjectAnimator.ofInt(this, "textAlpha", 0x00, 0xff);
        animator3.setDuration(600);
        animator3.setInterpolator(new FastOutSlowInInterpolator());

        ObjectAnimator animator4 = ObjectAnimator.ofInt(this, "textChangeX", 0, like.getHeight());
        animator4.setDuration(600);
        animator4.setInterpolator(new FastOutSlowInInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2).with(animator3).with(animator4);
        animatorSet.start();
    }

    private int changeNum(){
        String temp = text.trim();
        int result = temp.length()-1;
        if(isLike){
            for(int i = result; i >= 0; i--) {
                if ("0".equals(temp.substring(i,i+1))) {
                    result--;
                } else {
                    break;
                }
            }
        }else{
            for(int i = result; i >= 0; i--) {
                if ("9".equals(temp.substring(i,i+1))) {
                    result--;
                } else {
                    break;
                }
            }
        }
//        Log.e("前", temp.substring(0, result));
//        Log.e("后", temp.substring(result));
        if(result < 0){
            result = 0;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.e("width", (like.getWidth()*2 + Math.round(textWidth))+" : "+getDefaultSize(like.getWidth()*2 + Math.round(textWidth), widthMeasureSpec));
//        Log.e("height", like.getHeight()*2+" : "+getDefaultSize(like.getHeight()*2, widthMeasureSpec));
        setMeasuredDimension(like.getWidth()*2 + Math.round(textWidth),
                like.getHeight()*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0xff383838);
        paint.setColor(0xffff6445);

        //赞
        canvas.save();
        canvas.translate(0, like.getHeight());
        paint.setAlpha(unlikeAlpha);
        canvas.drawBitmap(unlike, like.getWidth()/2, -like.getHeight()/2, paint);
        paint.setAlpha(likeAlpha);
        canvas.drawBitmap(like, like.getWidth()/2, -like.getHeight()/2, paint);
        canvas.restore();

        //环
        canvas.save();
        canvas.translate(like.getWidth(), like.getHeight());
        canvas.scale(shineScale,shineScale);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAlpha(circleAlpha);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();

        //光线
        canvas.save();
        paint.setAlpha(likeAlpha);
        canvas.translate(like.getWidth(),like.getHeight()/2);
        canvas.scale(shineScale,shineScale);
        canvas.drawBitmap(likeShine,-likeShine.getWidth()/2, -likeShine.getHeight()/2, paint);
        canvas.restore();

        //文本
        canvas.save();
        paint.reset();
        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(like.getHeight());
        canvas.translate(0, like.getHeight());
        String unchangedText = text.substring(0, changeNum);
        canvas.drawText(unchangedText,like.getWidth()*2, -(fontMetrics.top+fontMetrics.bottom)/2, paint);
        int unchangedWidth = Math.round(paint.measureText(unchangedText));
//        Log.e("unchangedText", unchangedText);
//        Log.e("oldText", oldText);
//        Log.e("newText", newText);
        paint.setAlpha(0xff-textAlpha);
        canvas.translate(0, textChangeX);
        canvas.drawText(oldText,like.getWidth()*2+unchangedWidth, -(fontMetrics.top+fontMetrics.bottom)/2, paint);
        paint.setAlpha(textAlpha);
        if (isLike){
            canvas.drawText(newText,like.getWidth()*2+unchangedWidth, -(fontMetrics.top+fontMetrics.bottom)/2+like.getHeight(), paint);
        }else {
            canvas.drawText(newText,like.getWidth()*2+unchangedWidth, -(fontMetrics.top+fontMetrics.bottom)/2-like.getHeight(), paint);
        }
        canvas.restore();
    }
}
