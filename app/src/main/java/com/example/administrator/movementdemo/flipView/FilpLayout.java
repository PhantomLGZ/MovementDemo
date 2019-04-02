package com.example.administrator.movementdemo.flipView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.administrator.movementdemo.R;

public class FilpLayout extends RelativeLayout {

    private FilpView filpView;
    private Button button;

    public FilpLayout(Context context) {
        super(context);
    }

    public FilpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilpLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        filpView = findViewById(R.id.filp_view);
        button = findViewById(R.id.filp_buttom);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filpView.playAnime();
            }
        });
    }
}
