package com.example.huongthutran.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class onePlayerActivity extends AppCompatActivity {
    ImageView imageView;
    GomokuBoard gomokuBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
        imageView=findViewById(R.id.imgViewGomoku1);
        gomokuBoard = new GomokuBoard(onePlayerActivity.this, 800,800,4,4);
        gomokuBoard.init();
        imageView.setImageBitmap(gomokuBoard.drawBoard());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gomokuBoard.onTouch(view, motionEvent);
            }
        });
    }
}
