package com.example.administrator.movementdemo.moveView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.administrator.movementdemo.R;

public class MoveLayout extends RelativeLayout {

    MoveView moveView;
    Button startButton;

    public MoveLayout(Context context) {
        super(context);
    }

    public MoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        moveView = (MoveView) findViewById(R.id.move_view);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moveView.isAnimeEnd()){
                    moveView.startAnima();
                    Log.e("button", "down");
                }
            }
        });
    }
}
