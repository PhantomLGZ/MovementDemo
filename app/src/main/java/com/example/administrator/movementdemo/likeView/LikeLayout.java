package com.example.administrator.movementdemo.likeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.movementdemo.R;

public class LikeLayout extends LinearLayout implements View.OnClickListener {

    private LikeView likeView1;
    private LikeView likeView2;
    private LikeView likeView3;

    public LikeLayout(Context context) {
        super(context);
    }

    public LikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        likeView1 = findViewById(R.id.like_view1);
        likeView1.setOnClickListener(this);
        likeView1.setLike(false);
        likeView1.setNum(9);
        likeView2 = findViewById(R.id.like_view2);
        likeView2.setOnClickListener(this);
        likeView2.setLike(false);
        likeView2.setNum(239);
        likeView3 = findViewById(R.id.like_view3);
        likeView3.setOnClickListener(this);
        likeView3.setLike(true);
        likeView3.setNum(100000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like_view1:
                likeView1.playAnime();
                break;
            case R.id.like_view2:
                likeView2.playAnime();
                break;
            case R.id.like_view3:
                likeView3.playAnime();
                break;
        }
    }
}
