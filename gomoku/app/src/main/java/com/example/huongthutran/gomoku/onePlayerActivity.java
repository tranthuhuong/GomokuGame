package com.example.huongthutran.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.huongthutran.gomoku.PlayerVsBot.GomokuBoard;

public class onePlayerActivity extends AppCompatActivity {
    ImageView imageView;
    GomokuBoard gomokuBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
        imageView=findViewById(R.id.imgViewGomoku1);
        gomokuBoard = new GomokuBoard(onePlayerActivity.this, 600,600,3,3);
        gomokuBoard.init();
        imageView.setImageBitmap(gomokuBoard.drawBoard());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    return gomokuBoard.onTouch(view, motionEvent);
                } else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    return gomokuBoard.onTouchBot(view, motionEvent);

                } return true;

            }
        });
    }
}
