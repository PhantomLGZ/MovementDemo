package com.example.administrator.movementdemo.rulerView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.movementdemo.R;

public class RulerLayout extends RelativeLayout {

    private RulerView rulerView;

    public RulerLayout(Context context) {
        super(context);
    }

    public RulerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RulerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rulerView = findViewById(R.id.ruler_view);
        /*rulerView.setOnTouchListener(new OnTouchListener() {
            int lastX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e("event", "down");
                        lastX = x;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("event", "move");
                        getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        rulerView.playAnime( x - lastX);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("event", "up");
                        break;
                }
                return true;
            }
        });*/
    }
}
